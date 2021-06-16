package com.spring.data.springdata.repository;

import com.spring.data.springdata.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FlightRepository extends PagingAndSortingRepository<Flight, Long>, DeleteByOriginRepository {

    List<Flight> findByOrigin(String origin);

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByOriginIn(String ... origins);

    List<Flight> findByOriginAndDestinationIn(String origin, String ... destinations);

    List<Flight> findByOriginIgnoreCase(String origin);

    Page<Flight> findByOrigin(String origin, Pageable pageRequest);
}
