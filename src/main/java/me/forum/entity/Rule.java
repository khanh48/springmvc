package me.forum.Entity;

public class Rule {
	private int machucvu;
	private String tenchucvu;
	private int hang;
	
	public Rule(int machucvu, String tenchucvu, int hang) {
		super();
		this.machucvu = machucvu;
		this.tenchucvu = tenchucvu;
		this.hang = hang;
	}
	
	public int getMachucvu() {
		return machucvu;
	}
	public void setMachucvu(int machucvu) {
		this.machucvu = machucvu;
	}
	public String getTenchucvu() {
		return tenchucvu;
	}
	public void setTenchucvu(String tenchucvu) {
		this.tenchucvu = tenchucvu;
	}
	public int getHang() {
		return hang;
	}
	public void setHang(int hang) {
		this.hang = hang;
	}
	
	
}
