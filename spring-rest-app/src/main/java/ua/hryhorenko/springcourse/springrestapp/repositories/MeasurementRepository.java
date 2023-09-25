package ua.hryhorenko.springcourse.springrestapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hryhorenko.springcourse.springrestapp.models.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
