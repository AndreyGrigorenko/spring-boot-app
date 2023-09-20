package ua.hryhorenko.springcourse.springbootapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hryhorenko.springcourse.springbootapp.models.Person;
import ua.hryhorenko.springcourse.springbootapp.services.PeopleService;
import ua.hryhorenko.springcourse.springbootapp.util.PersonErrorResponse;
import ua.hryhorenko.springcourse.springbootapp.util.PersonNotFoundException;

import java.util.List;

@RestController()
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService peopleService;

  @Autowired
  public PeopleController(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @GetMapping()
  public List<Person> getPeople() {
    return peopleService.findAll(); // Jackson конвертирует эти объекты в JSON
  }

  @GetMapping("/{id}")
  public Person getPerson(@PathVariable("id") int id) {
    return peopleService.findOne(id); // Jackson конвертирует в JSON
  }

  @ExceptionHandler
  private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
    PersonErrorResponse response = new PersonErrorResponse("Person not found", System.currentTimeMillis());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}
