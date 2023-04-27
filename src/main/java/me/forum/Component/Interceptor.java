package me.forum.Component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Controller.BaseController;
import me.forum.Dao.GroupDao;
import me.forum.Dao.NotificationDao;
import me.forum.Dao.PostDao;
import me.forum.Entity.Notification;
import me.forum.Entity.Post;
import me.forum.Entity.User;

@Component
public class Interceptor implements HandlerInterceptor {
	@Autowired
	NotificationDao notificationDao;
	@Autowired
	GroupDao groupDao;
	@Autowired
	PostDao postDao;

	public Interceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userID");
		if (user != null) {
			int unread = 0;
			List<Notification> listNotify = notificationDao.GetByNguoiNhan(user.getTaikhoan());

			for (Notification notification : listNotify) {
				if (!notification.isTrangthai()) {
					unread++;
				}
			}
			session.setAttribute("unread", unread);
			session.setAttribute("listNotify", listNotify);

		}
		List<Post> groups = postDao.getTopList();
		List<List<Post>> allgroup = new ArrayList<>();
		for (Post post : groups) {
			allgroup.add(postDao.ByGroupLimit(post.getManhom(), 3));
		}
		session.setAttribute("bestgroups", allgroup);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}
}
