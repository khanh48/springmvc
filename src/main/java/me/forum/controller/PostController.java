package me.forum.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Entity.Comment;
import me.forum.Entity.Image;
import me.forum.Entity.Post;
import me.forum.Entity.User;

@Controller
public class PostController extends BaseController {

	public PostController() {

	}

	@RequestMapping(value = "/loadPost", method = RequestMethod.GET)
	@ResponseBody
	public List<String> loadOnScroll(@RequestParam int start, @RequestParam int limit,
			@RequestParam(required = false) String uid, HttpSession session) {
		String numlike, numcmt, uUrl, isliked;
		List<String> list = new ArrayList<>();
		List<Post> posts;
		posts = uid != null ? postDao.GetPostsUserLimit(uid, start, limit) : postDao.GetPostsLimitDesc(start, limit);
		User user = (User) session.getAttribute("userID");
		for (Post p : posts) {
			isliked = "";
			User u = p.getUser();
			uUrl = "/ho-so/" + u.getTaikhoan();
			numlike = p.getCountLike() > 0 ? p.getCountLike() + "" : "";
			numcmt = p.getCountComment() > 0 ? p.getCountComment() + "" : "";
			if (user != null && p.IsLiked(user.getTaikhoan())) {
				isliked = "fas-liked";
			}

			String item = "<div class='content' id='" + p.getMabaiviet() + "'>";
			item += "<div class='d-flex justify-content-between'>";
			item += "<div class=' c-header'>";
			item += "<span> <a class='name' href='" + uUrl + "'>";
			item += "<img class='avt' src='" + u.getAnhdaidien() + "' alt='avatar'>";
			item += "</a></span><div class='c-name'>";
			item += "<span><a class='name' href='" + uUrl + "'>" + u.getHoten() + "</a>";
			item += "<div class='time'>"; 
			item += "<small class='text-secondary'>" + p.getDateFormated() + "</small>";
			item += "</div></span></div></div>";

			if (u.getTaikhoan().equals(user.getTaikhoan()) || user.getRank() >= 2) {
				item += "<button name='delete-notification' class='btn-close py-1 px-3' ";
				item += "value='' data-bs-toggle='modal' data-bs-target='#delete-post' " + "onclick=\"deletePost("
						+ p.getMabaiviet() + ")\"></button>";
			}
			item += "</div><div> <div class='title'>";
			item += "<div class='name'>" + p.getNhom() + "</div>";
			item += "<span>></span><div class='name'>" + p.getTieude() + "</div></div></div>";
			item += "<div class='c-body'>" + p.getNoidung() + "</div>";
			item += "<div class='m-0 hide wh' style='text-align: end;'>";
			item += "<span class='read-more'></span></div>";

			List<Image> imgs = p.getImage();
			if (imgs != null && !imgs.isEmpty()) {
				item += "<div id='forpost" + p.getMabaiviet()
						+ "' class='carousel slide mt-1' data-bs-ride='carousel'>";
				item += "<div class='carousel-inner'>";
				int idx = 0;
				for (Image img : imgs) {
					item += "<div class='carousel-item" + (idx == 0 ? " active" : "") + "'>";
					item += "<img src='" + img.getUrl() + "' class='d-block w-100 postImg' alt='...'></div>";
					idx++;
				}

				item += "</div><button class='carousel-control-prev' type='button' ";
				item += "data-bs-target='#forpost" + p.getMabaiviet() + "' data-bs-slide='prev'>";
				item += "<span class='carousel-control-prev-icon' aria-hidden='true'></span>";
				item += "<span class='visually-hidden'>Previous</span></button>";
				item += "<button class='carousel-control-next' type='button' ";
				item += "data-bs-target='#forpost" + p.getMabaiviet() + "' data-bs-slide='next'>";
				item += "<span class='carousel-control-next-icon' aria-hidden='true'></span>";
				item += "<span class='visually-hidden'>Next</span></button></div>";
			}

			item += "<hr class='m-0'>";
			item += "<div class='interactive p-1 m-0'>";
			item += "<button type='button' class='like p-1' onclick=\"like(" + p.getMabaiviet() + ",true, '"
					+ u.getTaikhoan() + "')\">";
			item += "<i class='fas fa-heart action " + isliked + "' id='pl" + p.getMabaiviet() + "'></i> ";
			item += "<span class='count-like' id='p" + p.getMabaiviet() + "'>" + numlike + "</span></button>";
			item += "<button type='button' class='comment p-1' onclick=\"window.location.href='/bai-viet/"
					+ p.getMabaiviet() + "'\">";
			item += "<i class='fas fa-comment action'></i>";
			item += "<span class='count-comment'>" + numcmt + "</span>";
			item += "</button><button type='button' class='share p-1'>";
			item += "<i class='fas fa-share action'></i><span class='count-share'></span>";
			item += "</button></div></div>";
			list.add(item);
		}
		return list;
	}

	@RequestMapping(value = "/loadComment", method = RequestMethod.GET)
	@ResponseBody
	public List<String> loadCommentOnScroll(@RequestParam long id, @RequestParam int start, @RequestParam int limit,
			HttpSession session) {
		String numlike, uUrl, isliked;

		List<String> list = new ArrayList<>();
		List<Comment> comments = commentDao.GetPostsLimitDesc(id, start, limit);
		User user = (User) session.getAttribute("userID");
		for (Comment cmt : comments) {
			User u = cmt.getUser();
			isliked = "";
			uUrl = "/ho-so/" + u.getTaikhoan();
			numlike = cmt.getCountLike() > 0 ? cmt.getCountLike() + "" : "";
			if (user != null && cmt.IsLiked(user.getTaikhoan())) {
				isliked = "fas-liked";
			}

			String item = "<div class='content'>";
			item += "<div class='d-flex justify-content-between'>";
			item += "<div class='c-header'><span>";
			item += "<a class='name' href='" + uUrl + "'>";
			item += "<img class='avt' src='" + u.getAnhdaidien() + "' alt='avatar'>";
			item += "</a></span><div class='c-name'>";
			item += "<span><a class='name' href='" + uUrl + "'>" + u.getHoten() + "</a>";
			item += "<div class='time'>";
			item += "<small class='text-secondary'>" + cmt.getDateFormated() + "</small>";
			item += "</div> </span></div></div>";

			if (u.getTaikhoan().equals(user.getTaikhoan()) || user.getRank() >= 2) {
				item += "<button name='delete-notification' class='btn-close py-1 px-3' "
						+ "value='a' data-bs-toggle='modal' data-bs-target='#delete-post' onclick=\"deleteCmt("
						+ cmt.getMabinhluan() + ")\"></button>";
			}
			item += "</div><div class='c-body'>" + cmt.getNoidung() + "</div>";
			item += "<div class='m-0 hide wh' style='text-align: end;'>";
			item += "<span class='read-more'></span></div><hr class='m-0'>";
			item += "<div class='interactive p-1 m-0'>";
			item += "<button type='button' class='like p-1' ";
			item += "onclick=\"like(" + cmt.getMabinhluan() + ",false, '" + u.getTaikhoan() + "')\">";
			item += "<i class='fas fa-heart action " + isliked + "' id='cl" + cmt.getMabinhluan() + "'>";
			item += "</i><span class='count-like' id='c" + cmt.getMabinhluan() + "'>";
			item += numlike + "</span></button>" + "</div></div>";
			list.add(item);
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
	public ModelAndView addComment(HttpSession session, HttpServletRequest request) {
		String content;
		long id;

		content = request.getParameter("comment");
		id = Long.parseLong(request.getParameter("send"));
		mav.setViewName("redirect:/bai-viet/" + id);
		User user = (User) session.getAttribute("userID");
		if (user != null)
			commentDao.AddComment(content, user.getTaikhoan(), id);
		return mav;
	}
	

}
