package me.forum.Entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import me.forum.Controller.BaseController;

public class User {
	private String taikhoan;
	private String matkhau;
	private String hoten;
	private Date ngaysinh;
	private String gioitinh;
	private String sothich;
	private String sodienthoai;
	private String email;
	private Rule chucvu;
	private String anhdaidien;
	private Date ngaytao;
	private long lastlogin;
	private String mabaomat;
	private boolean tructuyen;

	public User(String taikhoan, String matkhau, String mabaomat, String hoten, Date ngaysinh, String gioitinh,
			String sothich, String sodienthoai, String email, int chucvu, String anhdaidien, long lastlogin,
			boolean tructuyen, Date ngaytao) {
		this.taikhoan = taikhoan;
		this.matkhau = matkhau;
		this.hoten = hoten;
		this.ngaysinh = ngaysinh;
		this.gioitinh = gioitinh;
		this.sothich = sothich;
		this.sodienthoai = sodienthoai;
		this.email = email;
		this.chucvu = BaseController.GetInstance().ruleDao.getById(chucvu);
		this.anhdaidien = anhdaidien;
		this.ngaytao = ngaytao;
		this.mabaomat = mabaomat;
		this.lastlogin = lastlogin;
		this.tructuyen = tructuyen;
	}

	public User() {
	}

	public String getTaikhoan() {
		return taikhoan;
	}

	public void setTaikhoan(String taikhoan) {
		this.taikhoan = taikhoan;
	}

	public String getMatkhau() {
		return matkhau;
	}

	public void setMabaomat(String mabaomat) {
		this.mabaomat = mabaomat;
	}

	public String getMabaomat() {
		return mabaomat;
	}

	public void setMatkhau(String matkhau) {
		this.matkhau = matkhau;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public Date getNgaysinh() {
		return ngaysinh;
	}

	public void setNgaysinh(Date ngaysinh) {
		this.ngaysinh = ngaysinh;
	}

	public String getGioitinh() {
		return gioitinh;
	}

	public void setGioitinh(String gioitinh) {
		this.gioitinh = gioitinh;
	}

	public String getSothich() {
		return sothich;
	}

	public void setSothich(String sothich) {
		this.sothich = sothich;
	}

	public String getSodienthoai() {
		return sodienthoai;
	}

	public void setSodienthoai(String sodienthoai) {
		this.sodienthoai = sodienthoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Rule getChucvu() {
		return chucvu;
	}

	public void setChucvu(Rule chucvu) {
		this.chucvu = chucvu;
	}

	public int getRank() {
		return chucvu.getHang();
	}

	public String getAnhdaidien() {
		return anhdaidien;
	}

	public void setAnhdaidien(String anhdaidien) {
		this.anhdaidien = anhdaidien;
	}

	public void setLastlogin(long lastlogin) {
		this.lastlogin = lastlogin;
	}

	public long getLastlogin() {
		return lastlogin;
	}

	public Date getNgaytao() {
		return ngaytao;
	}

	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}

	public static String MD5(String string) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		byte[] messageDigest = md.digest(string.getBytes());
		StringBuilder sb = new StringBuilder();

		for (byte b : messageDigest) {
			sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public void setTructuyen(boolean isOnline) {
		this.tructuyen = isOnline;
		BaseController.GetInstance().userDao.UpdateStatus(taikhoan, tructuyen);
	}

	public boolean getTructuyen() {
		return tructuyen;
	}

	public String getLastLogin() {
		if(tructuyen) {
			return "Đang trực tuyến";
		}
		long hours, minutes, day, month;
		String result = "";
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime old = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastlogin), ZoneId.systemDefault());
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
		result = h + ":" + m + " " + d + " Th" + mm;
		if (now.getYear() > old.getYear())
			 result += ", " + old.getYear();
		return result;
	}
	public String getStatus() {
		return tructuyen ? "online" : "offline";
	}

	@Override
	public String toString() {
		String user = "{taikhoan : " + taikhoan + ", hoten : " + hoten + ", email : " + email + ", chucvu : "
				+ chucvu.getTenchucvu() + "}";
		return user;
	}

	@Override
	public boolean equals(Object obj) {
		return this.taikhoan.equals(((User) obj).getTaikhoan());
	}

}
