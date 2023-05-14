package me.forum.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Message {
	int matinnhan;
	User nguoigui;
	User nguoinhan;
	String noidung;
	boolean trangthai;
	Date ngaytao;

	public Message(int matinnhan, User nguoigui, User nguoinhan, String noidung, boolean trangthai, String ngaytao) {
		this.matinnhan = matinnhan;
		this.nguoigui = nguoigui;
		this.nguoinhan = nguoinhan;
		this.noidung = noidung;
		this.trangthai = trangthai;
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.ngaytao = pa.parse(ngaytao);
		} catch (ParseException e) {
			this.ngaytao = null;
		}
	}
	public Message(User nguoigui, User nguoinhan, String noidung) {
		this.nguoigui = nguoigui;
		this.nguoinhan = nguoinhan;
		this.noidung = noidung;
		this.ngaytao = new Date();
	}

	public int getMatinnhan() {
		return matinnhan;
	}

	public void setMatinnhan(int matinnhan) {
		this.matinnhan = matinnhan;
	}

	public User getNguoigui() {
		return nguoigui;
	}

	public void setNguoigui(User nguoigui) {
		this.nguoigui = nguoigui;
	}

	public User getNguoinhan() {
		return nguoinhan;
	}

	public void setNguoinhan(User nguoinhan) {
		this.nguoinhan = nguoinhan;
	}

	public String getNoidung() {
		return noidung;
	}

	public void setNoidung(String noidung) {
		this.noidung = noidung;
	}

	public boolean isTrangthai() {
		return trangthai;
	}

	public void setTrangthai(boolean trangthai) {
		this.trangthai = trangthai;
	}

	public Date getNgaytao() {
		return ngaytao;
	}
	
	public String getFomattedDate() {

		long days, hours, minutes, day, month;
		String result = "";
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime old = LocalDateTime.ofInstant(ngaytao.toInstant(), ZoneId.systemDefault());
		Duration duration = Duration.between(old, now);
		days = duration.toDays();
		hours = old.getHour();
		minutes = old.getMinute();
		day = old.getDayOfMonth();
		month = old.getMonthValue();
		String h, m, d, mm;
		h = String.valueOf(hours);
		m = String.valueOf(minutes);
		d = String.valueOf(day);
		mm = String.valueOf(month);
		if(month < 10) {
			mm = "0" + month;
		}
		if(day < 10) {
			d = "0" + day;
		}
		if(hours < 10) {

			h = "0" + hours;
		}
		if(minutes < 10) {

			m = "0" + minutes;
		}
		
		if (now.getYear() > old.getYear())
			result = d + "/" + mm + "/" + old.getYear();
		else if (days >= 1)
			result = d + "/" + mm;
		else
			result = h + ":" + m;
		return result;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

}
