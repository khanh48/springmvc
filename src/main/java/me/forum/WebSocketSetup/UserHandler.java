package me.forum.WebSocketSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import me.forum.Module.ChatBot;

public class UserHandler {

	private HashMap<String, WebSocketSession> clients = new HashMap<>();
	private HashMap<Integer, WebSocketSession> chats = new HashMap<>();
	public static HashMap<String, ChatBot> bots = new HashMap<>();
	private List<WebSocketSession> allUser = new ArrayList<>();
	private static UserHandler instance;

	public void addClient(String name, WebSocketSession session) {
		clients.put(name, session);
	}

	public void addClient(WebSocketSession session) {
		allUser.add(session);
	}
	
	public void addChat(String sender, String receiver, WebSocketSession session) {
		int id = merge(sender, receiver);
		chats.put(id, session);
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
			if (containsClient(name))
				clients.get(name).sendMessage(textMessage);
		} catch (Exception e) {
			System.out.println("Socket send: " + e.getMessage());
			clients.remove(name);
		}
	}

	public void sendChat(String sender, String receiver, String message) {
		int id = merge(sender, receiver);
		TextMessage textMessage = new TextMessage(message);
		try {
			if (chats.containsKey(id))
				chats.get(id).sendMessage(textMessage);
		} catch (Exception e) {
			System.out.println("Socket send: " + e.getMessage());
			chats.remove(id);
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

	private Integer merge(String sender, String receiver) {
		return (receiver + "-" + sender).hashCode();
	}
	
	public static UserHandler GetInstance() {
		if (instance == null)
			instance = new UserHandler();
		return instance;
	}
}
