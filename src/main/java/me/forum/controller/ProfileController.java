package me.forum.Controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Entity.User;

@Controller
public class ProfileController extends BaseController {

	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ModelAndView updateProfile(@RequestParam(name = "avt", required = false) MultipartFile file,
			HttpServletRequest request, HttpSession session) {

		String hoten, gioitinh, sodienthoai, sothich, pathImg, rootPath;
		Date ngaysinh;
		User user = (User) session.getAttribute("userID");
		mav.setViewName("redirect:/ho-so");
		if (user == null) {
			return mav;
		}
		hoten = request.getParameter("hoten");
		gioitinh = request.getParameter("gioitinh");
		sodienthoai = request.getParameter("sdt");
		sothich = request.getParameter("sothich");
		pathImg = user.getAnhdaidien();
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd");
		try {
			ngaysinh = pa.parse(request.getParameter("ngaysinh"));
		} catch (ParseException e) {
			ngaysinh = null;
		}
		rootPath = request.getServletContext().getRealPath("/");
		if (!file.isEmpty()) {
			pathImg = "/resources/images/" + user.getTaikhoan() + "/" + file.getOriginalFilename();

			File folder = new File(rootPath + pathImg);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			try {
				file.transferTo(folder);
			} catch (IllegalStateException | IOException e) {
				pathImg = user.getAnhdaidien();
			}
		}
		userDao.UpdateProfile(user.getTaikhoan(), hoten, gioitinh, ngaysinh, sodienthoai, sothich, pathImg);
		session.setAttribute("userID", userDao.findUserByUserName(user.getTaikhoan()));
		return mav;
	}

	@RequestMapping(value = "/manage-user", method = RequestMethod.POST)
	public ModelAndView Test(@RequestParam(name = "checkbox", required = false) int[] checkbox,
			@RequestParam(name = "chucvu") int[] chucvu, HttpServletRequest request, HttpSession session) {
		mav.setViewName("redirect:/quan-ly");
		User user = (User) session.getAttribute("userID");

		if (user != null && checkbox != null) {

			User user2;
			String[] sdt, hoten, email, taikhoan;
			sdt = request.getParameterValues("sdt");
			hoten = request.getParameterValues("hoten");
			email = request.getParameterValues("email");
			taikhoan = request.getParameterValues("taikhoan");
			if (request.getParameter("save") != null) {
				if (user.getRank() >= 2) {


					for (int i : checkbox) {
						user2 = userDao.findUserByUserName(taikhoan[i]);
						user2.setSodienthoai(sdt[i]);
						user2.setHoten(hoten[i]);
						user2.setEmail(email[i]);
						if (chucvu[i] < user.getRank()) {
							user2.setChucvu(ruleDao.getById(chucvu[i]));
						}
						userDao.UpdateUser(user2);
					}
				}

			} else if(request.getParameter("save") == null){
				for (int i : checkbox) {
					userDao.RemoveUser(taikhoan[i]);
				}
			}
		}
		return mav;
	}

}
