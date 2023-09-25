package ua.hryhorenko.springcourse.springbootapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hryhorenko.springcourse.springbootapp.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
