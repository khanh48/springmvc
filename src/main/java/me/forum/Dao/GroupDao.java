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

import me.forum.Entity.Group;

@Repository
public class GroupDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Group getById(int id) {
		String sql = "select * from nhom where manhom = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {id}, new int[] {Types.INTEGER}, new GroupMapper());
	}
	
	public List<Group> getGroupList(){
		String sql = "select * from nhom";
		return jdbcTemplate.query(sql, new GroupMapper());
	}
	
	public int getCountPost(int manhom) {
		String sql = "SELECT count(mabaiviet) from baiviet where manhom = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {manhom}, new int[] {Types.INTEGER}, Integer.class);
		}catch (DataAccessException e) {
			return 0;
		}
	}
	

	public int UpdateDescription(int id, String description) {
		try {
			return jdbcTemplate.update("update nhom set mota = ? where manhom = ?", description, id);
		}catch (DataAccessException e) {
			return 0;
		}
	}
	
	class GroupMapper implements RowMapper<Group>{

		@Override
		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Group(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
		}
		
	}
}
