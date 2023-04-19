package me.forum.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Dao.CommentDao;
import me.forum.Dao.GroupDao;
import me.forum.Dao.ImageDao;
import me.forum.Dao.LikeDao;
import me.forum.Dao.NotificationDao;
import me.forum.Dao.PostDao;
import me.forum.Dao.RuleDao;
import me.forum.Dao.UserDao;

@Controller
public class BaseController {
	@Autowired
	public UserDao userDao;
	@Autowired
	public PostDao postDao;
	@Autowired
	public LikeDao likeDao;
	@Autowired
	public CommentDao commentDao;
	@Autowired
	public NotificationDao notificationDao;
	@Autowired
	public ImageDao imageDao;
	@Autowired
	public GroupDao groupDao;
	@Autowired
	public RuleDao ruleDao;

	public ModelAndView mav = new ModelAndView();
	
	private static BaseController instance;
	
	public BaseController() {
		instance = this;
		
	}
	public static BaseController GetInstance() {
		return instance;
	}
	
	
}
