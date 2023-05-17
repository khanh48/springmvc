package me.forum.RestController;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

import me.forum.Component.JwtProvider;
import me.forum.Config.Base;
import me.forum.Dao.CommentDao;
import me.forum.Dao.GroupDao;
import me.forum.Dao.LikeDao;
import me.forum.Dao.NotificationDao;
import me.forum.Dao.PostDao;
import me.forum.Dao.RuleDao;
import me.forum.Dao.UserDao;
import me.forum.Entity.Comment;
import me.forum.Entity.Group;
import me.forum.Entity.Notification;
import me.forum.Entity.Post;
import me.forum.Entity.Rule;
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
	@Autowired
	public RuleDao ruleDao;
	@Autowired
	public GroupDao groupDao;

	public MainRestController() {
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, String> login(@RequestParam String userName, @RequestParam String password,
			HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		User user = userDao.findUserByUserName(userName);
		map.put("type", "failed");
		if (user != null && user.getMatkhau().equals(User.MD5(password))) {
			long curTime = System.currentTimeMillis();
			if(curTime - user.getLastlogin() <= 1000L) {
				map.put("message", "Thao tác quá nhiều.");
				return map;
			}
			map.put("type", "success");
			map.put("message", "success");
			user.setLastlogin(curTime);
			userDao.UpdateMaBaoMat(userName, JwtProvider.GetInstance().generate(userName), curTime);
			user = userDao.findUserByUserName(userName);
			session.setAttribute(Base.USER, user);
			user.setTructuyen(true);
			map.put("token", user.getMabaomat());
		}else {
			map.put("message", "Tài khoản hoặc mật khẩu không chính xác.");
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
	
	@RequestMapping(value = "/editGroup", method = RequestMethod.GET)
	public Map<String, String> editGroup(@RequestParam int id, @RequestParam String mota, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		
		User user = (User) session.getAttribute(Base.USER);
		if(user == null || user.getRank() < 1) {
			map.put("result", groupDao.getById(id).getMota());
			return map;
		}

		groupDao.UpdateDescription(id, mota);
		map.put("result", groupDao.getById(id).getMota());
		return map;
	}
	
	@RequestMapping(value = "/pinPost", method = RequestMethod.GET)
	public String pinPost(@RequestParam long id ,HttpSession session) {
		String result = "fail";
		User user = (User) session.getAttribute(Base.USER);
		if(user == null || user.getRank() < 1) {
			return result;
		}
		boolean status = postDao.GetPostByID(id).getGhim();
		if(postDao.PinStatus(id, !status) > 0) {
			result = status?"unpinned":"pinned";
		}
		return result;
	}

	@RequestMapping(value = "/deleteNofity", method = RequestMethod.POST)
	public String updateNofity(@RequestParam long id, HttpSession session) {
		String result = "fail";
		User user = (User) session.getAttribute(Base.USER);
		if(user == null || !notificationDao.GetById(id).getNguoinhan().equals(user.getTaikhoan()))
			return result;
		
		if(notificationDao.Remove(id) > 0) {
			result = "success";
		}
		
		return result;
	}
	
	@RequestMapping(value = "/changePass", method = RequestMethod.POST)
	public Map<String, String> changePass(HttpSession session, HttpServletRequest request) {
		String oldPass, newPass, confirmPass, pattern;
		HashMap<String, String> map = new HashMap<>();
		User user = (User)session.getAttribute(Base.USER);
		if(user == null) {
			map.put("type", "failed");
			map.put("message", "Tài khoản không tồn tại");
			return map;
		}
		oldPass = request.getParameter("oldPass");
		newPass = request.getParameter("newPass");
		confirmPass = request.getParameter("confirmPass");
		pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,15}$";
		
		if(!user.getMatkhau().equals(User.MD5(oldPass))) {
			map.put("type", "failed");
			map.put("message", "Mật khẩu không chính xác.");
		}else if(!Pattern.matches(pattern, newPass)) {
			map.put("type", "failed");
			map.put("message", "Mật khẩu mới không hợp lệ.");
		}else if(!newPass.equals(confirmPass)) {
			map.put("type", "failed");
			map.put("message", "Mật khẩu mới phải trùng nhau.");
		}else if(oldPass.equals(newPass)) {
			map.put("type", "failed");
			map.put("message", "Hãy thử một mật khẩu khác.");
		}else {
			map.put("type", "success");
			map.put("message", "Đổi mật khẩu thành công.");
			userDao.ChangePassword(user.getTaikhoan(), User.MD5(newPass));
			user.setMatkhau(User.MD5(newPass));
			session.setAttribute(Base.USER, user);
		}
		return map;
	}

	@RequestMapping(value = "/sendLike", method = RequestMethod.POST)
	public Map<String, Object> like(@RequestParam long id, @RequestParam boolean isPost,
			@RequestParam String to, HttpSession session) {

		Map<String, Object> map = new HashMap<>();
		User user = (User) session.getAttribute(Base.USER);
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
			if (!user.equals(toUser)) {
				Notification ntf = new Notification(curTime, user.getTaikhoan(), ct, toUser.getTaikhoan(), url);
				notificationDao.AddNotification(ntf);
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
		User user = (User) session.getAttribute(Base.USER);
		if(user == null || cmt == null) {
			map.put("type", "failed");
			map.put("message", "null");
		}else if(!user.equals(cmt.getUser()) && user.getRank() < 2) {
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
		User user = (User) session.getAttribute(Base.USER);
		if(user == null || post == null) {
			map.put("type", "failed");
			map.put("message", "null");
		}else if(!user.equals(post.getUser()) && user.getRank() < 2) {
			map.put("type", "failed");
			map.put("message", "no permission");
		}else {
			postDao.DeletePostByID(pid);
			map.put("type", "success");
			map.put("message", "" + pid);
		}
		return map;
	}
	

	@RequestMapping(value = "/findUser", method = RequestMethod.POST)
	public Map<String, String> findUser(HttpServletRequest request, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		String taikhoan, hoten, email, sdt, rank, result = "";
		int i = 0;
		taikhoan = request.getParameter("taikhoan");
		hoten = request.getParameter("hoten");
		email = request.getParameter("email");
		sdt = request.getParameter("sdt");
		rank = request.getParameter("chucvu");
		
		List<User> listUser = userDao.FindLikeUser(taikhoan, hoten, email, sdt, rank);
		User myUser = (User) session.getAttribute(Base.USER);
		if(myUser == null) return map;
		for (User user : listUser) {
			if(user.getRank() >= myUser.getRank()) continue;
			result += "<tr><td><input type='checkbox' name='checkbox' value='"+i+"' /></td>";
			result += "<td><input class='form-control f-sm' type='text' name='taikhoan' value='"+user.getTaikhoan() +"' readonly /></td>";
			result += "<td><input class='form-control f-sm' type='text' name='hoten' value='"+user.getHoten() +"' /></td>";
			result += "<td><input class='form-control f-sm' type='email' name='email' value='"+user.getEmail() + "' /></td>";
			result += "<td><input type='text' class='form-control f-sm mb-1' name='sdt' value='"+user.getSodienthoai() + "' /></td>";

			result += "<td><select class='form-control f-sm mb-1' name='chucvu'>";
			for (Rule r : ruleDao.getAll()) {
				String isSelected = user.getRank() == r.getHang()? "selected":"";
				if(r.getHang() < myUser.getRank()) {
					result += "<option value='"+r.getMachucvu() +"' "+isSelected+">"+r.getTenchucvu() +"</option>";				
				}
			}

			result += "</select></td></tr>";
			i++;
		}
		
		map.put("result", result);
		return map;
	}
	

	@RequestMapping(value = "/findPost", method = RequestMethod.POST)
	public Map<String, String> findPost(HttpServletRequest request, HttpSession session) {
		HashMap<String, String> map = new HashMap<>();
		String taikhoan, tieude, noidung, nhom, mabaiviet, result = "";
		int i = 0;
		taikhoan = request.getParameter("taikhoan");
		tieude = request.getParameter("tieude");
		noidung = request.getParameter("noidung");
		nhom = request.getParameter("nhom");
		mabaiviet = request.getParameter("mabaiviet");
		
		List<Post> listPost = postDao.FindLikePost(taikhoan, mabaiviet, tieude, noidung, nhom);
		User myUser = (User) session.getAttribute(Base.USER);
		if(myUser == null) return map;
		for (Post post : listPost) {
			
			result += "<tr><td><input type='checkbox' name='checkbox' value='"+i+"' /></td>";
			result += "<td><input class='form-control f-sm' type='text' name='taikhoan' value='"+post.getTaikhoan() +"' readonly /></td>";
			result += "<td><input class='form-control f-sm' type='text' name='mabaiviet' value='"+post.getMabaiviet() +"' /></td>";
			result += "<td><input class='form-control f-sm' type='text' name='tieude' value='"+post.getTieude() + "' /></td>";
			result += "<td><textarea type='text' class='form-control f-sm mb-1' name='noidung'>"+post.getNoidung() + "</textarea></td>";

			result += "<td><select class='form-control f-sm mb-1' name='nhom'>";
			for (Group g : groupDao.getGroupList()) {
				String isSelected = g.getManhom() == post.getManhom()? "selected":"";
				result += "<option value='"+g.getManhom() +"' "+isSelected+">"+g.getTennhom() +"</option>";
			}

			result += "</select></td></tr>";
			i++;
		}
		
		map.put("result", result);
		return map;
	}
	
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public Map<String, String> sendMessage(HttpServletRequest request, HttpSession session){
		HashMap<String, String> map = new HashMap<>();
		String message = request.getParameter("message");
		String toUsername = request.getParameter("toUser");
		User toUser, fromUser;
		toUser = userDao.findUserByUserName(toUsername);
		fromUser = (User) session.getAttribute(Base.USER);
		
		if(toUser == null || fromUser == null || message == null || message.isBlank()) {
			map.put("type", "failed");
			return map;
		}
		JSONObject json = new JSONObject();
		
		json.put("fromUser", fromUser.getTaikhoan());
		json.put("toUser", toUsername);
		json.put("message", message);
		
		map.put("type", "success");
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
