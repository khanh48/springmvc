package me.forum.Service;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import me.forum.Dao.UserDao;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

@Service
@EnableScheduling
public class CheckStatus {
	@Autowired
	UserDao userDao;

	@Scheduled(fixedRate = 1000)
	public void checkOffline() {
		/*for (Entry<String, WebSocketSession> session : UserHandler.GetInstance().GetAllUsers().entrySet()) {
			
			User user = userDao.findUserByUserName(session.getKey());
			if (session.getValue().isOpen()) {
				user.setTructuyen(true);
				//userDao.SetLastLogin(user.getTaikhoan());
			} else {
				user.setTructuyen(false);
			}

		}*/
	}
}
