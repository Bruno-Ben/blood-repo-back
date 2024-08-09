package com.teste.bloodapi.model;

import lombok.Data;

import java.util.Map;

@Data
public class PersonResponse {

    private Map<String, Integer> peopleByState;
    private Map<String, Double> averageBMIByAgeGroup;
    private Map<String, Double> percentageOfSexWithHighBMI;
    private Map<String, Double> averageAgePerBloodType;
    private Map<String, Integer> donorCount;

}