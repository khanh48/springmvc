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

public class ChatHandler extends TextWebSocketHandler{
	private UserHandler handler = UserHandler.GetInstance();
	private HashMap<String, ChatBot> bots = UserHandler.bots;
	private UserDao userDao = BaseController.GetInstance().userDao;
	private JwtProvider jwtProvider = JwtProvider.GetInstance();

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
		String msg = message.getPayload();
		System.out.println(session.getUri().getUserInfo());
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
}
