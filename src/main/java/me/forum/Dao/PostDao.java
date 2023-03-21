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

import me.forum.Controller.BaseController;
import me.forum.Entity.Post;

@Repository
public class PostDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public int CountPost(String user) {
		try {
			return jdbcTemplate.queryForObject("select count(mabaiviet) from baiviet where taikhoan=?",
					new Object[] { user }, new int[] { Types.CHAR }, Integer.class);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	public Post GetPostByID(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from baiviet where mabaiviet=?", new Object[] { id },
					new int[] { Types.BIGINT }, new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<Post> GetPostsByUser(String taikhoan) {
		try {
			return jdbcTemplate.query("select * from baiviet where taikhoan=? order by ngaytao desc", new Object[] { taikhoan },
					new int[] { Types.CHAR }, new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<Post> GetPostsLimit(int start, int limit) {
		try {
			return jdbcTemplate.query("select * from baiviet order by ngaytao asc limit ?, ?",
					new Object[] { start, limit }, new int[] { Types.INTEGER, Types.INTEGER }, new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<Post> GetPostsLimitDesc(int start, int limit) {
		try {
			return jdbcTemplate.query("select * from baiviet order by ngaytao desc limit ?, ?",
					new Object[] { start, limit }, new int[] { Types.INTEGER, Types.INTEGER }, new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<Post> GetPostsUserLimit(String taikhoan, int start, int limit) {
		try {
			return jdbcTemplate.query("select * from baiviet where taikhoan = ? order by ngaytao desc limit ?, ?",
					new Object[] { taikhoan, start, limit }, new int[] { Types.CHAR, Types.INTEGER, Types.INTEGER },
					new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<Post> GetLikePost(String tieude) {
		try {
			return jdbcTemplate.query("select * from baiviet where tieude like ?", new Object[] { "%" + tieude + "%" },
					new int[] { Types.NVARCHAR }, new PostMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	public int DeletePostByID(long id) {
		return jdbcTemplate.update("delete from baiviet where mabaiviet = ?", id);
	}

	public int DeletePostByUser(String taikhoan) {
		return jdbcTemplate.update("delete from baiviet where taikhoan = ?", taikhoan);
	}

	public int UpdatePostByID(long id, String tieude, String noidung, int nhom) {
		return jdbcTemplate.update("update baiviet set tieude = ?, noidung = ?, manhom = ? where mabaiviet = ?", tieude,
				noidung, nhom, id);
	}

	public int AddPost(long id, String tieude, String noidung, int nhom, String taikhoan) {
		try {
			return jdbcTemplate.update(
					"insert into baiviet(mabaiviet, tieude, noidung, manhom, taikhoan) value(?, ?, ?, ?, ?)",
					new Object[] { id, tieude, noidung, nhom, taikhoan },
					new int[] { Types.BIGINT, Types.NVARCHAR, Types.NVARCHAR, Types.INTEGER, Types.CHAR });
		} catch (DataAccessException e) {
			return 0;
		}
	}

	private class PostMapper implements RowMapper<Post> {
		@Override
		public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
			Post post = new Post(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
					rs.getString(6));
			post.setCountLike(BaseController.GetInstance().likeDao.GetTotalLikePost(rs.getLong(1)));
			post.setCountComment(BaseController.GetInstance().commentDao.GetTotalComment(rs.getLong(1)));
			post.setUser(BaseController.GetInstance().userDao.findUserByUserName(rs.getString(4)));
			return post;
		}

	}
}
