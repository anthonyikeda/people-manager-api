package com.example.basicapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final Logger log = LoggerFactory.getLogger(PersonController.class);


    private final PersonManager manager;

    public PersonController(PersonManager manager) {
        this.manager = manager;
    }

    @PostMapping
    public ResponseEntity<PersonDAO> createPerson(@RequestParam String name, @RequestParam Integer age) {
        PersonDAO created = this.manager.createPerson(name, age);
        return ResponseEntity.created(URI.create(String.format("/api/person/", created.getPersonId()))).build();
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable("personId") Integer personId) {
        try {
            this.manager.softDeletePerson(personId);
            log.debug("Person {} deleted", personId);
            return ResponseEntity.ok().build();
        } catch(PersonNotFoundException pnfe) {
            log.error("Person with id {} was not found", personId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDAO> findById(@PathVariable("personId") Integer personId) {
        PersonDAO person;

        try {
            person = this.manager.findPersonById(personId);
            return ResponseEntity.ok(person);
        } catch(Exception e) {
            log.error("Unable to find person with id {}", personId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PersonDAO>> findAllPaginated(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<PersonDAO> results = this.manager.findAllPaginated(start, size);
        return ResponseEntity.ok(results);
    }
}