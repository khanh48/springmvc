package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.forum.entity.User;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public User findUserByUserName(String userName) {
		try {
			return jdbcTemplate.queryForObject("select * from nguoidung where taikhoan=?", new Object[] { userName }, new int[] {Types.CHAR},
					new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public User findUserByCrypt(String crypt) {
		try {
			return jdbcTemplate.queryForObject("select * from nguoidung where mabaomat = ?", new Object[] { crypt }, new int[] {Types.NCHAR},
					new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int AddUser(User user) {
		return jdbcTemplate.update("insert into nguoidung(hoten, taikhoan, matkhau) values(?, ?, ?)", user.getHoten(),
				user.getTaikhoan(), User.MD5(user.getMatkhau()));
	}

	public int AddUser(String hoten, String taikhoan, String matkhau) {
		return jdbcTemplate.update("insert into nguoidung(hoten, taikhoan, matkhau) values(?, ?, ?)", hoten, taikhoan,
				User.MD5(matkhau));
	}

	public List<User> findUserWhere(String query) {
		try {
			return jdbcTemplate.query("select * from nguoidung where " + query, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<User> GetLikeUser(String userName) {
		try {
			return jdbcTemplate.query("select * from nguoidung where hoten like ?",
					new Object[] { "%" + userName + "%" }, new int[] {Types.CHAR}, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int UpdateProfile(String userName, String hoten, String gioitinh, Date ngaysinh, String sodienthoai,
			String sothich, String anhdaidien) {
		return jdbcTemplate.update(
				"update nguoidung set hoten = ?, gioitinh = ?, ngaysinh = ?, sodienthoai = ?, sothich = ?, anhdaidien = ? where taikhoan = ?",
				hoten, gioitinh, ngaysinh, sodienthoai, sothich, anhdaidien, userName);
	}

	public int UpdateMaBaoMat(String userName, String mabaomat, long lastlogin) {
		return jdbcTemplate.update("update nguoidung set mabaomat = ?, lastlogin = ? where taikhoan = ?", mabaomat, lastlogin, userName);
	}

	public int ChangePassword(String userName, String newPassword) {
		return jdbcTemplate.update("update nguoidung set matkhau = ? where taikhoan = ?", newPassword, userName);
	}

	public int ChangePassword(User user) {
		return jdbcTemplate.update("update nguoidung set matkhau = ? where taikhoan = ?", user.getMatkhau(),
				user.getTaikhoan());
	}

	public int UpdateUser(String userName, String hoten, String gioitinh, Date ngaysinh, String sodienthoai,
			String chucvu) {
		return jdbcTemplate.update(
				"update nguoidung set hoten = ?, gioitinh = ?, ngaysinh = ?, sodienthoai = ?, chucvu = ? where taikhoan = ?",
				hoten, gioitinh, ngaysinh, sodienthoai, chucvu, userName);
	}

	public int UpdateUser(User user) {
		return jdbcTemplate.update(
				"update nguoidung set hoten = ?, gioitinh = ?, ngaysinh = ?, sodienthoai = ?, chucvu = ? where taikhoan = ?",
				user.getHoten(), user.getGioitinh(), user.getNgaysinh(), user.getSodienthoai(), user.getChucvu(),
				user.getTaikhoan());
	}

	private class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),
					rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),
					rs.getLong(11), rs.getDate(12));
		}
	}
}
