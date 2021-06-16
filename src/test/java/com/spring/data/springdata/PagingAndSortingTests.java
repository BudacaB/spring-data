package com.spring.data.springdata;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PagingAndSortingTests {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldSortFlightsByDestination() {
        final Flight flight1 = createFlight("Madrid");
        final Flight flight2 = createFlight("Lisbon");
        final Flight flight3 = createFlight("Paris");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        final Iterable<Flight>  flights = flightRepository.findAll(Sort.by("destination"));

        Assertions.assertThat(flights).hasSize(3);

        final Iterator<Flight> iterator = flights.iterator();

        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Lisbon");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Madrid");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Paris");
    }

    @Test
    public void shouldSortFlightsByScheduledAndThenName() {
        final LocalDateTime now = LocalDateTime.now();
        final Flight flight1 = createFlight("Paris", now);
        final Flight flight2 = createFlight("Paris", now.plusHours(2));
        final Flight flight3 = createFlight("Paris", now.minusHours(1));
        final Flight flight4 = createFlight("London", now.plusHours(1));
        final Flight flight5 = createFlight("London", now);

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        flightRepository.save(flight5);

        final Iterable<Flight> flights = flightRepository
                .findAll(Sort.by("destination", "scheduledAt"));

        Assertions.assertThat(flights).hasSize(5);

        final Iterator<Flight> iterator = flights.iterator();

        Assertions.assertThat(iterator.next()).isEqualTo(flight5);
        Assertions.assertThat(iterator.next()).isEqualTo(flight4);
        Assertions.assertThat(iterator.next()).isEqualTo(flight3);
        Assertions.assertThat(iterator.next()).isEqualTo(flight1);
        Assertions.assertThat(iterator.next()).isEqualTo(flight2);
    }

    @Test
    public void shouldPageResults() {
        for (int i = 0; i < 50; i++) {
            flightRepository.save(createFlight(String.valueOf(i)));
        }

        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("10", "11", "12", "13", "14");
    }

    @Test
    public void shouldPageAndSortResults() {
        for (int i = 0; i < 50; i++) {
            flightRepository.save(createFlight(String.valueOf(i)));
        }

        final Page<Flight> page = flightRepository
                .findAll(PageRequest.of(2, 5, Sort.by(DESC, "destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("44", "43", "42", "41", "40");
    }

    @Test
    public void shouldPageAndSortADerivedQuery() {
        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("Paris");
            flightRepository.save(flight);
        }

        for (int i = 0; i < 10; i++) {
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("London");
            flightRepository.save(flight);
        }

        final Page<Flight> page = flightRepository
                .findByOrigin("London", PageRequest.of(0, 5, Sort.by(DESC, "destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(10);
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("9", "8", "7", "6", "5");
    }

    public Flight createFlight(String destination, LocalDateTime scheduledAt) {
        final Flight flight = new Flight();
        flight.setOrigin("London");
        flight.setDestination(destination);
        flight.setScheduledAt(scheduledAt);
        return flight;
    }

    public Flight createFlight(String destination) {
        return createFlight(destination, LocalDateTime.parse("2011-12-13T12:12:00"));
    }
}
