package me.forum.Entity;

import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

public class Notification {
	private long mathongbao;
	private boolean trangthai;
	private String nguoigui;
	private String noidung;
	private String nguoinhan;
	private String url;
	private Date ngaytao;

	public Notification(long mathongbao, boolean trangthai, String nguoigui, String noidung, String nguoinhan,
			String url, String ngaytao) {

		this.mathongbao = mathongbao;
		this.trangthai = trangthai;
		this.nguoigui = nguoigui;
		this.noidung = noidung;
		this.nguoinhan = nguoinhan;
		this.url = url;
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.ngaytao = pa.parse(ngaytao);
		} catch (ParseException e) {
			this.ngaytao = null;
		}
		 
	}

	public long getMathongbao() {
		return mathongbao;
	}

	public void setMathongbao(long mathongbao) {
		this.mathongbao = mathongbao;
	}

	public boolean isTrangthai() {
		return trangthai;
	}

	public void setTrangthai(boolean trangthai) {
		this.trangthai = trangthai;
	}

	public String getNguoigui() {
		return nguoigui;
	}

	public void setNguoigui(String nguoigui) {
		this.nguoigui = nguoigui;
	}

	public String getNoidung() {
		return noidung;
	}

	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}

	public String getNguoinhan() {
		return nguoinhan;
	}

	public void setNguoinhan(String nguoinhan) {
		this.nguoinhan = nguoinhan;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		if(days >= 365) result = Period.between(old.toLocalDate(), now.toLocalDate()).getYears() + " năm trước";
		else if(days >= 30) result = Period.between(old.toLocalDate(), now.toLocalDate()).getMonths() + " tháng trước";
		else if(days >= 1) result = days + " ngày trước";
		else if(hours >= 1) result = hours + " giờ trước";
		else if(minutes >= 1) result = minutes + " phút trước";
		else if(seconds >= 1) result = seconds + " giây trước";
		else result = "Ngay bây giờ";
		return result;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

}
