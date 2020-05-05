package com.xebia.covidtracker;

import com.xebia.covidtracker.repository.RecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CovidtrackerApplicationTests {

	@Autowired
	RecordRepository recordRepository;

	@AfterEach
	void tearDown() {
		recordRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

}
