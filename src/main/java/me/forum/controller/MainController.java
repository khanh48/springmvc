package me.forum.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import me.forum.Component.JwtProvider;
import me.forum.Config.Base;
import me.forum.Entity.Comment;
import me.forum.Entity.Group;
import me.forum.Entity.HTML;
import me.forum.Entity.Post;
import me.forum.Entity.User;

@Controller
public class MainController extends BaseController {

	public MainController() {
	}

	@RequestMapping(value = { "/", "/index" })
	public ModelAndView homePage(HttpServletRequest request) {
		mav.setViewName("index");
		mav.addObject("posts", postDao.GetPostsLimitDesc(0, 10));
		mav.addObject("groups", groupDao.getGroupList());
		return mav;
	}

	@RequestMapping(value = { "/chat/{username}"})
	public ModelAndView chatPageUser(HttpSession session, @PathVariable(name = "username", required = false) String friend) {
		mav.setViewName("chat");
		User user = (User) session.getAttribute(Base.USER);
		User chatUser = userDao.findUserByUserName(friend);
		if(user == null || chatUser == null) {
			return mav;	
		}
		messageDao.makeAsRead(friend, user.getTaikhoan());
		mav.addObject("chatUser", chatUser);
		return mav;
	}

	@RequestMapping(value = { "/ho-so/{username}", "/profile/{username}" })
	public ModelAndView profilePage(@PathVariable("username") String username, HttpSession session) {

		mav.setViewName("profile");
		User user = userDao.findUserByUserName(username);
		if (user == null) {
			mav.addObject("profile", null);
			mav.addObject("postOfUser", null);
			return mav;

		}
		List<Post> post = postDao.GetPostsByUser(username);

		mav.addObject("profile", user);
		mav.addObject("postOfUser", post);
		return mav;
	}

	@RequestMapping(value = { "/ho-so", "/profile" })
	public ModelAndView myProfilePage(HttpSession session) {

		mav.setViewName("profile");
		User user = (User) session.getAttribute(Base.USER);
		if (user == null) {
			mav.addObject("profile", null);
			mav.addObject("postOfUser", null);
			return mav;

		}
		List<Post> post = postDao.GetPostsByUser(user.getTaikhoan());

		mav.addObject("profile", user);
		mav.addObject("postOfUser", post);
		return mav;
	}

	@RequestMapping(value = { "/thong-bao", "/notification" })
	public ModelAndView notificationsPage(@RequestParam(name = "makeAsRead", required = false) Object ids,
			@RequestParam(name = "deleteNotifications", required = false) Object dn, HttpSession session) {

		mav.setViewName("notification");
		User user = (User) session.getAttribute(Base.USER);
		boolean flag = false;

		if(user == null) return mav;
		
		if(ids != null) {
			flag = true;
			notificationDao.MakeAsReadAll(user.getTaikhoan());
		}
		if(dn != null) {
			flag = true;
			notificationDao.RemoveReaded(user.getTaikhoan());
		}
		if(flag) {
			mav.setViewName("redirect:/thong-bao");
		}
		return mav;
	}

	@RequestMapping(value = { "/bai-viet/{id}", "/post/{id}" })
	public ModelAndView postPage(@PathVariable long id, HttpSession session) {

		Post post = postDao.GetPostByID(id);
		mav.setViewName("post");
		if (post == null) {
			mav.addObject("post", null);
			mav.addObject("comments", null);
			return mav;
		}
		List<Comment> comments = commentDao.GetPostsLimitDesc(id, 0, 10);
		post.setNoidung(post.getNoidung().replaceAll("<", "&lt;").replaceAll("\n", "<br>"));
		mav.addObject("post", post);
		mav.addObject("comments", comments);
		return mav;	
	}

	@RequestMapping(value = { "/bai-viet/{id}/{nid}", "/post/{id}/{nid}" })
	public ModelAndView postPageReaded(@PathVariable long id, @PathVariable long nid, HttpSession session) {

		notificationDao.MakeAsRead(nid);
		mav.setViewName("redirect:/bai-viet/" + id);
		return mav;
	}
	
	@RequestMapping(value = { "/nhom/{id}", "/group/{id}" })
	public ModelAndView groupIDPage(@PathVariable int id, HttpSession session) {
		mav.addObject("entities", null);
		mav.addObject("group", groupDao.getById(id));
		mav.setViewName("group");
		return mav;
	}

	@RequestMapping(value = { "/nhom", "/group" })
	public ModelAndView groupPage(HttpSession session) {

		mav.setViewName("group");
		HashMap<String, Object> map = new HashMap<>();
		List<Group> groups = groupDao.getGroupList();
		map.put("group", groups);
		int[] countPost = new int[groups.size()];
		Post[] hotPost, exaltedPost;
		hotPost = new Post[groups.size()];
		exaltedPost = new Post[groups.size()];
		
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			exaltedPost[i] = postDao.getExaltedPost(group.getManhom());
			hotPost[i] = postDao.getHotPost(group.getManhom());
			countPost[i] = groupDao.getCountPost(group.getManhom());
		}
		
		map.put("hot", hotPost);
		map.put("exalted", exaltedPost);
		map.put("count", countPost);
		mav.addObject("entities", map);
		return mav;
	}

	@RequestMapping(value = { "/tim-kiem" }, method = RequestMethod.GET)
	public ModelAndView searchPage(@RequestParam(name = "type", required = false) Integer type, 
			@RequestParam(name = "value", required = false) String value, HttpSession session) {
		mav.setViewName("search");
		mav.addObject("search_users", null);
		mav.addObject("search_posts", null);
		User user = (User) session.getAttribute(Base.USER);
		if(type != null && value != null) {
			if(type == 0) {
				List<User> users = userDao.SearchUser(value, 50);
				mav.addObject("search_users", users);
			}else {
				List<Post> posts = postDao.Search(value, 50);
				List<String> result = new ArrayList<>();
				for (Post post : posts) {
					result.add(HTML.GetPost(user, post));
				}
				mav.addObject("search_posts", result);
			}
		}
		return mav;
	}
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession session) {
		mav.setViewName("redirect:/");
		mav.addObject("search_posts", null);
		if (session.getAttribute(Base.USER) != null) {
			String uname = ((User) session.getAttribute(Base.USER)).getTaikhoan();
			long curTime = System.currentTimeMillis();
			userDao.UpdateMaBaoMat(uname, JwtProvider.GetInstance().generate(uname), curTime);
			session.removeAttribute(Base.USER);
			session.removeAttribute("listNotify");
		}
		return mav;
	}
	@RequestMapping(value = "/errors")
	public ModelAndView errorPage(HttpServletRequest request) {
		mav.setViewName("errors");
		String result = "";
		switch ((int)request.getAttribute("javax.servlet.error.status_code")) {
		case 400:
			result = "400 Bad Request";
			break;
		case 401:
			result = "401 Unauthorized";
			break;
		case 403:
			result = "403 Forbidden: Người dùng không được phép truy cập vào tài nguyên này";
			break;
		case 404:
			result = "404 - Trang không tồn tại";
			break;
		case 500:
			result = "500 Internal Server Error";
			break;
		default:
			break;
		}
		mav.addObject("errorMessage", result);
		return mav;
	}

}
