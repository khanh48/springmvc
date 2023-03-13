package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.forum.Entity.Image;

@Repository
public class ImageDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public int AddImage(String taikhoan, String url, long mabaiviet) {
		String sql = "insert into hinhanh(taikhoan, loai, url, mabaiviet) values(?,?,?,?)";
		return jdbcTemplate.update(sql, taikhoan, "baiviet", url, mabaiviet);

	}
	
	public List<Image> getByPost(long postID){
		String sql = "select * from hinhanh where mabaiviet = ?";
		try {
			return jdbcTemplate.query(sql, new Object[] {postID}, new int[] {Types.BIGINT}, new ImageMap());
		} catch (DataAccessException e) {
			return null;
		}
	}

	private class ImageMap implements RowMapper<Image> {

		@Override
		public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Image(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getLong(5), rs.getInt(6), rs.getString(7));
		}

	}
}
