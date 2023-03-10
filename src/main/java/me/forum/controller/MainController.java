package me.forum.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Entity.Comment;
import me.forum.Entity.Post;
import me.forum.Entity.User;

@Controller
public class MainController extends BaseController {

	public MainController() {
	}

	@RequestMapping(value = { "/", "/index" })
	public ModelAndView homePage(HttpSession session, HttpServletRequest request) {
		mav.setViewName("index");
		mav.addObject("posts", postDao.GetPostsLimitDesc(0, 10));
		if (session.getAttribute("userID") != null)
			session.setAttribute("listNotify",
					notificationDao.GetByNguoiNhan(((User) session.getAttribute("userID")).getTaikhoan()));
		return mav;
	}


	@RequestMapping(value = { "/ho-so/{username}", "/profile/{username}" })
	public ModelAndView profilePage(@PathVariable("username") String username) {
		mav.setViewName("profile");
		User user = userDao.findUserByUserName(username);
		if(user == null) {
			mav.addObject("profile", null);
			mav.addObject("postOfUser", null);
			return mav;
			
		}
		List<Post> post = postDao.GetPostsByUser(username);

		mav.addObject("profile", user);
		mav.addObject("postOfUser", post);
		return mav;
	}
	

	@RequestMapping(value = { "/ho-so", "/profile" })
	public ModelAndView myProfilePage(HttpSession session) {
		mav.setViewName("profile");
		User user = (User)session.getAttribute("userID");
		if(user == null) {
			mav.addObject("profile", null);
			mav.addObject("postOfUser", null);
			return mav;
			
		}
		List<Post> post = postDao.GetPostsByUser(user.getTaikhoan());

		mav.addObject("profile", user);
		mav.addObject("postOfUser", post);
		return mav;
	}
	
	@RequestMapping(value = { "/bai-viet/{id}", "/post/{id}" })
	public ModelAndView postPage(@PathVariable long id, HttpSession session) {
		Post post = postDao.GetPostByID(id);
			mav.setViewName("post");
		if(post == null) {
			mav.addObject("post", null);
			mav.addObject("comments", null);
			return mav;
		}
		List<Comment> comments = commentDao.GetPostsLimitDesc(id, 0, 10);
		mav.addObject("post", post);
		mav.addObject("comments", comments);
		if (session.getAttribute("userID") != null)
			session.setAttribute("listNotify",
					notificationDao.GetByNguoiNhan(((User) session.getAttribute("userID")).getTaikhoan()));
		return mav;
	}

	@RequestMapping(value = { "/bai-viet/{id}/{nid}", "/post/{id}/{nid}" })
	public ModelAndView postPageReaded(@PathVariable long id, @PathVariable long nid) {
		notificationDao.MakeAsRead(nid);
		mav.setViewName("redirect:/bai-viet/" + id);
		return mav;
	}
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {

		mav.setViewName("redirect:/");
		if (session.getAttribute("userID") != null) {
			String uname = ((User) session.getAttribute("userID")).getTaikhoan();
			long curTime = System.currentTimeMillis();
			userDao.UpdateMaBaoMat(uname, MainRestController.encrypt(uname, curTime), curTime);
			session.removeAttribute("userID");
		}
		return mav;
	}

}
