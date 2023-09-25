package ua.hryhorenko.springcourse.springrestapp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.hryhorenko.springcourse.springrestapp.dto.SensorDTO;
import ua.hryhorenko.springcourse.springrestapp.models.Sensor;
import ua.hryhorenko.springcourse.springrestapp.services.SensorService;
import ua.hryhorenko.springcourse.springrestapp.util.MeasurementErrorResponse;
import ua.hryhorenko.springcourse.springrestapp.util.MeasurementException;
import ua.hryhorenko.springcourse.springrestapp.util.SensorValidator;

import static ua.hryhorenko.springcourse.springrestapp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
  private final SensorService sensorService;
  private final ModelMapper modelMapper;
  private final SensorValidator sensorValidator;

  @Autowired
  public SensorsController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
    this.sensorService = sensorService;
    this.modelMapper = modelMapper;
    this.sensorValidator = sensorValidator;
  }

  @PostMapping("/registration")
  public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
    Sensor sensorToAdd = convertToSensor(sensorDTO);
    sensorValidator.validate(sensorToAdd, bindingResult);

    if (bindingResult.hasErrors()) {
      returnErrorsToClient(bindingResult);
    }

    sensorService.register(sensorToAdd);

    return ResponseEntity.ok(HttpStatus.OK);
  }

  @ExceptionHandler
  private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
    MeasurementErrorResponse response = new MeasurementErrorResponse(
            e.getMessage(),
            System.currentTimeMillis()
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private Sensor convertToSensor(SensorDTO sensorDTO) {
    return modelMapper.map(sensorDTO, Sensor.class);
  }
}
