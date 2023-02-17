package me.forum.entity;

import java.util.Date;

public class Comment {
	private int mabinhluan;
	private String noidung;
	private String taikhoan;
	private long mabaiviet;
	private Date ngaytao;
	
	public Comment(int mabinhluan, String noidung, String taikhoan, long mabaiviet, Date ngaytao) {

		this.mabinhluan = mabinhluan;
		this.noidung = noidung;
		this.taikhoan = taikhoan;
		this.mabaiviet = mabaiviet;
		this.ngaytao = ngaytao;
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
	
	
}
