package me.forum.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;

import me.forum.controller.BaseController;

public class Post {
	private long mabaiviet;
	private String tieude;
	private String noidung;
	private String taikhoan;
	private String nhom;
	private int countLike, countComment;
	private Date ngaytao;
	private User user;

	public Post(long mabaiviet, String tieude, String noidung, String taikhoan, String nhom, String ngaytao) {
		this.mabaiviet = mabaiviet;
		this.tieude = tieude;
		this.noidung = noidung;
		this.taikhoan = taikhoan;
		this.nhom = nhom;
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.ngaytao = pa.parse(ngaytao);
		} catch (ParseException e) {
			this.ngaytao = null;
		}
		countLike = 0;
		countComment = 0;
		this.user = null;
	}

	public boolean IsLiked(String taikoan, boolean isPost) {
		int count = 0;
		try {
			count = BaseController.GetInstance().likeDao.IsLiked(this.mabaiviet, taikoan, isPost);
		} catch (Exception e) {
		}
		return count > 0 ? true : false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getCountLike() {
		return countLike;
	}

	public void setCountLike(int countLike) {
		this.countLike = countLike;
	}

	public int getCountComment() {
		return countComment;
	}

	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}

	public long getMabaiviet() {
		return mabaiviet;
	}

	public void setMabaiviet(long mabaiviet) {
		this.mabaiviet = mabaiviet;
	}

	public String getTieude() {
		return tieude;
	}

	public void setTieude(String tieude) {
		this.tieude = tieude;
	}

	public String getNoidung() {
		return noidung;
	}

	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}

	public String getTaikhoan() {
		return taikhoan;
	}

	public void setTaikhoan(String taikhoan) {
		this.taikhoan = taikhoan;
	}

	public String getNhom() {
		return nhom;
	}

	public void setNhom(String nhom) {
		this.nhom = nhom;
	}

	public Date getNgaytao() {
		return ngaytao;
	}

	public String getDateFormated() {
		long days, hours, minutes, seconds;
		String result = "";
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime old = LocalDateTime.ofInstant(ngaytao.toInstant(), ZoneId.systemDefault());
		Duration duration = Duration.between(old, now);
		days = duration.toDays();
		hours = duration.toHours();
		minutes = duration.toMinutes();
		seconds = duration.getSeconds();
		if (days >= 365)
			result = Period.between(old.toLocalDate(), now.toLocalDate()).getYears() + " năm trước";
		else if (days >= 30)
			result = Period.between(old.toLocalDate(), now.toLocalDate()).getMonths() + " tháng trước";
		else if (days >= 1)
			result = days + " ngày trước";
		else if (hours >= 1)
			result = hours + " giờ trước";
		else if (minutes >= 1)
			result = minutes + " phút trước";
		else if (seconds >= 1)
			result = seconds + " giây trước";
		else
			result = "Ngay bây giờ";
		return result;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

}