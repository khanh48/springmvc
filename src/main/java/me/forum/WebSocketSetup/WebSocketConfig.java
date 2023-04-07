package me.forum.WebSocketSetup;

import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

import me.forum.Controller.BaseController;
import me.forum.Dao.UserDao;
import me.forum.Entity.User;
import me.forum.Module.ChatBot;

@ServerEndpoint("/websocket")
public class WebSocketConfig {
	private UserHandler handler = UserHandler.GetInstance();
	private HashMap<String, ChatBot> bots = UserHandler.bots;
	private UserDao userDao = BaseController.GetInstance().userDao;

	@OnOpen
	public void onOpen(Session session) {
		handler.addClient(session);
	}

	@OnMessage
	public void handleMessage(String msg, Session session) {
		JSONObject json = new JSONObject(msg);
		User user;
		switch (json.getString("type")) {
		
		case "sendMessage":
			if (handler.containsClient(json.getString("name"))) {
				handler.send(json.getString("name"), "hihi");
			}
			break;
		case "requestChat":
			if(json.isNull("token")) break;
			user = userDao.findUserByCrypt(json.getString("token"));
			if (user == null)
				break;
			if(!bots.containsKey(user.getTaikhoan()) || bots.get(user.getTaikhoan()).isStoped()) {
				bots.put(user.getTaikhoan(), new ChatBot(user));
			}
			bots.get(user.getTaikhoan()).request(json.get("message").toString());
			break;
		default:
			if(json.isNull("token")) break;
			user = userDao.findUserByCrypt(json.getString("token"));
			if (user == null)
				break;
			handler.addClient(user.getTaikhoan(), session);
		}
	}

	@OnClose
	public void onClose(Session session) {
		handler.removeClient(session);
	}

	@OnError
	public void onError(Throwable throwable) {
		System.out.println(throwable.getMessage());
	}

}
