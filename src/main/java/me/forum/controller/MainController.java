package me.forum.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
