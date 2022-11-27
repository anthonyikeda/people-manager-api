package com.example.basicapp;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final Logger log = LoggerFactory.getLogger(PersonController.class);


    private final PersonManager manager;
    private final MeterRegistry registry;

    public PersonController(PersonManager manager, MeterRegistry registry) {
        this.manager = manager;
        this.registry = registry;
    }

    @PutMapping("/{personId}")
    public ResponseEntity<Void> updatePerson(@PathVariable("personId") Long personId, @RequestBody PersonDAO person) {
        log.info("Person id: {}, name: {}, age: {}", personId, person.getName(), person.getAge());
        person.setPersonId(personId);
        this.manager.updatePerson(person);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping
    public ResponseEntity<PersonDAO> createPerson(@RequestParam String name, @RequestParam Integer age) {
        PersonDAO created = this.manager.createPerson(name, age);
        return ResponseEntity.created(URI.create(String.format("/api/person/%s", created.getPersonId()))).build();
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
        Timer timer = this.registry.timer("find-person-by-id");

        return timer.record(() -> {
            try {
                PersonDAO person = this.manager.findPersonById(personId);
                return ResponseEntity.ok(person);
            } catch(Exception e) {
                log.error("Unable to find person with id {}", personId);
                return ResponseEntity.notFound().build();
            }
        });
    }

    @GetMapping
    public ResponseEntity<List<PersonDAO>> findAllPaginated(@RequestParam(name = "start", defaultValue = "0") Integer start, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<PersonDAO> results = this.manager.findAllPaginated(start, size);
        Long total = this.manager.getTotalPersons();
        return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(total)).body(results);
    }
}
