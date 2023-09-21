package ua.hryhorenko.springcourse.springbootapp.util;

public class PersonNotCreatedException extends RuntimeException {
  public PersonNotCreatedException(String message) {
    super(message);
  }
}
