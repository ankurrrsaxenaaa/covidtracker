package com.xebia.covidtracker.api;

import com.xebia.covidtracker.api.representations.RecordRequest;
import com.xebia.covidtracker.api.representations.RecordResponse;
import com.xebia.covidtracker.domain.Record;
import com.xebia.covidtracker.services.domain.RecordService;
import com.xebia.covidtracker.services.helpers.file.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/covid/records")
public class RecordResource {

    @Autowired
    RecordService recordService;

    @Autowired
    CsvService csvService;

    @PostMapping
    public ResponseEntity<List<RecordResponse>> initializeRecords(@RequestBody String csvContent) throws Exception {
        List<RecordRequest> records = csvService.mapToCSV(csvContent, RecordRequest.class);
        List<Record> toSave = records.stream().map(RecordRequest::toRecord).collect(Collectors.toList());
        List<Record> saved = recordService.save(toSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                saved.stream().map(RecordResponse::from).collect(Collectors.toList()));
    }

    @PatchMapping
    public ResponseEntity<RecordResponse> updateRecord(@RequestParam("location") String location, @RequestBody RecordRequest recordRequest){
        Record copyFrom = recordRequest.toRecord();
        Record updated = recordService.update(location, copyFrom);
        return ResponseEntity.status(HttpStatus.OK).body(RecordResponse.from(updated));
    }

    @GetMapping
    public ResponseEntity<List<RecordResponse>> getRecord(@RequestParam(value = "location", required = false) List<String> locations,
                                                          @RequestParam(value = "type", required = false) String type,
                                                          @RequestParam(value = "selected", required = false) String selected){

        List<Record> records = recordService.findByQuery(locations, type, selected);
        return ResponseEntity.status(HttpStatus.OK).body(
                records.stream().map(RecordResponse::from).collect(Collectors.toList()));
    }
}
