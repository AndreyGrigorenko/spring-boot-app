package ua.hryhorenko.springcourse.springbootapp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.hryhorenko.springcourse.springbootapp.dto.PersonDTO;
import ua.hryhorenko.springcourse.springbootapp.models.Person;
import ua.hryhorenko.springcourse.springbootapp.services.PeopleService;
import ua.hryhorenko.springcourse.springbootapp.util.PersonErrorResponse;
import ua.hryhorenko.springcourse.springbootapp.util.PersonNotCreatedException;
import ua.hryhorenko.springcourse.springbootapp.util.PersonNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService peopleService;
  private final ModelMapper modelMapper;

  @Autowired
  public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
    this.peopleService = peopleService;
    this.modelMapper = modelMapper;
  }

  @GetMapping()
  public List<PersonDTO> getPeople() {
    return peopleService.findAll().stream().map(this::convertToPersonDTO).collect(Collectors.toList()); // Jackson конвертирует эти объекты в JSON
  }

  @GetMapping("/{id}")
  public PersonDTO getPerson(@PathVariable("id") int id) {
    return convertToPersonDTO(peopleService.findOne(id)); // Jackson конвертирует в JSON
  }

  @PostMapping()
  public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      StringBuilder errorMsg = new StringBuilder();
      List<FieldError> errors = bindingResult.getFieldErrors();

      for (FieldError error : errors) {
        errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
      }

      throw new PersonNotCreatedException(errorMsg.toString());
    }
    peopleService.save(convertToPerson(personDTO));

    return ResponseEntity.ok(HttpStatus.OK);
  }

  private Person convertToPerson(PersonDTO personDTO) {
    return modelMapper.map(personDTO, Person.class);
  }

  private PersonDTO convertToPersonDTO(Person person) {
    return modelMapper.map(person, PersonDTO.class);
  }

  @ExceptionHandler
  private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
    PersonErrorResponse response = new PersonErrorResponse("Person not found", System.currentTimeMillis());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
    PersonErrorResponse response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}