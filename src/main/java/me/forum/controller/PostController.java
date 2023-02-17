package me.forum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.forum.entity.Post;
import me.forum.entity.User;

@Controller
public class PostController extends BaseController{
	
	public PostController() {
	}

	@RequestMapping(value = "/loadPost", method = RequestMethod.GET)
	@ResponseBody
	public List<String> loadOnScroll(@RequestParam int start, @RequestParam int limit){
		List<String> list = new ArrayList<>();
		List<Post> posts = postDao.GetPostsLimit(start, limit);
		for(int i = posts.size() - 1; i >= 0; i--) {
			Post p = posts.get(i);
			User u = p.getUser();
			
			String item = "<div class='content rm'>"
					+ "<div class='d-flex justify-content-between $loggedin'>"
					+ "<div class=' c-header'>"
					+ "<span> <a class='name' href='#'> <img class='avt' src='"+u.getAnhdaidien()+"' alt='avatar'></a></span>"
					+ "<div class='c-name'>"
					+ "<span><a class='name' href='#'>"+u.getHoten()+"</a>"
					+ "<div class='time'><small class='text-secondary'>"+p.getDateFormated()+"</small></div> </span></div></div>"
					+ "<button name='delete-notification' class='btn-close py-1 px-3' "
					+ "value='a' data-bs-toggle='modal' data-bs-target='#delete-post' onclick=\"deletePost("+p.getMabaiviet()+")\"></button>"
					+ "</div><div> <div class='title'>"
					+ "<div class='name'>"+p.getNhom()+"</div>"
					+ "<span>></span><div class='name'>"+p.getTieude()+"</div></div></div>"
					+ "<div class='c-body'>"+p.getNoidung()+"</div>"
					+ "<div class='m-0 hide wh' style='text-align: end;'>"
					+ "<span class='read-more'></span></div>"
					+ "<hr class='m-0'>"
					+ "<div class='interactive p-1 m-0'>"
					+ "<button type='button' class='like p-1' onclick=\"like('mabaiviet',true,'my_id', 'taikhoan')\">"
					+ "<i class='fas fa-heart action $is_liked' id='plmabaiviet'></i> "
					+ "<span class='count-like' id='pmabaiviet'>"+p.getCountLike()+"</span></button>"
					+ "<button type='button' class='comment p-1' onclick=\" window.location.href='./post.php?id='\">"
					+ "<i class='fas fa-comment action'></i> <span class='count-comment'>"+p.getCountComment()+"</span>"
					+ "</button><button type='button' class='share p-1'>"
					+ "<i class='fas fa-share action'></i><span class='count-share'>2</span>"
					+ "</button></div></div>";
			list.add(item);
		}
		return list;
	}
}
