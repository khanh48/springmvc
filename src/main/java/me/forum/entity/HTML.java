package me.forum.Entity;

import java.util.List;

public class HTML {
	

	public static String GetPost(User user, Post p) {
		return GetPost(user, p, false);
	}
	
	public static String GetPost(User user, Post p, boolean isInGroup) {
		String numlike, numcmt, uUrl, isliked;
		p.setTieude(p.getTieude().replaceAll("<", "&lt;"));
		p.setNoidung(p.getNoidung().replaceAll("<", "&lt;").replaceAll("\n", "<br>"));
		isliked = "";
		User u = p.getUser();
		uUrl = "/ho-so/" + u.getTaikhoan();
		numlike = p.getCountLike() > 0 ? p.getCountLike() + "" : "";
		numcmt = p.getCountComment() > 0 ? p.getCountComment() + "" : "";
		if (user != null && p.IsLiked(user.getTaikhoan())) {
			isliked = "fas-liked";
		}

		String item = "<div class='content' id='" + p.getMabaiviet() + "'>";
		item += "<div class='d-flex justify-content-between'>";
		item += "<div class=' c-header'>";
		item += "<span> <a class='name' href='" + uUrl + "'>";
		item += "<img class='avt' src='" + u.getAnhdaidien() + "' alt='avatar'>";
		item += "</a></span><div class='c-name'>";
		item += "<span><a class='name' href='" + uUrl + "'>" + u.getHoten() + "</a>";
		item += "<div class='time'>";
		item += "<small class='text-secondary'>" + p.getDateFormated() + "</small>";
		item += "</div></span></div></div><div>";
		
		if(p.getGhim()) {
			item += "<button class='btn-pinned btn-hover me-2'></button>";
		}
		
		if(isInGroup && user != null && user.getRank() >= 1) {
			if(p.getGhim()) {
				item += "<button class='btn-unpin btn-hover me-2' onclick='pinPost(this, "+p.getMabaiviet()+");'></button>";
			}else {
				item += "<button class='btn-pin btn-hover me-2' onclick='pinPost(this, "+p.getMabaiviet()+");' ></button>";
			}
		}
		
		if (user != null && (u.getTaikhoan().equals(user.getTaikhoan()) || user.getRank() >= 2)) {
			item += "<button name='delete-notification' class='btn-close me-2' ";
			item += "value='' data-bs-toggle='modal' data-bs-target='#delete-post' " + "onclick=\"deletePost("
					+ p.getMabaiviet() + ")\"></button>";
		}

		item += "</div></div><div> <div class='title'>";
		if(!isInGroup) {
			item += "<div class='name'>" + p.getNhom() + "</div>";
			item += "<span>></span>";
		}
		item += "<div class='name'>" + p.getTieude() + "</div></div></div>";
		item += "<div class='c-body'>" + p.getNoidung() + "</div>";
		item += "<div class='m-0 hide wh' style='text-align: end;'>";
		item += "<span class='read-more'></span></div>";

		List<Image> imgs = p.getImage();
		if (imgs != null && !imgs.isEmpty()) {
			item += "<div id='forpost" + p.getMabaiviet() + "' class='carousel slide mt-1' data-bs-ride='carousel'>";
			item += "<div class='carousel-inner'>";
			int idx = 0;
			for (Image img : imgs) {
				item += "<div class='carousel-item" + (idx == 0 ? " active" : "") + "'>";
				item += "<img src='" + img.getUrl() + "' class='d-block w-100 postImg' alt='...'></div>";
				idx++;
			}

			item += "</div><button class='carousel-control-prev' type='button' ";
			item += "data-bs-target='#forpost" + p.getMabaiviet() + "' data-bs-slide='prev'>";
			item += "<span class='carousel-control-prev-icon' aria-hidden='true'></span>";
			item += "<span class='visually-hidden'>Previous</span></button>";
			item += "<button class='carousel-control-next' type='button' ";
			item += "data-bs-target='#forpost" + p.getMabaiviet() + "' data-bs-slide='next'>";
			item += "<span class='carousel-control-next-icon' aria-hidden='true'></span>";
			item += "<span class='visually-hidden'>Next</span></button></div>";
		}

		item += "<hr class='m-0'>";
		item += "<div class='interactive p-1 m-0'>";
		item += "<button type='button' class='like p-1' onclick=\"like(" + p.getMabaiviet() + ",true, '"
				+ u.getTaikhoan() + "')\">";
		item += "<i class='fas fa-heart action " + isliked + "' id='pl" + p.getMabaiviet() + "'></i> ";
		item += "<span class='count-like' id='p" + p.getMabaiviet() + "'>" + numlike + "</span></button>";
		item += "<button type='button' class='comment-btn p-1' onclick=\"window.location.href='/bai-viet/"
				+ p.getMabaiviet() + "'\">";
		item += "<i class='fas fa-comment action'></i>";
		item += "<span class='count-comment'>" + numcmt + "</span>";
		item += "</button><button type='button' class='share p-1'>";
		item += "<i class='fas fa-share action'></i><span class='count-share'></span>";
		item += "</button></div></div>";
		return item;
	}
	
	public static String GetComment(User user, Comment cmt) {
		String numlike, uUrl, isliked;
		cmt.setNoidung(cmt.getNoidung().replaceAll("<", "&lt;").replaceAll("\n", "<br>"));
		User u = cmt.getUser();
		isliked = "";
		uUrl = "/ho-so/" + u.getTaikhoan();
		numlike = cmt.getCountLike() > 0 ? cmt.getCountLike() + "" : "";
		if (user != null && cmt.IsLiked(user.getTaikhoan())) {
			isliked = "fas-liked";
		}

		String item = "<div class='content' id='" + cmt.getMabinhluan() + "'>";
		item += "<div class='d-flex justify-content-between'>";
		item += "<div class='c-header'><span>";
		item += "<a class='name' href='" + uUrl + "'>";
		item += "<img class='avt' src='" + u.getAnhdaidien() + "' alt='avatar'>";
		item += "</a></span><div class='c-name'>";
		item += "<span><a class='name' href='" + uUrl + "'>" + u.getHoten() + "</a>";
		item += "<div class='time'>";
		item += "<small class='text-secondary'>" + cmt.getDateFormated() + "</small>";
		item += "</div> </span></div></div>";

		if (user != null && (u.getTaikhoan().equals(user.getTaikhoan()) || user.getRank() >= 2)) {
			item += "<button name='delete-notification' class='btn-close py-1 px-3' "
					+ "value='a' data-bs-toggle='modal' data-bs-target='#delete-cmt' onclick=\"deleteCmt("
					+ cmt.getMabinhluan() + ")\"></button>";
		}
		item += "</div><div class='c-body'>" + cmt.getNoidung() + "</div>";
		item += "<div class='m-0 hide wh' style='text-align: end;'>";
		item += "<span class='read-more'></span></div><hr class='m-0'>";
		item += "<div class='interactive p-1 m-0'>"; 
		item += "<button type='button' class='like p-1' ";
		item += "onclick=\"like(" + cmt.getMabinhluan() + ",false, '" + u.getTaikhoan() + "')\">";
		item += "<i class='fas fa-heart action " + isliked + "' id='cl" + cmt.getMabinhluan() + "'>";
		item += "</i><span class='count-like' id='c" + cmt.getMabinhluan() + "'>";
		item += numlike + "</span></button>" + "</div></div>";
		return item;
	}
}
