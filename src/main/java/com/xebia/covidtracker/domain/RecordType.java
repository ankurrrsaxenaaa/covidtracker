package com.xebia.covidtracker.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RecordType {
    TOTAL,MIN,MAX;

    private static final Map<String, RecordType> lookup =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Enum::name, Function.identity()));
    public static RecordType ofType(String type){
        return lookup.get(type.toUpperCase());
    }
}
