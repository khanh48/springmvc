package me.forum.controller;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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
import me.forum.Dao.UserDao;
import me.forum.WebSocketSetup.UserHandler;
import me.forum.entity.Notification;
import me.forum.entity.User;

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

		String patternName, patternPass, type = "success", message = "Tạo tài khoản thành công.";
		HashMap<String, String> map = new HashMap<>();
		User user = userDao.findUserByUserName(userName);

		patternName = "^(?=.*[A-Za-z])[A-Za-z\\d]{6,13}$";
		patternPass = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$";

		if (fullName.length() < 5 || fullName.length() > 50) {
			type = "name_size";
			message = "Họ tên phải từ 5 đến 50 ký tự.";
		} else if (!Pattern.matches(patternName, userName)) {
			type = "user_format";
			message = "Tên đăng nhập không hợp lệ.";
		} else if (!Pattern.matches(patternPass, pwd)) {
			type = "pass_format";
			message = "Mật khẩu phải từ 6 đến 15 ký tự, bao gồm cả chữ và số.";
		} else if (!pwd.equals(repwd)) {
			type = "repass_not_same";
			message = "Mật khẩu phải giống nhau.";
		} else if (user != null) {
			type = "user_already_exists";
			message = "Tài khoản đã tồn tại.";
		} else {
			userDao.AddUser(fullName, userName, pwd);
		}
		map.put("type", type);
		map.put("message", message);
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
			ct = user.getHoten() + " đã thích " + (isPost ? "bài viết" : "bình luận") + " của bạn.";
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
