package me.forum.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.ModelAndView;

import me.forum.entity.Comment;
import me.forum.entity.Post;
import me.forum.entity.User;


@Controller
public class MainController extends BaseController{
	private static MainController instance;
	
	
	public MainController() {
		instance = this;
	}
	
	public static MainController GetInstance() {
		return instance;
	}
	
	@RequestMapping(value = {"/","/index"})
	public ModelAndView homePage(HttpSession session) {
		mav.setViewName("index");
		mav.addObject("posts", postDao.GetPostsLimitDesc(0, 10));
		if(session.getAttribute("userID") != null)
			session.setAttribute("listNotify",  notificationDao.GetByNguoiNhan(((User)session.getAttribute("userID")).getTaikhoan()));
		return mav;
	}
	

	@RequestMapping(value = {"/bai-viet/{id}","/post/{id}"})
	public ModelAndView postPage(@PathVariable long id, HttpSession session) {
		Post post = postDao.GetPostByID(id);
		System.out.println(post.getUser().getHoten());
		List<Comment> comments = commentDao.GetByPostID(id);
		mav.setViewName("post");
		mav.addObject("post", post);
		mav.addObject("comments", comments);
		if(session.getAttribute("userID") != null)
			session.setAttribute("listNotify",  notificationDao.GetByNguoiNhan(((User)session.getAttribute("userID")).getTaikhoan()));
		return mav;
	}
	
	@RequestMapping(value = {"/bai-viet/{id}/{nid}","/post/{id}/{nid}"})
	public ModelAndView postPageReaded(@PathVariable long id, @PathVariable long nid) {
		notificationDao.MakeAsRead(nid);
		mav.setViewName("redirect:/bai-viet/" + id);
		return mav;
	}
	
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		mav.setViewName("redirect:index");
		if(session.getAttribute("userID") != null) {
			String uname = ((User)session.getAttribute("userID")).getTaikhoan();
			long curTime = System.currentTimeMillis();
			userDao.UpdateMaBaoMat(uname, MainRestController.encrypt(uname, curTime), curTime);
			session.removeAttribute("userID");
		}
		return mav;
	}

}
