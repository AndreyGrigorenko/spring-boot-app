package ua.hryhorenko.springcourse.springrestapp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.hryhorenko.springcourse.springrestapp.dto.MeasurementDTO;
import ua.hryhorenko.springcourse.springrestapp.dto.MeasurementResponse;
import ua.hryhorenko.springcourse.springrestapp.models.Measurement;
import ua.hryhorenko.springcourse.springrestapp.services.MeasurementService;
import ua.hryhorenko.springcourse.springrestapp.util.MeasurementErrorResponse;
import ua.hryhorenko.springcourse.springrestapp.util.MeasurementException;
import ua.hryhorenko.springcourse.springrestapp.util.MeasurementValidator;

import java.util.stream.Collectors;

import static ua.hryhorenko.springcourse.springrestapp.util.ErrorsUtil.returnErrorsToClient;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
  private MeasurementService measurementService;
  private MeasurementValidator measurementValidator;
  private ModelMapper modelMapper;

  @Autowired
  public MeasurementController(MeasurementService measurementService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
    this.measurementService = measurementService;
    this.measurementValidator = measurementValidator;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/add")
  public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
    Measurement measurementToAdd = convertToMeasurement(measurementDTO);
    measurementValidator.validate(measurementToAdd, bindingResult);

    if (bindingResult.hasErrors()) {
      returnErrorsToClient(bindingResult);
    }

    measurementService.addMeasurement(measurementToAdd);

    return ResponseEntity.ok(HttpStatus.OK);
  }

  @GetMapping()
  public MeasurementResponse getMeasurements() {
    return new MeasurementResponse(measurementService.findAll().stream().map(this::convertToMeasurementDTO)
            .collect(Collectors.toList()));
  }

  @GetMapping("/rainyDaysCount")
  public Long getRainyDaysCount() {
    return measurementService.findAll().stream().filter(Measurement::getRaining).count();
  }

  private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
    return modelMapper.map(measurementDTO, Measurement.class);
  }

  private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
    return modelMapper.map(measurement, MeasurementDTO.class);
  }

  @ExceptionHandler
  private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
    MeasurementErrorResponse response = new MeasurementErrorResponse(
            e.getMessage(),
            System.currentTimeMillis()
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
