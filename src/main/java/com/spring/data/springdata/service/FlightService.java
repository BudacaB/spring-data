package com.spring.data.springdata.service;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void saveFlight(Flight flight) {
        flightRepository.save(flight);
        // some other queries and method calls here maybe
        throw new RuntimeException("I failed");
    }

    @Transactional
    public void saveFlightTransactional(Flight flight) {
        flightRepository.save(flight);
        // some other queries and method calls here maybe
        throw new RuntimeException("I failed");
    }
}
