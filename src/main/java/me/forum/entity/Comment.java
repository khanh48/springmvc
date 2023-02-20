package me.forum.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import me.forum.controller.BaseController;

public class Comment {
	private int mabinhluan;
	private String noidung;
	private String taikhoan;
	private long mabaiviet;
	private Date ngaytao;
	
	public Comment(int mabinhluan, String noidung, String taikhoan, long mabaiviet, String ngaytao) {

		this.mabinhluan = mabinhluan;
		this.noidung = noidung;
		this.taikhoan = taikhoan;
		this.mabaiviet = mabaiviet;
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.ngaytao = pa.parse(ngaytao);
		} catch (ParseException e) {
			this.ngaytao = null;
		}
	}
	public boolean IsLiked(String taikoan) {
		int count = 0;
			count = BaseController.GetInstance().likeDao.IsLiked(this.mabinhluan, taikoan, false);
		return count > 0 ? true : false;
	}
	public int getMabinhluan() {
		return mabinhluan;
	}
	public void setMabinhluan(int mabinhluan) {
		this.mabinhluan = mabinhluan;
	}
	public String getNoidung() {
		return noidung;
	}
	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}

	public int getCountLike() {
		return 0;
	}

	public User getUser() {
		return BaseController.GetInstance().userDao.findUserByUserName(taikhoan);
	}
	
	public String getTaikhoan() {
		return taikhoan;
	}
	public void setTaikhoan(String taikhoan) {
		this.taikhoan = taikhoan;
	}
	public long getMabaiviet() {
		return mabaiviet;
	}
	public void setMabaiviet(long mabaiviet) {
		this.mabaiviet = mabaiviet;
	}
	public Date getNgaytao() {
		return ngaytao;
	}
	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
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
	
}
