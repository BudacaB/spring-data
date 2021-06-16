package com.spring.data.springdata;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomImplTests {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldSaveCustomImpl() {
        final Flight toDelete = createFlight("London");
        final Flight toKeep = createFlight("Paris");

        flightRepository.save(toDelete);
        flightRepository.save(toKeep);

        flightRepository.deleteByOrigin("London");

        Assertions.assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .isEqualTo(toKeep);
    }

    public Flight createFlight(String origin) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Tokyo");
        flight.setScheduledAt(LocalDateTime.now());
        return flight;
    }
}
