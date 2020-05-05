package com.xebia.covidtracker.repository;

import com.xebia.covidtracker.domain.Record;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {

    Optional<Record> findByLocationIgnoreCase(String location);

    @Query(value="SELECT u FROM Record u")
    List<Record> findAllRecords(Sort sort);

    List<Record> findByLocationInIgnoreCase(List<String> locations);

    List<Record> findByLocationInIgnoreCase(List<String> locations, Sort sort);
}
