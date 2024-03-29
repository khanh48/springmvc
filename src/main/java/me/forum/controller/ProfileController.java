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

import me.forum.Config.Base;
import me.forum.Entity.User;

@Controller
public class ProfileController extends BaseController {

	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ModelAndView updateProfile(@RequestParam(name = "avt", required = false) MultipartFile file,
			HttpServletRequest request, HttpSession session) {

		String hoten, gioitinh, sodienthoai, sothich, pathImg, rootPath;
		Date ngaysinh;
		User user = (User) session.getAttribute(Base.USER);
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
		session.setAttribute(Base.USER, userDao.findUserByUserName(user.getTaikhoan()));
		return mav;
	}


}
