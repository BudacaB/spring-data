package com.spring.data.springdata.repository;

import com.spring.data.springdata.entity.Flight;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {
}
