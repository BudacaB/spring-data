package com.spring.data.springdata;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import com.spring.data.springdata.service.FlightService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest // will be creating some extra non JPA related Beans
public class TransactionalTests {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightService flightService;

    @Before
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldNotRollBackWhenTheresNoTransaction() {
        try {
            flightService.saveFlight(new Flight());
        } catch (Exception e) {
            // Do nothing
        } finally {
            Assertions.assertThat(flightRepository.findAll()).isNotEmpty();
        }
    }

    @Test
    public void shouldRollBackWhenThereIsATransaction() {
        try {
            flightService.saveFlightTransactional(new Flight());
        } catch (Exception e) {
            // Do nothing
        } finally {
            Assertions.assertThat(flightRepository.findAll()).isEmpty();
        }
    }
}
