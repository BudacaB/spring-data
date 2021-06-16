package com.spring.data.springdata;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DerivedQueryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void deleteAll() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldFindFlightsFromLondon() {
        final Flight flight1 = createFlight("London");
        final Flight flight2 = createFlight("London");
        final Flight flight3 = createFlight("Lisbon");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> flightsFromLondon = flightRepository.findByOrigin("London");

        Assertions.assertThat(flightsFromLondon).hasSize(2);
        Assertions.assertThat(flightsFromLondon.get(0)).isEqualTo(flight1);
        Assertions.assertThat(flightsFromLondon.get(1)).isEqualTo(flight2);
    }

    @Test
    public void shouldFindFlightsFromLondonToParis() {
        final Flight flight1 = createFlight("London", "Paris");
        final Flight flight2 = createFlight("London", "Lisbon");
        final Flight flight3 = createFlight("Lisbon", "Paris");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> londonToParis = flightRepository
                .findByOriginAndDestination("London", "Paris");

        Assertions.assertThat(londonToParis)
                .hasSize(1)
                .first()
                .isEqualTo(flight1);
    }

    @Test
    public void shouldFindFlightsFromLondonOrMadrid() {
        final Flight flight1 = createFlight("London", "Paris");
        final Flight flight2 = createFlight("Tokyo", "Lisbon");
        final Flight flight3 = createFlight("Madrid", "Paris");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> fromLondonOrMadrid = flightRepository
                .findByOriginIn("London", "Madrid");

        Assertions.assertThat(fromLondonOrMadrid).hasSize(2);
        Assertions.assertThat(fromLondonOrMadrid.get(0)).isEqualTo(flight1);
        Assertions.assertThat(fromLondonOrMadrid.get(1)).isEqualTo(flight3);
    }

    @Test
    public void shouldFindFlightsFromLondonToLisbonOrMadrid() {
        final Flight flight1 = createFlight("London", "Paris");
        final Flight flight2 = createFlight("London", "Lisbon");
        final Flight flight3 = createFlight("London", "Madrid");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> fromLondonOrMadridOrLisbon = flightRepository
                .findByOriginAndDestinationIn("London", "Madrid", "Lisbon");

        Assertions.assertThat(fromLondonOrMadridOrLisbon).hasSize(2);
        Assertions.assertThat(fromLondonOrMadridOrLisbon.get(0)).isEqualTo(flight2);
        Assertions.assertThat(fromLondonOrMadridOrLisbon.get(1)).isEqualTo(flight3);
    }

    @Test
    public void shouldFindFlightsFromLondonIgnoringCase() {
        final Flight flight = createFlight("LONDON");

        flightRepository.save(flight);

        List<Flight> flightsToLondon = flightRepository.findByOriginIgnoreCase("London");

        Assertions.assertThat(flightsToLondon).
                hasSize(1)
                .first()
                .isEqualTo(flight);
    }

    public Flight createFlight(String origin, String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));
        return flight;
    }

    public Flight createFlight(String origin) {
        return createFlight(origin, "Madrid");
    }
}
