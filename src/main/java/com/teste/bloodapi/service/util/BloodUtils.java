package com.teste.bloodapi.service.util;

import com.teste.bloodapi.model.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
@Service
public class BloodUtils {

    public double calculateBMI(Person person) {
        return person.getPeso() / (person.getAltura() * person.getAltura());
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String determineAgeGroup(int age) {
        if (age <= 10) {
            return "0-10";
        } else if (age <= 20) {
            return "11-20";
        } else if (age <= 30) {
            return "21-30";
        } else if (age <= 40) {
            return "31-40";
        } else if (age <= 50) {
            return "41-50";
        } else if (age <= 60) {
            return "51-60";
        } else if (age <= 70) {
            return "61-70";
        } else if (age <= 80) {
            return "71-80";
        } else if (age <= 90) {
            return "81-90";
        } else {
            return "91+";
        }
    }

    public double calculateExactAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }

        LocalDate today = LocalDate.now();
        long totalDays = ChronoUnit.DAYS.between(birthDate, today);
        double years = totalDays / 365.25;

        return years;
    }

    public Map<String, String[]> getBloodCompatibilityMap() {
        Map<String, String[]> compatibilityMap = new HashMap<>();
        compatibilityMap.put("A+", new String[]{"A+", "A-", "O+", "O-"});
        compatibilityMap.put("A-", new String[]{"A-", "O-"});
        compatibilityMap.put("B+", new String[]{"B+", "B-", "O+", "O-"});
        compatibilityMap.put("B-", new String[]{"B-", "O-"});
        compatibilityMap.put("AB+", new String[]{"AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-"});
        compatibilityMap.put("AB-", new String[]{"AB-", "A-", "B-", "O-"});
        compatibilityMap.put("O+", new String[]{"O+", "O-"});
        compatibilityMap.put("O-", new String[]{"O-"});
        return compatibilityMap;

    }

}
