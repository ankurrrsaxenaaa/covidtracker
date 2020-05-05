package com.xebia.covidtracker.services.helpers.file;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.xebia.covidtracker.exceptions.InvalidFileException;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;

@Service
public class CsvService{

    public <T> List<T> mapToCSV(String csvContent, Class<T> mapToClass) {
        if(csvContent.isEmpty()){
            throw new InvalidFileException();
        }

        List<String> validHeaders = new ArrayList<>();
        Arrays.stream(mapToClass.getDeclaredFields()).forEach(field -> validHeaders.add(field.getName()));

        List<String> csvHeaders = Arrays.asList(csvContent.split(","));

        if(!isValidContent(csvHeaders, validHeaders)){
            throw new InvalidFileException();
        }

        CsvToBean<T> csvToBean = new CsvToBean<T>();

        Map<String, String> columnMapping = new HashMap<>();
        Arrays.stream(mapToClass.getDeclaredFields()).forEach(field -> {
            columnMapping.put(field.getName(), field.getName());
        });

        HeaderColumnNameTranslateMappingStrategy<T> strategy = new HeaderColumnNameTranslateMappingStrategy<T>();
        strategy.setType(mapToClass);
        strategy.setColumnMapping(columnMapping);
        csvToBean.setMappingStrategy(strategy);

        CSVReader reader = new CSVReader(new StringReader(csvContent));
        csvToBean.setCsvReader(reader);
        return csvToBean.parse();
    }

    private boolean isValidContent(List<String> csvHeaders, List<String> validHeaders) {
        if(csvHeaders.size()<validHeaders.size())
            throw new InvalidFileException();
        int count = 0;
        for(int i = 0; i<validHeaders.size();i++){
            if (csvHeaders.get(i).equalsIgnoreCase(validHeaders.get(i))){
                count++;
            }
        }
        return count == validHeaders.size()-1;
    }
}
