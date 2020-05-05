package com.xebia.covidtracker.services.helpers.file;

import com.xebia.covidtracker.api.representations.RecordRequest;
import com.xebia.covidtracker.exceptions.InvalidFileException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class CsvServiceTests {

    @InjectMocks
    CsvService csvService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_be_able_to_map_csv_when_valid_data_is_provided() {
        String csvContent = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Delhi,100,100,10,1,1\n" +
                "2,Gujrat,200,101,9,1,1\n" +
                "3,Kerala,300,102,8,1,0";
        List<RecordRequest> recordRequests = csvService.mapToCSV(csvContent,RecordRequest.class);
        assertThat(recordRequests).hasSize(3);
    }

    @Test
    public void should_be_able_to_throw_exception_when_invalid_data_is_provided() {
        String csvContent = "Id,Location,Tested,Confirmed,Active,Recovered\n" +
                "1,Delhi,100,100,10,1\n" +
                "2,Gujrat,200,101,9,1\n" +
                "3,Kerala,300,102,8,1";

        thrown.expect(InvalidFileException.class);
        List<RecordRequest> recordRequests = csvService.mapToCSV(csvContent,RecordRequest.class);
    }

    @Test
    public void should_be_able_to_throw_exception_when_incomplete_data_is_provided() {
        String csvContent = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Delhi,100,100,10,1,1\n" +
                "2,Gujrat,200,101,9,1,1\n" +
                "3,Kerala,300,102,8,1";
        thrown.expect(RuntimeException.class);
        List<RecordRequest> recordRequests = csvService.mapToCSV(csvContent,RecordRequest.class);
    }
}