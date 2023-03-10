package me.forum.WebSocketSetup;

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

@ServerEndpoint("/websocket")
public class WebSocketConfig {
	private UserHandler handler = UserHandler.GetInstance();
	private UserDao userDao = BaseController.GetInstance().userDao;

	@OnOpen
	public void onOpen(Session session) {
		handler.addClient(session);
	}

	@OnMessage
	public void handleMessage(String msg, Session session) {
		JSONObject json = new JSONObject(msg);
		switch (json.getString("type")) {
		case "sendMessage":
			if (handler.containsClient(json.getString("name"))) {
				handler.send(json.getString("name"), "hihi");
			}
			break;
		default:
			if(json.isNull("token")) break;
			User user = userDao.findUserByCrypt(json.getString("token"));
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
