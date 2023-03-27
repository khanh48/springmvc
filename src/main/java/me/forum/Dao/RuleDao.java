package me.forum.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import me.forum.Entity.Rule;

@Repository
public class RuleDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public Rule getById(int id) {
		String sql = "select * from chucvu where machucvu = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {id}, new int[] {Types.INTEGER}, new RuleMapper());
	}
	
	public List<Rule> getAll() {
		String sql = "select * from chucvu";
		return jdbcTemplate.query(sql, new RuleMapper());
	}
	
	class RuleMapper implements RowMapper<Rule>{

		@Override
		public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {

			return new Rule(rs.getInt(1), rs.getString(2));
		}
		
	}
}
