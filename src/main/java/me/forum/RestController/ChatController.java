package me.forum.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import me.forum.Dao.MessageDao;
import me.forum.Dao.UserDao;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.SocketHandler;
import me.forum.WebSocketSetup.UserHandler;

@RestController
public class ChatController {
	@Autowired
	UserDao userDao;
	@Autowired
	MessageDao messageDao;
	@RequestMapping(value = "/chatHandler", method = RequestMethod.POST)
	public Map<String, String> chatHandler(HttpServletRequest request, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		String toUser = request.getParameter("user");
		String type = request.getParameter("type");
		String message = request.getParameter("message");
		System.out.println(message);
		User user, user1;
		user = (User) session.getAttribute("userID");
		user1 = userDao.findUserByUserName(toUser);
		if (user == null || user1 == null)
			return map;
		if ("requestChat".equals(type)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("type", type);
			jsonObject.put("token", user.getMabaomat());
			jsonObject.put("message", message);
			try {
				messageDao.AddMessage(user.getTaikhoan(), toUser, message);
				SocketHandler.GetInstance().handleTextMessage(
						UserHandler.GetInstance().GetAllUsers().get(user.getTaikhoan()),
						new TextMessage(jsonObject.toString()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return map;
	}

	@RequestMapping(value = "/stopBotSession", method = RequestMethod.POST)
	public Map<String, String> stopSession(@RequestParam boolean stopBot, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		User user;
		user = (User) session.getAttribute("userID");
		if(user == null) return map;
		UserHandler.bots.get(user.getTaikhoan()).stop();
		return map;
	}
}
