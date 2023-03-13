package me.forum.Entity;

public class Like {
	private int maluotthich;
	private boolean loai;
	private String taikhoan;
	private long mabaiviet;
	private int mabinhluan;
	
	public Like(int maluotthich, boolean loai, String taikhoan, long mabaiviet, int mabinhluan) {
		this.maluotthich = maluotthich;
		this.loai = loai;
		this.taikhoan = taikhoan;
		this.mabaiviet = mabaiviet;
		this.mabinhluan = mabinhluan;
	}

	public int getMaluotthich() {
		return maluotthich;
	}

	public void setMaluotthich(int maluotthich) {
		this.maluotthich = maluotthich;
	}

	public boolean isLoai() {
		return loai;
	}

	public void setLoai(boolean loai) {
		this.loai = loai;
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

	public void setMabaiviet(int mabaiviet) {
		this.mabaiviet = mabaiviet;
	}

	public int getMabinhluan() {
		return mabinhluan;
	}

	public void setMabinhluan(int mabinhluan) {
		this.mabinhluan = mabinhluan;
	}
	
	
}
