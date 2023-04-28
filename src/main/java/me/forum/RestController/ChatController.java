package me.forum.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import me.forum.Entity.Message;
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
		User user, user1;
		user = (User) session.getAttribute("userID");
		user1 = userDao.findUserByUserName(toUser);
		if (user == null || user1 == null)
			return map;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("message", message);
		
		if ("requestChat".equals(type)) {
			jsonObject.put("token", user.getMabaomat());
			try {
				messageDao.AddMessage(user.getTaikhoan(), toUser, message);
				SocketHandler.GetInstance().handleTextMessage(
						UserHandler.GetInstance().GetAllUsers().get(user.getTaikhoan()),
						new TextMessage(jsonObject.toString()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else if ("chat".equals(type)) {
			jsonObject.put("avatar", user.getAnhdaidien());
			jsonObject.put("sender", "other-chat");
			messageDao.AddMessage(user.getTaikhoan(), toUser, message);
			UserHandler.GetInstance().sendChat(user.getTaikhoan(), toUser, jsonObject.toString());
		}
		return map;
	}

	@RequestMapping(value = "/stopBotSession", method = RequestMethod.POST)
	public Map<String, String> stopSession(@RequestParam boolean stopBot, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		User user;
		user = (User) session.getAttribute("userID");
		if (user == null)
			return map;
		UserHandler.bots.get(user.getTaikhoan()).stop();
		return map;
	}

	@RequestMapping(value = "/loadMessage", method = RequestMethod.POST)
	public Map<String, Object> loadMessage(@RequestParam int start, @RequestParam int limit, @RequestParam String uid,
			HttpSession session) {
		HashMap<String, Object> map = new HashMap<>();
		User user, user2;
		user = (User) session.getAttribute("userID");
		user2 = userDao.findUserByUserName(uid);
		if (user == null)
			return map;
		List<Map<String, Object>> text = new ArrayList<>();
		for (Message msg : messageDao.getLimitMessage(user.getTaikhoan(), uid, start, limit)) {
			Map<String, Object> json = new HashMap<>();
			if (msg.getNguoigui().equals(user)) {
				json.put("sender", "my-chat");
			} else {
				json.put("sender", "other-chat");
				json.put("avatar", user2.getAnhdaidien());
			}
			json.put("message", msg.getNoidung());
			json.put("time", msg.getFomattedDate());
			text.add(json);
		}
		map.put("payLoad", text);
		return map;
	}
}
