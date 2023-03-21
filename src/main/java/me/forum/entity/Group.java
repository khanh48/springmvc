package me.forum.Entity;

import java.util.Date;

public class Group {
	private int manhom;
	private String tennhom;
	private String mota;
	private Date ngaytao;
	
	public Group(int manhom, String tennhom, String mota, Date ngaytao) {
		this.manhom = manhom;
		this.tennhom = tennhom;
		this.mota = mota;
		this.ngaytao = ngaytao;
	}
	public int getManhom() {
		return manhom;
	}
	public void setManhom(int manhom) {
		this.manhom = manhom;
	}
	public String getTennhom() {
		return tennhom;
	}
	public void setTennhom(String tennhom) {
		this.tennhom = tennhom;
	}
	public String getMota() {
		return mota;
	}
	public void setMota(String mota) {
		this.mota = mota;
	}
	public Date getNgaytao() {
		return ngaytao;
	}
	public void setNgaytao(Date ngaytao) {
		this.ngaytao = ngaytao;
	}
	
	
}
