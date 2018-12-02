package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRepositoryRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Ride> getRides() {
		
		List<Ride> rides = jdbcTemplate.query("Select * from ride", new RideRepositoryRowMapper());
//		Ride ride = new Ride();
//		ride.setName("Corner Canyon");
//		ride.setDuration(120);
//		List <Ride> rides = new ArrayList<>();
//		rides.add(ride);
		return rides;
	}

	@Override
	public Ride createRide(final Ride ride) {
		//jdbcTemplate.update("insert into ride (name,duration) values (?,?)", ride.getName(), ride.getDuration());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement("insert into ride (name,duration) values (?,?)", new String[] {"id"});
				preparedStatement.setString(1, ride.getName());
				preparedStatement.setInt(2, ride.getDuration());
				
				return preparedStatement;
			}
		}, keyHolder);
		Number id = keyHolder.getKey();
		return getRide(id.intValue());
//		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
//		
//		List<String> columnNames = new ArrayList<>();
//		columnNames.add("name");
//		columnNames.add("duration");
//		
//		insert.setTableName("ride");
//		insert.setColumnNames(columnNames);
//		
//		insert.setGeneratedKeyName("id");
//		
//		Map<String, Object> data = new HashMap<>();
//		data.put("name", ride.getName());
//		data.put("duration", ride.getDuration());
//		
//		
//		Number key =insert.executeAndReturnKey(data);
//		System.out.println(key);
	}
	
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject("select * from ride where id=?", new RideRepositoryRowMapper(), id);
		
		return ride;
	}
	
}
