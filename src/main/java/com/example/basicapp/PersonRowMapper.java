package com.example.basicapp;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<PersonDAO> {

    @Override
    public PersonDAO mapRow(ResultSet rs, int row) throws SQLException {
        var person = new PersonDAO();
        person.setPersonId(rs.getLong("person_id"));
        person.setName(rs.getString("name"));
        person.setAge(rs.getInt("age"));
        return person;
    }
}
