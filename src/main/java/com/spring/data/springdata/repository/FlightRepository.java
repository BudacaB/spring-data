package com.spring.data.springdata.repository;

import com.spring.data.springdata.entity.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Long> {

    List<Flight> findByOrigin(String origin);

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByOriginIn(String ... origins);

    List<Flight> findByOriginAndDestinationIn(String origin, String ... destinations);

    List<Flight> findByOriginIgnoreCase(String origin);
}
