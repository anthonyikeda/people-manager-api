package com.example.basicapp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/object")
public class SampleController {

    private ObjectRepository repository;

    public SampleController(ObjectRepository repo) {
        this.repository = repo;
    }

    @GetMapping()
    public ResponseEntity<List<PersonDAO>> getAllPeople() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDAO> getPersonById(@PathVariable("personId") Integer personId) {
        Optional<PersonDAO> result = this.repository.findById(personId);

        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<Void> createObject(@RequestParam("name") String name, @RequestParam("age") Integer age) {

        var person = new PersonDAO();
        person.setName(name);
        person.setAge(age);

        PersonDAO saved = this.repository.save(person);

        return ResponseEntity.created(URI.create(String.format("/api/object/%s", saved.getPersonId()))).build();
    }
}