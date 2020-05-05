package com.xebia.covidtracker.services.domain;

import com.xebia.covidtracker.domain.Record;
import com.xebia.covidtracker.exceptions.InvalidSearchException;
import com.xebia.covidtracker.exceptions.RecordNotFoundException;
import com.xebia.covidtracker.repository.RecordRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTests {

    @Mock
    RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void should_save_all_records() throws Exception {
        List<Record> toSave = getRecords();
        when(recordRepository.saveAll(toSave)).thenReturn(toSave);
        List<Record> saved = recordService.save(toSave);
        verify(recordRepository).saveAll(any());
        assertThat(saved).hasSize(2);
    }



    @Test
    public void should_update_records() {
        String location = "delhi";
        Record record = getRecord();
        Record record1 = getRecord();
        when(recordRepository.findByLocationIgnoreCase(any())).thenReturn(Optional.of(record));
        when(recordRepository.save(any())).thenReturn(record1);
        Record updated = recordService.update(location, record);
        verify(recordRepository).save(any());
        assertThat(updated).hasToString(record1.toString());
    }

    @Test
    public void should_not_update_record_when_not_found_and_throws_exception() {
        String location = "delhi";
        Record record = getRecord();
        Record record1 = getRecord();
        when(recordRepository.findByLocationIgnoreCase(any())).thenReturn(Optional.empty());
        thrown.expect(RecordNotFoundException.class);
        recordService.update(location, record);
    }

    @Test
    public void should_throw_InvalidQueryException_when_any_of_filters_is_invalid_when_finding_by_query() {
        List<String> locations = null;
        String type = "";
        String selected = "";
        List<Record> records = getRecords();

        when(recordRepository.findAll()).thenReturn(records);
        thrown.expect(InvalidSearchException.class);

        recordService.findByQuery(locations,type,selected);

        type = "total";
        selected="active";

        when(recordRepository.findAll()).thenReturn(records);
        thrown.expect(InvalidSearchException.class);

        recordService.findByQuery(locations,type,selected);

        type = "min";
        selected = "";

        when(recordRepository.findAll()).thenReturn(records);
        thrown.expect(InvalidSearchException.class);

        recordService.findByQuery(locations,type,selected);

        locations = Arrays.asList("delhi","mumbai");
        type="total";
        selected="active";

        when(recordRepository.findAll()).thenReturn(records);
        thrown.expect(InvalidSearchException.class);

        recordService.findByQuery(locations,type,selected);

        type = "min";
        selected = "";

        when(recordRepository.findAll()).thenReturn(records);
        thrown.expect(InvalidSearchException.class);

        recordService.findByQuery(locations,type,selected);

    }

    @Test
    public void should_return_all_records_when_location_is_not_given() {
        List<String> locations = null;
        String type = null;
        String selected = null;

        List<Record> records = getRecords();

        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAll()).thenReturn(records);

        List<Record> found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(2);

    }

    @Test
    public void should_throw_RecordNotFoundException_when_database_is_empty() {
        List<String> locations = null;
        String type = null;
        String selected = null;

        thrown.expect(RecordNotFoundException.class);

        recordService.findByQuery(locations,type,selected);
    }

    @Test
    public void should_return_valid_output_when_valid_queries_are_used() {
        List<String> locations = null;
        String type = "total";
        String selected = null;

        List<Record> records = getRecords();
        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAll()).thenReturn(records);

        List<Record> found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);

        type = "min";
        selected = "active";

        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAllRecords(any())).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);

        type = "max";
        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAllRecords(any())).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);

        List<String> locationss = Arrays.asList("delhi", "mumbai");
        type =null;
        selected =null;

        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAll()).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(2);

        type ="total";

        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAll()).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);

        type = "min";
        selected = "active";

        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAllRecords(any())).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);

        type = "max";
        when(recordRepository.findAll()).thenReturn(records);
        when(recordRepository.findAllRecords(any())).thenReturn(records);

        found = recordService.findByQuery(locations,type,selected);
        assertThat(found).hasSize(1);
    }

    private List<Record> getRecords() {
        Record record = new Record.Builder()
                .withLocation("delhi")
                .withActive(1)
                .withConfirmed(1)
                .withDead(1)
                .withRecovered(1)
                .withTested(1)
                .withUpdatedAt(new Date())
                .build();
        Record record1 = new Record.Builder()
                .withLocation("mumbai")
                .withActive(2)
                .withConfirmed(2)
                .withDead(2)
                .withRecovered(2)
                .withTested(2)
                .withUpdatedAt(new Date())
                .build();
        return Arrays.asList(record,record1);
    }

    private Record getRecord() {
        return new Record.Builder()
                .withLocation("delhi")
                .withActive(1)
                .withConfirmed(1)
                .withDead(1)
                .withRecovered(1)
                .withTested(1)
                .withUpdatedAt(new Date())
                .build();
    }
}