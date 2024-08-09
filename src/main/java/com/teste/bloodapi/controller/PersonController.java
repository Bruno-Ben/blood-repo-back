package com.teste.bloodapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teste.bloodapi.model.Person;
import com.teste.bloodapi.model.PersonResponse;
import com.teste.bloodapi.service.PersonService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {

    @Autowired
    private PersonService personService;

    private ObjectMapper objectMapper;

    public PersonController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
    }

    @PostMapping("/data")
    public void uploadFile(@RequestBody MultipartFile file) throws IOException {
        String content = new String(file.getBytes()); // Convert file bytes to string
        List<Person> people = objectMapper.readValue(content, new TypeReference<List<Person>>() {});
        personService.saveOrUpdatePeople(people);
    }

    @GetMapping("/data")
    public PersonResponse returnData() throws IOException {
        List<Person> people = personService.findAll();

        Map<String, Integer> peopleByState = new TreeMap<>(personService.countPeopleByState(people));
        Map<String, Double> averageBMIByAgeGroup = new TreeMap<>(personService.calculateAverageBMIByAgeGroup(people));
        Map<String, Double> percentageOfSexWithHighBMI = new TreeMap<>(personService.calculatePercentageOfSexWithHighBMI(people));
        Map<String, Double> averageAgePerBloodType = personService.calculateAverageAgePerBloodType(people);
        Map<String, Integer> donorCount = personService.countPossibleDonors(people);

        PersonResponse response = new PersonResponse();

        response.setPeopleByState(peopleByState);
        response.setAverageBMIByAgeGroup(averageBMIByAgeGroup);
        response.setPercentageOfSexWithHighBMI(percentageOfSexWithHighBMI);
        response.setAverageAgePerBloodType(averageAgePerBloodType);
        response.setDonorCount(donorCount);

        return response;
    }

}
