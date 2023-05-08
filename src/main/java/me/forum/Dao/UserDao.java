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

import me.forum.Entity.User;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public User findUserByUserName(String userName) {
		try {
			return jdbcTemplate.queryForObject("select * from nguoidung where taikhoan=?", new Object[] { userName },
					new int[] { Types.CHAR }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<User> getUserUnderRank(int rank) {
		try {
			return jdbcTemplate.query("select * from nguoidung where machucvu < ?", new Object[] { rank },
					new int[] { Types.INTEGER }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public User findUserByCrypt(String crypt) {
		try {
			return jdbcTemplate.queryForObject("select * from nguoidung where mabaomat = ?", new Object[] { crypt },
					new int[] { Types.NCHAR }, new UserMapper());
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
	

	public int RemoveUser(String taikhoan) {
		return jdbcTemplate.update("delete from nguoidung where taikhoan = ?", taikhoan);
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
					new Object[] { "%" + userName + "%" }, new int[] { Types.CHAR }, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<User> FindLikeUser(String taikhoan, String hoten, String email, String sdt, String rank) {
		String sql = "select * from nguoidung where taikhoan like ? and hoten like ? and email like ? and sodienthoai like ? and machucvu like ?";
		try {
			return jdbcTemplate.query(sql, new Object[] { "%" + taikhoan + "%",
					"%" + hoten + "%",
					"%" + email + "%",
					"%" + sdt + "%", 
					"%" + rank + "%" },
					new int[] { Types.CHAR, Types.NVARCHAR, Types.CHAR, Types.CHAR, Types.CHAR}, new UserMapper());
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
		return jdbcTemplate.update("update nguoidung set mabaomat = ?, lastlogin = ? where taikhoan = ?", mabaomat,
				lastlogin, userName);
	}


	public int UpdateStatus(String userName, boolean status) {
		return jdbcTemplate.update("update nguoidung set tructuyen = ? where taikhoan = ?", status, userName);
	}

	public int ChangePassword(String userName, String newPassword) {
		return jdbcTemplate.update("update nguoidung set matkhau = ? where taikhoan = ?", newPassword, userName);
	}

	public int ChangePassword(User user) {
		return jdbcTemplate.update("update nguoidung set matkhau = ? where taikhoan = ?", user.getMatkhau(),
				user.getTaikhoan());
	}

	public int UpdateUser(String userName, String hoten, String gioitinh, Date ngaysinh, String sodienthoai,
			int machucvu) {
		return jdbcTemplate.update(
				"update nguoidung set hoten = ?, gioitinh = ?, ngaysinh = ?, sodienthoai = ?, machucvu = ? where taikhoan = ?",
				hoten, gioitinh, ngaysinh, sodienthoai, machucvu, userName);
	}

	public int UpdateUser(User user) {
		String sql = "update nguoidung set hoten = ?, gioitinh = ?, ngaysinh = ?, sodienthoai = ?, machucvu = ?, email = ?, sothich = ? where taikhoan = ?";
		return jdbcTemplate.update(sql, user.getHoten(), user.getGioitinh(), user.getNgaysinh(), user.getSodienthoai(),
				user.getRank(), user.getEmail(), user.getSothich(), user.getTaikhoan());
	}

	public int SetLastLogin(String user) {
		String sql = "update nguoidung set lastlogin = ? where taikhoan = ?";
		return jdbcTemplate.update(sql, System.currentTimeMillis(), user);
	}

	private class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),
					rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11),
					rs.getLong(12), rs.getBoolean(13), rs.getDate(14));
		}
	}
}
