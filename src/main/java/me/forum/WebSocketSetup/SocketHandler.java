package me.forum.WebSocketSetup;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import me.forum.Component.JwtProvider;
import me.forum.Controller.BaseController;
import me.forum.Dao.UserDao;
import me.forum.Entity.User;
import me.forum.Module.ChatBot;

public class SocketHandler extends TextWebSocketHandler {
	private UserHandler handler = UserHandler.GetInstance();
	private HashMap<String, ChatBot> bots = UserHandler.bots;
	private UserDao userDao = BaseController.GetInstance().userDao;
	private JwtProvider jwtProvider = JwtProvider.GetInstance();

	private static SocketHandler instance;

	public SocketHandler() {
		instance = this;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		String msg = message.getPayload();
		JSONObject json = new JSONObject(msg);
		if (json.isNull("token"))
			return;
		String token = json.getString("token");
		if (!authentication(token))
			return;
		User user = userDao.findUserByUserName(jwtProvider.extractUsername(token));
		switch (json.getString("type")) {
		case "requestChat":
			if (!bots.containsKey(user.getTaikhoan()) || bots.get(user.getTaikhoan()).isStoped()) {
				bots.put(user.getTaikhoan(), new ChatBot(user));
			}
			bots.get(user.getTaikhoan()).request(json.get("message").toString());
			break;
		default:
			handler.addClient(user.getTaikhoan(), session);
			String[] path = String.valueOf(json.get("path")).split("/");
			if(path.length >= 3) {
				handler.addChat(path[2], user.getTaikhoan(), session);
			}
		}

	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

	}

	public boolean authentication(String token) {
		if (userDao.findUserByCrypt(token) == null)
			return false;
		return jwtProvider.validateToken(token);
	}

	public static SocketHandler GetInstance() {
		return instance;
	}
}
