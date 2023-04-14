package me.forum.Service;

import java.util.Map.Entry;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import me.forum.Dao.UserDao;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

@Service
@EnableScheduling
public class CheckStatus {
	@Autowired
	UserDao userDao;

	@Scheduled(fixedRate = 2000)
	public void checkOffline() {
		for (Entry<String, Session> session : UserHandler.GetInstance().GetAllUsers().entrySet()) {
				User user = userDao.findUserByUserName(session.getKey());
				if(!session.getValue().isOpen()) {
					user.setTructuyen(false);
					UserHandler.GetInstance().GetAllUsers().remove(user.getTaikhoan());
				}else{
					user.setTructuyen(true);
				}
			
		}
	}
}
