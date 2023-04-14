package me.forum.WebSocketSetup;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketHandler extends TextWebSocketHandler {

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Map<String, Object> attributes = session.getAttributes();
		String uname = (String) attributes.get("username");
		
		
		HttpSession httpSession = (HttpSession) attributes.get("httpSession");
		String sessionID = httpSession.getId();
		
		Integer counter = (Integer) httpSession.getAttribute("counter");
		if(counter == null) {
			counter = 0;
		}
		
		counter++;
		
		httpSession.setAttribute("counter", counter);
		
		System.out.println(httpSession);
	}
}
