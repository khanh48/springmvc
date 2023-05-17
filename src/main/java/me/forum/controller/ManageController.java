package me.forum.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Config.Base;
import me.forum.Entity.Post;
import me.forum.Entity.User;
@Controller
public class ManageController extends BaseController {

	@RequestMapping(value = { "/manage", "/quan-ly" })
	public ModelAndView managePage(HttpSession session, HttpServletRequest request) {

		mav.setViewName("member_manage");
		User user = (User) session.getAttribute(Base.USER);
		if (user == null || user.getRank() < 2) {
			mav.setViewName("redirect:/");
			return mav;
		}
		mav.addObject("userList", userDao.getUserUnderRank(user.getRank()));
		mav.addObject("ruleList", ruleDao.getAll());
		return mav;
	}

	@RequestMapping(value = { "/posts-manage", "/quan-ly-bai-viet" })
	public ModelAndView postsManagePage(HttpSession session, HttpServletRequest request) {

		mav.setViewName("posts_manage");
		User user = (User) session.getAttribute(Base.USER);
		if (user == null || user.getRank() < 2) {
			mav.setViewName("redirect:/");
			return mav;
		}
		mav.addObject("postList", postDao.GetAll());
		mav.addObject("groupList", groupDao.getGroupList());
		return mav;
	}
	

	@RequestMapping(value = "/user-manage", method = RequestMethod.POST)
	public ModelAndView userManage(@RequestParam(name = "checkbox", required = false) int[] checkbox,
			@RequestParam(name = "chucvu") int[] chucvu, HttpServletRequest request, HttpSession session) {
		mav.setViewName("redirect:/quan-ly");
		User user = (User) session.getAttribute(Base.USER);

		if (user != null && checkbox != null) {

			User user2;
			String[] sdt, hoten, email, taikhoan;
			sdt = request.getParameterValues("sdt");
			hoten = request.getParameterValues("hoten");
			email = request.getParameterValues("email");
			taikhoan = request.getParameterValues("taikhoan");
			if (user.getRank() >= 2) {
				for (int i : checkbox) {
					if (request.getParameter("save") != null) {
						user2 = userDao.findUserByUserName(taikhoan[i]);
						user2.setSodienthoai(sdt[i]);
						user2.setHoten(hoten[i]);
						user2.setEmail(email[i]);
						if (chucvu[i] < user.getRank() && user2.getRank() < user.getRank()) {
							user2.setChucvu(ruleDao.getById(chucvu[i]));
						}
						userDao.UpdateUser(user2);
					} else if(request.getParameter("save") == null){
						userDao.RemoveUser(taikhoan[i]);
					}
				}
			}
		}
		return mav;
	}

	@RequestMapping(value = "/post-manage", method = RequestMethod.POST)
	public ModelAndView userManage(@RequestParam(name = "checkbox", required = false) int[] checkbox,
			@RequestParam(name = "nhom") int[] nhom, @RequestParam(name = "mabaiviet") long[] mabaiviet, HttpServletRequest request, HttpSession session) {
		mav.setViewName("redirect:/quan-ly-bai-viet");
		User user = (User) session.getAttribute(Base.USER);

		if (user != null && checkbox != null) {
			String[] tieude, noidung;
			tieude = request.getParameterValues("tieude");
			noidung = request.getParameterValues("noidung");
			if (user.getRank() >= 2) {
				for (int i : checkbox) {
					if (request.getParameter("save") != null) {
						Post post = postDao.GetPostByID(mabaiviet[i]);
						post.setTieude(tieude[i]);
						post.setNoidung(noidung[i]);
						post.setNhom(nhom[i]);
						postDao.UpdatePost(post);
					}else if(request.getParameter("save") == null){
						postDao.DeletePostByID(mabaiviet[i]);
					}
				}
			}

		}
		return mav;
	}
}
