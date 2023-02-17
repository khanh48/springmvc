package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.forum.entity.Like;

@Repository
public class LikeDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public int GetTotalLikePost(long mabaiviet) {
		try {
			return jdbcTemplate.queryForObject("SELECT COUNT(maluotthich) FROM luotthich WHERE mabaiviet = ?",
					new Object[] { mabaiviet }, new int[] { Types.BIGINT }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	public int IsLiked(long mabaiviet, String taikhoan, boolean isPost) {
		String sql = "SELECT COUNT(maluotthich) FROM luotthich WHERE +" + (isPost ? "mabaiviet" : "mabinhluan")
				+ " = ? and taikhoan = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { mabaiviet, taikhoan },
					new int[] { Types.BIGINT, Types.CHAR }, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	public int AddLike(boolean loai, String taikhoan, long id) {
		String sql = "insert into luotthich(loai, taikhoan, ma" + (loai ? "baiviet" : "binhluan") + ") values(?,?,?)";
		return jdbcTemplate.update(sql, loai, taikhoan, id);

	}
	

	public int DeleteLike(boolean loai, String taikhoan, long id) {
		String sql = "delete from luotthich where taikhoan = ? and ma" + (loai ? "baiviet" : "binhluan") + " = ?";
		return jdbcTemplate.update(sql, taikhoan, id);

	}

	private class LikeMap implements RowMapper<Like> {

		@Override
		public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Like(rs.getInt(1), rs.getBoolean(2), rs.getString(3), rs.getLong(4), rs.getInt(5));
		}

	}
}
