package me.forum.Entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Image {
	private int mahinhanh;
	private String taikhoan;
	private String loai;
	private String url;
	private long mabaiviet;
	private int mabinhluan;
	private Date ngaytao;
	
	public Image(int mahinhanh, String taikhoan, String loai, String url, long mabaiviet, int mabinhluan,
			String ngaytao) {
		super();
		this.mahinhanh = mahinhanh;
		this.taikhoan = taikhoan;
		this.loai = loai;
		this.url = url;
		this.mabaiviet = mabaiviet;
		this.mabinhluan = mabinhluan;
		SimpleDateFormat pa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.ngaytao = pa.parse(ngaytao);
		} catch (ParseException e) {
			this.ngaytao = null;
		}
	}
	
	public int getMahinhanh() {
		return mahinhanh;
	}
	public void setMahinhanh(int mahinhanh) {
		this.mahinhanh = mahinhanh;
	}
	public String getTaikhoan() {
		return taikhoan;
	}
	public void setTaikhoan(String taikhoan) {
		this.taikhoan = taikhoan;
	}
	public String getLoai() {
		return loai;
	}
	public void setLoai(String loai) {
		this.loai = loai;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getMabaiviet() {
		return mabaiviet;
	}
	public void setMabaiviet(long mabaiviet) {
		this.mabaiviet = mabaiviet;
	}
	public int getMabinhluan() {
		return mabinhluan;
	}
	public void setMabinhluan(int mabinhluan) {
		this.mabinhluan = mabinhluan;
	}
	public Date getNgaytao() {
		return ngaytao;
	}
	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}
	
	
	
}
