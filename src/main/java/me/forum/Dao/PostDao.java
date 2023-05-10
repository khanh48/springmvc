package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
	public List<Post> getTopList(){
		String sql = "SELECT * FROM baiviet INNER JOIN (SELECT manhom, MAX(ngaytao) AS recent_post_date FROM baiviet GROUP BY manhom) AS latest_posts ON baiviet.manhom = latest_posts.manhom AND baiviet.ngaytao = latest_posts.recent_post_date ORDER BY baiviet.ngaytao DESC limit 3;";
		return jdbcTemplate.query(sql, new PostMapper());
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
	

	public List<Post> GetAll() {
		try {
			return jdbcTemplate.query("select * from baiviet", new PostMapper());
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
	

	public List<Post> ByGroupLimit(int manhom, int limit) {
		try {
			return jdbcTemplate.query("select * from baiviet where manhom = ? order by ngaytao desc limit ?",
					new Object[] { manhom, limit }, new int[] {Types.INTEGER, Types.INTEGER },
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
	public int UpdatePost(Post post) {
		return jdbcTemplate.update("update baiviet set tieude = ?, noidung = ?, manhom = ? where mabaiviet = ?", post.getTieude(),
				post.getNoidung(), post.getManhom(), post.getMabaiviet());
	}

	public List<Post> FindLikePost(String taikhoan, String id, String tieude, String noidung, String nhom) {
		String sql = "select * from baiviet where taikhoan like ? and mabaiviet like ? and tieude like ? and noidung like ? and manhom like ?";
		try {
			return jdbcTemplate.query(sql, new Object[] { "%" + taikhoan + "%",
					"%" + id + "%",
					"%" + tieude + "%",
					"%" + noidung + "%", 
					"%" + nhom + "%" },
					new int[] { Types.CHAR, Types.CHAR, Types.LONGNVARCHAR, Types.NVARCHAR, Types.NCHAR}, new PostMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int AddPost(long id, String tieude, String noidung, int nhom, String taikhoan) {
		try {
			return jdbcTemplate.update(
					"insert into baiviet(mabaiviet, tieude, noidung, manhom, taikhoan) value(?, ?, ?, ?, ?)",
					new Object[] { id, tieude, noidung, nhom, taikhoan },
					new int[] { Types.BIGINT, Types.NVARCHAR, Types.LONGNVARCHAR, Types.INTEGER, Types.CHAR });
		} catch (DataAccessException e) {
			return 0;
		}
	}
	
	
	public Post getHotPost(int manhom) {
		String sql = "SELECT baiviet.*, COUNT(luotthich.maluotthich) AS total_likes "
				+ "FROM baiviet "
				+ "INNER JOIN luotthich ON baiviet.mabaiviet = luotthich.mabaiviet and baiviet.manhom = ? "
				+ "GROUP BY baiviet.mabaiviet "
				+ "ORDER BY total_likes DESC "
				+ "LIMIT 1;";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {manhom}, new int[] {Types.INTEGER}, new PostMapper());
		}catch (DataAccessException e) {
			return null;
		}
	}
	public Post getExaltedPost(int manhom) {
		String sql = "SELECT baiviet.*, COUNT(binhluan.mabinhluan) AS total_comments "
				+ "FROM baiviet "
				+ "INNER JOIN binhluan ON baiviet.mabaiviet = binhluan.mabaiviet and baiviet.manhom = ? "
				+ "GROUP BY baiviet.mabaiviet "
				+ "ORDER BY total_comments DESC "
				+ "LIMIT 1;";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {manhom}, new int[] {Types.INTEGER}, new PostMapper());
		}catch (DataAccessException e) {
			return null;
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
