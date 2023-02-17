package me.forum.WebSocketSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.websocket.Session;

public class UserHandler {

	private HashMap<String, Session> clients = new HashMap<>();
	private List<Session> allUser = new ArrayList<>();
	private static UserHandler instance;

	public void addClient(String name, Session session) {
		clients.put(name, session);
	}

	public void addClient(Session session) {
		allUser.add(session);
	}

	public void removeClient(String name) {
		clients.remove(name);
	}

	public void removeClient(Session session) {
		allUser.remove(session);
	}

	public void send(String name, String message) {
		try {
			if(containsClient(name))
				clients.get(name).getBasicRemote().sendText(message);
		} catch (IOException e) {
			clients.remove(name);
		}
	}

	public boolean containsClient(String name) {
		return clients.containsKey(name);
	}
	
	public void send(String message) {
		for (Session session : allUser) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
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
