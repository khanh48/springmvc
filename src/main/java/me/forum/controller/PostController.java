package me.forum.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Entity.Comment;
import me.forum.Entity.HTML;
import me.forum.Entity.Image;
import me.forum.Entity.Notification;
import me.forum.Entity.Post;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

@Controller
public class PostController extends BaseController {

	public PostController() {

	}

	@RequestMapping(value = "/loadPost", method = RequestMethod.GET)
	@ResponseBody
	public List<String> loadOnScroll(@RequestParam int start, @RequestParam int limit,
			@RequestParam(required = false) String uid, HttpSession session) {

		List<String> list = new ArrayList<>();
		List<Post> posts;
		posts = uid.isEmpty() ? postDao.GetPostsLimitDesc(start, limit) : postDao.GetPostsUserLimit(uid, start, limit);
		User user = (User) session.getAttribute("userID");
		for (Post p : posts) {
			list.add(HTML.GetPost(user, p, true));
		}
		return list;
	}

	@RequestMapping(value = "/loadComment", method = RequestMethod.GET)
	@ResponseBody
	public List<String> loadCommentOnScroll(@RequestParam long id, @RequestParam int start, @RequestParam int limit,
			HttpSession session) {

		List<String> list = new ArrayList<>();
		List<Comment> comments = commentDao.GetPostsLimitDesc(id, start, limit);
		User user = (User) session.getAttribute("userID");
		for (Comment cmt : comments) {
			list.add(HTML.GetComment(user, cmt));
		}
		return list;
	}

	@RequestMapping(value = "/addPost", method = RequestMethod.POST)
	public ModelAndView addPost(@RequestParam("uploadImg") List<MultipartFile> uploadImg, HttpSession session,
			HttpServletRequest request) throws IllegalStateException, IOException {
		String tieude, noidung;
		int nhom;
		tieude = request.getParameter("tieude");
		noidung = request.getParameter("noidung");
		nhom = Integer.valueOf(request.getParameter("nhom"));
		mav.setViewName("redirect:/");
		User user = (User) session.getAttribute("userID");
		String rootPath = request.getServletContext().getRealPath("/");
		if (user != null) {
			String pathImg;
			long curTime = System.currentTimeMillis();
			if (postDao.AddPost(curTime, tieude, noidung, nhom, user.getTaikhoan()) > 0) {
				for (MultipartFile file : uploadImg) {
					if (!file.isEmpty()) {
						pathImg = "/resources/images/" + user.getTaikhoan() + "/" + file.getOriginalFilename();

						File folder = new File(rootPath + pathImg);
						if (!folder.exists()) {
							folder.mkdirs();
						}
						file.transferTo(folder);
						imageDao.AddImage(user.getTaikhoan(), pathImg, curTime);
					}
				}
			}
		}
		return mav;
	}

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public ModelAndView addComment(@RequestParam(name = "send") long id,HttpSession session, HttpServletRequest request) {
		String content;
		content = request.getParameter("comment");
		mav.setViewName("redirect:/bai-viet/" + id);
		User user = (User) session.getAttribute("userID");
		Post post = postDao.GetPostByID(id);
		if (user != null && post != null) {
			long curTime = System.currentTimeMillis();
			commentDao.AddComment(content, user.getTaikhoan(), id);
			if(!user.equals(post.getUser())) {
			
				String thongbao, url;
				thongbao= user.getHoten() + " đã bình luận trong bài viết của bạn.";
				url= "/bai-viet/" + id + "/" + curTime;
			
				Notification notification = new Notification(curTime, user.getTaikhoan(), thongbao, post.getUser().getTaikhoan(), url);
				notificationDao.AddNotification(notification);
				JSONObject json = new JSONObject();
				json.put("type", "newNotification");
				json.put("message", thongbao);
				json.put("url", url);
				json.put("date", notification.getDateFormated());
				UserHandler.GetInstance().send(post.getUser().getTaikhoan(), json.toString());
			}
			
		}
		return mav;
	}
	

}
