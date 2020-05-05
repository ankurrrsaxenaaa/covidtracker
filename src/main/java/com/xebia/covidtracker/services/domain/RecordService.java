package com.xebia.covidtracker.services.domain;

import com.xebia.covidtracker.domain.Record;
import com.xebia.covidtracker.exceptions.InvalidSearchException;
import com.xebia.covidtracker.exceptions.RecordNotFoundException;
import com.xebia.covidtracker.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.xebia.covidtracker.domain.RecordSelection.*;
import static com.xebia.covidtracker.domain.RecordType.*;

@Service
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    public List<Record> save(List<Record> toSave) throws Exception {
        return recordRepository.saveAll(toSave);
    }

    public Record update(String location, Record copyFrom) {
        Record record = findByLocation(location);
        record.update(copyFrom);
        return recordRepository.save(record);
    }

    private Record findByLocation(String location) {
        Optional<Record> optionalRecord = recordRepository.findByLocationIgnoreCase(location);
        if (!optionalRecord.isPresent()){
            throw new RecordNotFoundException();
        }
        return optionalRecord.get();
    }

    public List<Record> findByQuery(List<String> locations, String type, String selected) {
        if(recordRepository.findAll().isEmpty()){
            throw new RecordNotFoundException();
        }

        if(locations==null){
            if(type!=null){
                if(ofType(type)==TOTAL&&selected!=null||ofType(type)!=TOTAL&&selected==null){
                    throw new InvalidSearchException();
                }
                if(ofType(type)==TOTAL){
                    List<Record> records = recordRepository.findAll();
                    return sumOfRecords(records);
                }
                if(ofType(type)==MIN){
                    if(selected==null){
                        throw new InvalidSearchException();
                    }
                    if(ofSelection(selected)==ACTIVE){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.ASC, "active"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==CONFIRMED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.ASC, "confirmed"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==TESTED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.ASC, "tested"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==RECOVERED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.ASC, "recovered"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==DEAD){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.ASC, "dead"));
                        return Collections.singletonList(records.get(0));
                    }
                    throw new InvalidSearchException();
                }
                if(ofType(type)==MAX){
                    if(selected==null){
                        throw new InvalidSearchException();
                    }
                    if(ofSelection(selected)==ACTIVE){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.DESC, "active"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==CONFIRMED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.DESC, "confirmed"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==TESTED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.DESC, "tested"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==RECOVERED){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.DESC, "recovered"));
                        return Collections.singletonList(records.get(0));
                    }
                    if(ofSelection(selected)==DEAD){
                        List<Record> records = recordRepository.findAllRecords(Sort.by(Sort.Direction.DESC, "dead"));
                        return Collections.singletonList(records.get(0));
                    }
                    throw new InvalidSearchException();
                }
            }
            if(selected!=null){
                throw new InvalidSearchException();
            }
            return recordRepository.findAll();
        }
        else if(locations.isEmpty()) {
            throw new InvalidSearchException();
        }

        /**
         * When Location filter is active
         */

        if(type!=null){
            if(ofType(type)==TOTAL&&selected!=null||ofType(type)!=TOTAL&&selected==null){
                throw new InvalidSearchException();
            }
            if(ofType(type)==TOTAL){
                List<Record> records = recordRepository.findByLocationInIgnoreCase(locations);
                if(records.isEmpty())
                    throw new RecordNotFoundException();
                return sumOfRecords(records);
            }
            if(ofType(type)==MIN){
                if(selected==null){
                    throw new InvalidSearchException();
                }
                if(ofSelection(selected)==ACTIVE){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.ASC, "active"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==CONFIRMED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.ASC, "confirmed"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==TESTED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.ASC, "tested"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==RECOVERED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.ASC, "recovered"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==DEAD){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.ASC, "dead"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                throw new InvalidSearchException();
            }
            if(ofType(type)==MAX){
                if(selected==null){
                    throw new InvalidSearchException();
                }
                if(ofSelection(selected)==ACTIVE){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.DESC, "active"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==CONFIRMED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.DESC, "confirmed"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==TESTED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.DESC, "tested"));
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==RECOVERED){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.DESC, "recovered"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                if(ofSelection(selected)==DEAD){
                    List<Record> records = recordRepository.findByLocationInIgnoreCase(locations,Sort.by(Sort.Direction.DESC, "dead"));
                    if(records.isEmpty())
                        throw new RecordNotFoundException();
                    return Collections.singletonList(records.get(0));
                }
                throw new InvalidSearchException();
            }
        }
        if(selected!=null){
            throw new InvalidSearchException();
        }
        return recordRepository.findByLocationInIgnoreCase(locations);
    }

    private List<Record> sumOfRecords(List<Record> records) {
        Record sum = new Record();
        for(Record record:records){
            sum.update(record);
        }
        return Collections.singletonList(sum);
    }
}
