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

import me.forum.Entity.Notification;

@Repository
public class NotificationDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Notification> GetByNguoiGui(String nguoiGui) {
		try {
			return jdbcTemplate.query("select * from thongbao where nguoigui = ?", new Object[] { nguoiGui },
					new int[] { Types.CHAR }, new NotiMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Notification GetById(long id) {
		try {
			return jdbcTemplate.queryForObject("select * from thongbao where mathongbao = ?", new Object[] { id },
					new int[] { Types.BIGINT }, new NotiMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Notification> GetByNguoiNhan(String nguoiNhan) {
		try {
			return jdbcTemplate.query("select * from thongbao where nguoinhan = ? order by ngaytao desc",
					new Object[] { nguoiNhan }, new int[] { Types.CHAR }, new NotiMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int AddNotification(long id, String nguoigui, String noidung, String nguoinhan, String url) {
		return jdbcTemplate.update(
				"insert into thongbao(mathongbao, nguoigui, noidung, nguoinhan, url) value(?, ?, ?, ?, ?)", id,
				nguoigui, noidung, nguoinhan, url);
	}

	public int AddNotification(Notification notification) {
		return jdbcTemplate.update(
				"insert into thongbao(mathongbao, nguoigui, noidung, nguoinhan, url) value(?, ?, ?, ?, ?)",
				notification.getMathongbao(), notification.getNguoigui(), notification.getNoidung(),
				notification.getNguoinhan(), notification.getUrl());
	}
	public int RemoveReaded(String user) {
		try {
			return jdbcTemplate.update("delete from thongbao where trangthai = 1 and nguoinhan = ?", user);
		} catch (Exception e) {
			return 0;
		}
	}
	public int Remove(long id) {
		try {
			return jdbcTemplate.update("delete from thongbao where mathongbao = ?", id);
		} catch (Exception e) {
			return 0;
		}
	}

	public int MakeAsRead(long id) {
		try {
			return jdbcTemplate.update("update thongbao set trangthai = 1 where mathongbao = ?", id);
		} catch (Exception e) {
			return 0;
		}
	}

	public int MakeAsReadAll(String user) {
		try {
			return jdbcTemplate.update("update thongbao set trangthai = 1 where nguoinhan = ?", user);
		} catch (Exception e) {
			return 0;
		}
	}

	private class NotiMapper implements RowMapper<Notification> {
		@Override
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Notification(rs.getLong(1), rs.getBoolean(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7));
		}

	}
}
