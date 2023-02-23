package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.forum.entity.Comment;

@Repository
public class CommentDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Comment> GetByPostID(long id){
		try {
			return jdbcTemplate.query("select * from binhluan where mabaiviet = ?", new Object[] {id}, new int[] {Types.BIGINT}, new CommentMap());
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	

	public Comment GetByID(long id){
		try {
			return jdbcTemplate.queryForObject("select * from binhluan where mabinhluan = ?", new Object[] {id}, new int[] {Types.INTEGER}, new CommentMap());
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Comment> GetPostsLimit(long id, int start, int limit){
		try {
			return jdbcTemplate.query("select * from binhluan where mabaiviet = ? order by ngaytao asc limit ?, ?", new Object[] {id, start, limit},new int[] {Types.BIGINT, Types.INTEGER, Types.INTEGER}, new CommentMap());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Comment> GetPostsLimitDesc(long id, int start, int limit){
		try {
			return jdbcTemplate.query("select * from binhluan where mabaiviet = ? order by ngaytao desc limit ?, ?", new Object[] {id, start, limit},new int[] {Types.BIGINT, Types.INTEGER, Types.INTEGER}, new CommentMap());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int GetTotalComment(long mabaiviet) {
		try {
			return jdbcTemplate.queryForObject("SELECT COUNT(mabinhluan) FROM binhluan WHERE mabaiviet = ?", new Object[] {mabaiviet},  new int[] {Types.BIGINT}, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}
	
	public List<Comment> GetByUser(String user){
		try {
			return jdbcTemplate.query("select * from binhluan where taikhoan = ?", new Object[] {user}, new int[] {Types.CHAR}, new CommentMap());
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int DeleteByID(int cmtID) {
		return jdbcTemplate.update("delete from binhluan where mabinhluan = ?", cmtID);
	}

	public int AddComment(String noidung, String taikhoan, long baiviet) {
		return jdbcTemplate.update("insert into binhluan(noidung, taikhoan, mabaiviet) values(?,?,?)", noidung, taikhoan, baiviet);
	}
	
	private class CommentMap implements RowMapper<Comment>{

		@Override
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Comment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getString(5));
		}
		
	}
}
