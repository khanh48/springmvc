package me.forum.Service;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

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
		Set<Entry<String, WebSocketSession>> allUser = UserHandler.GetInstance().GetAllUsers().entrySet();
		Iterator<Entry<String, WebSocketSession>> iterators = allUser.iterator();
		while (iterators.hasNext()) {
			
			Entry<String, WebSocketSession> session = iterators.next();
			User user = userDao.findUserByUserName(session.getKey());
			if (session.getValue().isOpen()) {
				user.setTructuyen(true);
			} else {
				user.setTructuyen(false);
				userDao.SetLastLogin(user.getTaikhoan());
				iterators.remove();
			}

		}
	}
}
