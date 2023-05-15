package me.forum.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Dao.GroupDao;
import me.forum.Dao.MessageDao;
import me.forum.Dao.NotificationDao;
import me.forum.Dao.PostDao;
import me.forum.Entity.Message;
import me.forum.Entity.Notification;
import me.forum.Entity.Post;
import me.forum.Entity.User;

@Component
public class Interceptor implements HandlerInterceptor {
	@Autowired
	NotificationDao notificationDao;
	@Autowired
	MessageDao messageDao;
	@Autowired
	GroupDao groupDao;
	@Autowired
	PostDao postDao;

	public Interceptor() {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("userID");
		if (user != null) {
			int unread = 0, unreadMessage = 0;
			List<Notification> listNotify = notificationDao.GetByNguoiNhan(user.getTaikhoan());
			List<Message> listMessage = messageDao.getMessageOfUser(user.getTaikhoan());

			for (Notification notification : listNotify) {
				if (!notification.isTrangthai()) {
					unread++;
				}
			}
			for (Message message : listMessage) {
				if (!message.isTrangthai()) {
					unreadMessage++;
				}
			}
			String input = messageDao.getMotto().get(GenerateInt(0, messageDao.getMotto().size() - 1)).getNoidung();
			Pattern pattern = Pattern.compile("\\-\\s.*.\\.");
			Matcher matcher = pattern.matcher(input);
			if (matcher.find()) {
			    String result = matcher.group();
			    System.out.println(result);
			}
			session.setAttribute("listMessage", listMessage);
			session.setAttribute("unreadMessage", unreadMessage);
			session.setAttribute("unread", unread);
			session.setAttribute("listNotify", listNotify);

		}
		List<Post> groups = postDao.getTopList();
		List<List<Post>> allgroup = new ArrayList<>();
		for (Post post : groups) {
			allgroup.add(postDao.ByGroupLimit(post.getManhom(), 0, 3));
		}
		session.setAttribute("bestgroups", allgroup);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
	
	static int GenerateInt(int min, int max) {
		Random random = new Random();
		int range = max - min + 1;
		return random.nextInt(range) + min;
	}
}
