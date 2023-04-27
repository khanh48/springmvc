package me.forum.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public Date getFomattedDate() {
		return ngaytao;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

}
