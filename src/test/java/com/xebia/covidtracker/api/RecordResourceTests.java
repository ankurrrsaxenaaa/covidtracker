package com.xebia.covidtracker.api;

import com.xebia.covidtracker.api.representations.RecordRequest;
import com.xebia.covidtracker.domain.Record;
import com.xebia.covidtracker.repository.RecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RecordResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecordRepository recordRepository;

    @AfterEach
    void tearDown() {
        recordRepository.deleteAll();
    }

    @BeforeEach
    void setup(){
        RecordRequest recordRequest = new RecordRequest();
        recordRequest.setId(1);
        recordRequest.setLocation("delhi");
        recordRequest.setTested(10);
        recordRequest.setConfirmed(9);
        recordRequest.setActive(5);
        recordRequest.setRecovered(3);
        recordRequest.setDead(1);

        RecordRequest recordRequest2 = new RecordRequest();
        recordRequest2.setId(2);
        recordRequest2.setLocation("mumbai");
        recordRequest2.setTested(11);
        recordRequest2.setConfirmed(8);
        recordRequest2.setActive(4);
        recordRequest2.setRecovered(2);
        recordRequest2.setDead(2);
        List<RecordRequest> recordRequests = Arrays.asList(recordRequest,recordRequest2);
        List<Record> records = recordRequests.stream().map(RecordRequest::toRecord).collect(Collectors.toList());
        recordRepository.saveAll(records);
    }

    @Test
    void mock_mvc_should_be_set() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_initialize_data_and_return_201_when_data_is_valid() throws Exception {
        String request = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Delhi,100,100,10,1,1\n" +
                "2,Gujrat,200,101,9,1,1\n" +
                "3,Kerala,300,102,8,1,0";

        mockMvc.perform(post("/api/covid/records")
                .header("Content-Type","text/csv")
                .content(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    void should_not_initialize_data_and_return_400_when_data_is_invalid_csv_format() throws Exception {
        String request = "Id,Location,Tested,Confirmed,Active,Recovered\n" +
                "1,Delhi,100,100,10,1\n" +
                "2,Gujrat,200,101,9,1\n" +
                "3,Kerala,300,102,8,1";

        mockMvc.perform(post("/api/covid/records")
                .header("Content-Type","text/csv")
                .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_initialize_data_and_return_400_when_data_is_missing_for_csv_column() throws Exception {
        String request = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Delhi,100,100,10,1,1\n" +
                "2,Gujrat,200,101,9,1,1\n" +
                "3,Kerala,300,102,8,1";

        mockMvc.perform(post("/api/covid/records")
                .header("Content-Type","text/csv")
                .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_new_data_and_return_200_with_new_data() throws Exception {
        String request = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Lucknow,100,100,10,1,1";

        mockMvc.perform(post("/api/covid/records")
                .header("Content-Type","text/csv")
                .content(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].location").value("lucknow"));
    }

    @Test
    void should_replace_initialized_matching_data_in_database_and_return_200_with_updated_data() throws Exception {
        String request = "Id,Location,Tested,Confirmed,Active,Recovered,Dead\n" +
                "1,Delhi,100,100,10,1,1";

        mockMvc.perform(post("/api/covid/records")
                .header("Content-Type","text/csv")
                .content(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].tested").value(100))
                .andExpect(jsonPath("$[0].location").value("delhi"));
    }

    @Test
    void should_update_a_particular_record_and_return_200_and_the_updated_record() throws Exception {
        String request = "{\"tested\":\"10\"}";
        mockMvc.perform(patch("/api/covid/records")
                .param("location","delhi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tested").value(20));
    }

    @Test
    void should_not_update_a_particular_record_and_return_404_when_location_is_not_found() throws Exception {
        String request = "{\"tested\":\"10\"}";
        mockMvc.perform(patch("/api/covid/records")
                .param("location","lucknow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_update_a_particular_record_and_return_400_when_location_is_not_missing() throws Exception {
        String request = "{\"tested\":\"10\"}";
        mockMvc.perform(patch("/api/covid/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_find_records_and_return_200_with_all_records_when_no_parameter_is_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_be_able_to_find_records_and_return_200_with_all_records_when_locations_are_passed() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","delhi")
                .param("location", "mumbai"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_be_able_to_find_records_and_return_200_with_sum_of_all_records_when_type_is_requested_as_total_and_locations_are_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","total")
                .param("location","delhi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tested").value(10));
    }

    @Test
    void should_be_able_to_find_records_and_return_200_with_records_when_type_is_requested_as_min_or_max_selected_as_tested_and_locations_are_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","min")
                .param("selected","tested")
                .param("location","delhi"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tested").value(10));
    }

    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_type_is_total_and_selected_is_requested_with_location() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","delhi")
                .param("type","total")
                .param("selected","tested"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_type_is_not_requested_and_selected_is_requested_with_location() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","delhi")
                .param("selected","tested"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location", "")
                .param("type","")
                .param("selected",""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_be_able_to_find_records_and_return_200_with_sum_of_all_records_when_type_is_requested_as_total() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","total"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tested").value(21));
    }

    @Test
    void should_be_able_to_find_records_and_return_200_with_records_when_type_is_requested_as_min_or_max_selected_as_tested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","min")
                .param("selected","tested"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tested").value(10));
    }

    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_type_is_total_and_selected_is_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","total")
                .param("selected","tested"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_type_is_not_requested_and_selected_is_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("selected","tested"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_without_location() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","")
                .param("selected",""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_return_400_when_invalid_parameters_are_passed_with_wrong_value_of_type() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("type","wow"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_able_to_return_200_and_empty_response_when_parameters_are_passed_with_location_not_in_database() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","lucknow"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_be_able_to_return_404_and_empty_response_when_parameters_are_passed_with_location_not_in_database_and_type_is_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","lucknow")
                .param("type","total"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_be_able_to_return_404_and_empty_response_when_parameters_are_passed_with_location_not_in_database_and_type_and_selected_are_requested() throws Exception {
        mockMvc.perform(get("/api/covid/records")
                .param("location","lucknow")
                .param("type","min")
                .param("selected","active"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_be_able_to_return_404_and_empty_response_when_database_is_empty() throws Exception {
        recordRepository.deleteAll();
        mockMvc.perform(get("/api/covid/records"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}