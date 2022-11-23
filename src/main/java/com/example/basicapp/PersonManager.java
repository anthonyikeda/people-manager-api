package com.example.basicapp;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@Component
public class PersonManager {

    private final Logger log = LoggerFactory.getLogger(PersonManager.class);

    private final JdbcTemplate template;
    private MeterRegistry registry;

    public PersonManager(JdbcTemplate temp, MeterRegistry registry) {
        this.template = temp;
        this.registry = registry;
    }

    public List<PersonDAO> findAllPaginated(int start, int size) {
        String findPaginated = "select person_id, name, age from person where person_deleted = false limit ? offset ?";

        Timer timer = Timer.builder("manager-get-persons-paginated")
        .description("Measures how long to call the jdbc template for paginated list")
        .tags("manager", "jdbcTemplate", "paginated","true")
        .register(registry);

        return timer.record( () -> this.template.query(con -> {
                PreparedStatement pstmt = con.prepareStatement(findPaginated);
                pstmt.setInt(1, size);
                pstmt.setInt(2, start);
                return pstmt;
            },
            new PersonRowMapper()));
    }
    public void softDeletePerson(Integer personId) throws PersonNotFoundException {
        final String softDeletePerson = "update person set person_deleted = true where person_id = ? and person_deleted = false";

        int totalUpdates = this.template.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(softDeletePerson);
            pstmt.setLong(1, personId);
            return pstmt;
        });

        if(totalUpdates == 0) {
            log.error("Person not found with id {}", personId);
            throw new PersonNotFoundException(String.format("Person with id %s not found", personId));
        } else {
            log.debug("Deleted person with id {}", personId);
        }

    }
    public PersonDAO createPerson(String name, int age) {
        final String savePerson = "insert into person(name, age) values(?, ?) returning person_id";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PersonDAO toSave = new PersonDAO();
        toSave.setName(name);
        toSave.setAge(age);

        this.template.update(connection -> {
            PreparedStatement pss = connection.prepareStatement(savePerson, new String[]{"person_id"});
            pss.setString(1, name);
            pss.setInt(2, age);
            return pss;
        }, keyHolder);

        toSave.setPersonId((Long) keyHolder.getKey());
        log.debug("Returning key of {}", keyHolder.getKey());
        return toSave;
    }

    public PersonDAO findPersonById(Integer personId) throws PersonNotFoundException {
        final String sql = "select person_id, name, age from person where person_id = ? and person_deleted = false";

        Timer timer = Timer.builder("manager-get-person-by-id")
        .description("Measures how long to call the jdbc template")
        .tags("manager", "jdbcTemplate")
        .register(registry);

        PersonDAO personToReturn = timer.record(() ->
            this.template.query(sql,
                                pstmt -> pstmt.setInt(1, personId),
                                rs -> {
                if(rs.next()) {
                    var person = new PersonDAO();
                    person.setPersonId(rs.getLong("person_id"));
                    person.setName(rs.getString("name"));
                    person.setAge(rs.getInt("age"));
                    return person;
                } else {
                    return null;
                }
            })
        );

        if (personToReturn == null) {
            throw new PersonNotFoundException(String.format("Unable to find person with id %d", personId));
        }
        return personToReturn;
    }
}
