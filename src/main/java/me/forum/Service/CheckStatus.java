package me.forum.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import me.forum.Dao.UserDao;

@Service
@EnableScheduling
public class CheckStatus {
	@Autowired
	UserDao userDao;

	//@Scheduled(fixedRate = 1000)
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
