package com.xebia.covidtracker.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RecordSelection {
    TESTED, CONFIRMED, ACTIVE, RECOVERED, DEAD;

    private static final Map<String, RecordSelection> lookup =
            Arrays.stream(values())
                    .collect(Collectors.toMap(Enum::name, Function.identity()));
    public static RecordSelection ofSelection(String selection){
        return lookup.get(selection.toUpperCase());
    }
}
