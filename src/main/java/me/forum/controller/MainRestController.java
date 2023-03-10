package me.forum.Controller;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.forum.Dao.CommentDao;
import me.forum.Dao.LikeDao;
import me.forum.Dao.NotificationDao;
import me.forum.Dao.PostDao;
import me.forum.Dao.UserDao;
import me.forum.Entity.Comment;
import me.forum.Entity.Notification;
import me.forum.Entity.Post;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

@RestController
public class MainRestController {

	private static final String ALGORITHM = "AES";
	
	@Autowired
	public UserDao userDao;
	@Autowired
	public NotificationDao notificationDao;
	@Autowired
	public LikeDao likeDao;
	@Autowired
	public CommentDao commentDao;
	@Autowired
	public PostDao postDao;

	public MainRestController() {
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, String> login(@RequestParam String userName, @RequestParam String password,
			HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		User user = userDao.findUserByUserName(userName);
		if (user != null && user.getMatkhau().equals(User.MD5(password))) {
			map.put("message", "success");
			long curTime = System.currentTimeMillis();
			user.setLastlogin(curTime);
			userDao.UpdateMaBaoMat(userName, MainRestController.encrypt(userName, curTime), curTime);
			user = userDao.findUserByUserName(userName);
			session.setAttribute("userID", user);
			session.setAttribute("listNotify", notificationDao.GetByNguoiNhan(user.getTaikhoan()));
			map.put("token", user.getMabaomat());
		} else {
			map.put("message", "failed");
		}

		return map;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, String> register(@RequestParam String userName, @RequestParam String pwd,
			@RequestParam String repwd, @RequestParam String fullName, HttpSession session) {

		String patternName, patternPass, type = "success", message = "T???o t??i kho???n th??nh c??ng.";
		HashMap<String, String> map = new HashMap<>();
		User user = userDao.findUserByUserName(userName);

		patternName = "^(?=.*[A-Za-z])[A-Za-z\\d]{6,13}$";
		patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$";

		if (fullName.length() < 5 || fullName.length() > 50) {
			type = "name_size";
			message = "H??? t??n ph???i t??? 5 ?????n 50 k?? t???.";
		} else if (!Pattern.matches(patternName, userName)) {
			type = "user_format";
			message = "T??n ????ng nh???p kh??ng h???p l???.";
		} else if (!Pattern.matches(patternPass, pwd)) {
			type = "pass_format";
			message = "M???t kh???u ph???i t??? 6 ?????n 15 k?? t???, bao g???m c??? ch??? v?? s???.";
		} else if (!pwd.equals(repwd)) {
			type = "repass_not_same";
			message = "M???t kh???u ph???i gi???ng nhau.";
		} else if (user != null) {
			type = "user_already_exists";
			message = "T??i kho???n ???? t???n t???i.";
		} else {
			userDao.AddUser(fullName, userName, pwd);
		}
		map.put("type", type);
		map.put("message", message);
		return map;
	}
	

	
	@RequestMapping(value = "/changePass", method = RequestMethod.POST)
	public Map<String, String> changePass(HttpSession session, HttpServletRequest request) {
		String oldPass, newPass, confirmPass, pattern;
		HashMap<String, String> map = new HashMap<>();
		User user = (User)session.getAttribute("userID");
		if(user == null) {
			map.put("type", "failed");
			map.put("message", "T??i kho???n kh??ng t???n t???i");
			return map;
		}
		oldPass = request.getParameter("oldPass");
		newPass = request.getParameter("newPass");
		confirmPass = request.getParameter("confirmPass");
		pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$";
		
		if(!user.getMatkhau().equals(User.MD5(oldPass))) {
			map.put("type", "failed");
			map.put("message", "M???t kh???u kh??ng ch??nh x??c.");
		}else if(!Pattern.matches(pattern, newPass)) {
			map.put("type", "failed");
			map.put("message", "M???t kh???u m???i kh??ng h???p l???.");
		}else if(!newPass.equals(confirmPass)) {
			map.put("type", "failed");
			map.put("message", "M???t kh???u m???i ph???i tr??ng nhau.");
		}else if(oldPass.equals(newPass)) {
			map.put("type", "failed");
			map.put("message", "H??y th??? m???t m???t kh???u kh??c.");
		}else {
			map.put("type", "success");
			map.put("message", "?????i m???t kh???u th??nh c??ng.");
			userDao.ChangePassword(user.getTaikhoan(), User.MD5(newPass));
			user.setMatkhau(User.MD5(newPass));
			session.setAttribute("userID", user);
		}
		return map;
	}

	@RequestMapping(value = "/sendLike", method = RequestMethod.POST)
	public Map<String, Object> like(@RequestParam long id, @RequestParam boolean isPost, @RequestParam String token,
			@RequestParam String to) {

		Map<String, Object> map = new HashMap<>();
		User user = userDao.findUserByCrypt(token);
		User toUser = userDao.findUserByUserName(to);
		boolean liked;
		int count = 0;
		long curTime = System.currentTimeMillis();
		long urlID = isPost?id: commentDao.GetByID(id).getMabaiviet();
		if (user == null || toUser == null) {
			liked = false;
		}
		else if (likeDao.IsLiked(id, user.getTaikhoan(), isPost) > 0) {
			likeDao.DeleteLike(isPost, user.getTaikhoan(), id);
			liked = false;
		} else {
			String ct, url;
			ct = user.getHoten() + " ???? th??ch " + (isPost ? "b??i vi???t" : "b??nh lu???n") + " c???a b???n.";
			url = "/bai-viet/" + urlID + "/"+curTime;
			likeDao.AddLike(isPost, user.getTaikhoan(), id);
			if (!user.getTaikhoan().equals(toUser.getTaikhoan())) {
				notificationDao.AddNotification(curTime, user.getTaikhoan(), ct, toUser.getTaikhoan(), url);
				Notification ntf = notificationDao.GetById(curTime);
				JSONObject json = new JSONObject();
				json.put("type", "newNotification");
				json.put("message", ct);
				json.put("url", url);
				json.put("date", ntf.getDateFormated());
				UserHandler.GetInstance().send(to, json.toString());
			}
			liked = true;
		}
		count = isPost? likeDao.GetTotalLikePost(id):likeDao.GetTotalLikeComment(id);
		map.put("status", liked);
		map.put("count", count);
		return map;
	}

	@RequestMapping(value = "/deleteCmt", method = RequestMethod.POST)
	public Map<String, String> deleteCmt(@RequestParam int cid, HttpSession session) {

		HashMap<String, String> map = new HashMap<>();
		Comment cmt = commentDao.GetByID(cid);
		User user = (User) session.getAttribute("userID");
		if(user == null || cmt == null) {
			map.put("type", "failed");
			map.put("message", "null");
		}else if(!user.equals(cmt.getUser()) && !user.getChucvu().equals("Admin")) {
			map.put("type", "failed");
			map.put("message", "no permission");
		}else {
			commentDao.DeleteByID(cid);
			map.put("type", "success");
			map.put("message", "" + cid);
		}
		return map;
	}

	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public Map<String, String> deletePost(@RequestParam long pid, HttpSession session) {

		HashMap<String, String> map = new HashMap<>();
		Post post = postDao.GetPostByID(pid);
		User user = (User) session.getAttribute("userID");
		if(user == null || post == null) {
			map.put("type", "failed");
			map.put("message", "null");
		}else if(!user.equals(post.getUser()) && !user.getChucvu().equals("Admin")) {
			map.put("type", "failed");
			map.put("message", "no permission");
		}else {
			postDao.DeletePostByID(pid);
			map.put("type", "success");
			map.put("message", "" + pid);
		}
		return map;
	}

	public static String encrypt(String input, long key) {
		try {
			byte[] bs = LongToBytes(key);
			SecretKeySpec keySpec = new SecretKeySpec(bs, ALGORITHM);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.ISO_8859_1));
			return new String(encryptedBytes, StandardCharsets.ISO_8859_1);

		} catch (Exception e) {
			System.out.println("MainRestController.encrypt: " + e.getMessage());
			return null;
		}
	}

	public static String decrypt(String input, long key) {
		try {
			byte[] bs = LongToBytes(key);
			SecretKeySpec keySpec = new SecretKeySpec(bs, ALGORITHM);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.ISO_8859_1));
			return new String(decryptedBytes, StandardCharsets.ISO_8859_1);

		} catch (Exception e) {
			System.out.println("MainRestController.decrypt: " + e.getMessage());
			return null;
		}
	}

	public static byte[] LongToBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(value);
		byte[] bs = new byte[16];
		System.arraycopy(buffer.array(), 0, bs, 0, buffer.array().length);
		return bs;
	}
}
