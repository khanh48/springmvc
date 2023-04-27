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
import me.forum.Entity.Message;
import me.forum.Entity.User;

@Repository
public class MessageDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	UserDao userDao = BaseController.GetInstance().userDao;

	public List<Message> getAllMessage(String user, String user1) {
		String sql = "select * from tinnhan where (nguoinhan = ? and nguoigui = ?) or (nguoinhan = ? and nguoigui = ?) order by ngaytao desc";
		return jdbcTemplate.query(sql, new Object[] { user, user1, user1, user },
				new int[] { Types.CHAR, Types.CHAR, Types.CHAR, Types.CHAR }, new MessageMapping());
	}

	public List<Message> getLimitMessage(String user, String user1, int start, int limit) {
		String sql = "select * from tinnhan where (nguoinhan = ? and nguoigui = ?) or (nguoinhan = ? and nguoigui = ?) order by ngaytao desc limit ?, ?";
		return jdbcTemplate.query(sql, new Object[] { user, user1, user1, user, start, limit},
				new int[] { Types.CHAR, Types.CHAR, Types.CHAR, Types.CHAR, Types.INTEGER, Types.INTEGER }, new MessageMapping());
	}
	public Integer AddMessage(String nguoigui, String nguoinhan, String noidung) {
		try {
			return jdbcTemplate.update("insert into tinnhan(nguoigui, nguoinhan, noidung) value(?, ?, ?)",
					new Object[] { nguoigui, nguoinhan, noidung },
					new int[] { Types.CHAR, Types.CHAR, Types.LONGNVARCHAR });
		} catch (DataAccessException e) {
			return 0;
		}
	}

	class MessageMapping implements RowMapper<Message> {
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			User from, to;
			from = userDao.findUserByUserName(rs.getString(2));
			to = userDao.findUserByUserName(rs.getString(3));
			return new Message(rs.getInt(1), from, to, rs.getString(4), rs.getBoolean(5), rs.getString(6));
		}

	}
}
