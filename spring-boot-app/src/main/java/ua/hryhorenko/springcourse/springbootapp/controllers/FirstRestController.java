package ua.hryhorenko.springcourse.springbootapp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FirstRestController {
  @RequestMapping("/sayhello")
  public String sayHello() {
    return "Hello!!!";
  }

}
