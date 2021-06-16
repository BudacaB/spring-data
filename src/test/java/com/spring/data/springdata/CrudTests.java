package com.spring.data.springdata;

import com.spring.data.springdata.entity.Flight;
import com.spring.data.springdata.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataMongoTest
class CrudTests {

	@Autowired
	private FlightRepository flightRepository;

	@Test
	public void shouldPerformCRUDOperations() {
		final Flight flight = new Flight();
		flight.setOrigin("London");
		flight.setDestination("New York");
		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

		flightRepository.save(flight);

		Assertions.assertThat(flightRepository.findAll())
				.hasSize(1)
				.first()
				.isEqualToComparingFieldByField(flight);

		flightRepository.delete(flight);

		Assertions.assertThat(flightRepository.count()).isZero();
	}
}