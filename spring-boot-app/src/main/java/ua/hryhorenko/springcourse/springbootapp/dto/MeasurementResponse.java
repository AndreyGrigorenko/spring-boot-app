package ua.hryhorenko.springcourse.springbootapp.dto;

import java.util.List;

public class MeasurementResponse {
  private List<MeasurementDTO> measurement;

  public MeasurementResponse(List<MeasurementDTO> measurement) {
    this.measurement = measurement;
  }

  public List<MeasurementDTO> getMeasurement() {
    return  measurement;
  }

  public void setMeasurement(List<MeasurementDTO> measurement) {
    this.measurement = measurement;
  }
}
