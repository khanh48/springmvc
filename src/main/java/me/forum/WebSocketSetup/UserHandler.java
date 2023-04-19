package me.forum.WebSocketSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import me.forum.Module.ChatBot;

public class UserHandler {

	private HashMap<String, WebSocketSession> clients = new HashMap<>();
	public static HashMap<String, ChatBot> bots = new HashMap<>();
	private List<WebSocketSession> allUser = new ArrayList<>();
	private static UserHandler instance;

	public void addClient(String name, WebSocketSession session) {
		clients.put(name, session);
	}

	public void addClient(WebSocketSession session) {
		allUser.add(session);
	}

	public void removeClient(String name) {
		clients.remove(name);
	}

	public void removeClient(WebSocketSession session) {
		allUser.remove(session);
	}

	public void send(String name, String message) {
		TextMessage textMessage = new TextMessage(message);
		try {
			if(containsClient(name))
				clients.get(name).sendMessage(textMessage);;
		} catch (Exception e) {
			clients.remove(name);
		}
	}

	public boolean containsClient(String name) {
		return clients.containsKey(name);
	}
	
	public HashMap<String, WebSocketSession> GetAllUsers() {
		return clients;
	}
	
	public void send(String message) {
		TextMessage textMessage = new TextMessage(message);
		for (WebSocketSession session : allUser) {
			try {
				session.sendMessage(textMessage);
			} catch (Exception e) {
				allUser.remove(session);
			}
		}
	}

	public static UserHandler GetInstance() {
		if (instance == null)
			instance = new UserHandler();
		return instance;
	}
}
