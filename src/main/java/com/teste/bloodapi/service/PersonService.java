package com.teste.bloodapi.service;

import com.teste.bloodapi.model.Person;
import com.teste.bloodapi.repository.PersonRepository;
import com.teste.bloodapi.service.util.BloodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BloodUtils util;

    public void saveOrUpdatePeople(List<Person> people) {
        personRepository.saveAll(people);
    }

    public List<Person> findAll () {return personRepository.findAll();}

    public Map<String, Integer> countPeopleByState(List<Person> people) {
        Map<String, Integer> stateCountMap = new HashMap<>();

        for (Person person : people) {
            String state = person.getEstado();
            stateCountMap.put(state, stateCountMap.getOrDefault(state, 0) + 1);
        }

        return stateCountMap;
    }

    public Map<String, Double> calculateAverageBMIByAgeGroup(List<Person> people) {
        Map<String, Double> bmiSumMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        for (Person person : people) {
            int age = util.calculateAge(person.getData_nasc());
            String ageGroup = util.determineAgeGroup(age);

            double bmi = util.calculateBMI(person);
            bmiSumMap.put(ageGroup, bmiSumMap.getOrDefault(ageGroup, 0.0) + bmi);
            countMap.put(ageGroup, countMap.getOrDefault(ageGroup, 0) + 1);
        }

        Map<String, Double> averageBmiMap = new HashMap<>();
        for (String ageGroup : bmiSumMap.keySet()) {
            averageBmiMap.put(ageGroup, bmiSumMap.get(ageGroup) / countMap.get(ageGroup));
        }

        return averageBmiMap;
    }



    public static Map<String, Double> calculatePercentageOfSexWithHighBMI(List<Person> people) {
        Map<String, Long> totalCountByGender = people.stream()
                .collect(Collectors.groupingBy(Person::getSexo, Collectors.counting()));

        Map<String, Long> highBmiCountByGender = people.stream()
                .filter(person -> person.getBmi() > 30)
                .collect(Collectors.groupingBy(Person::getSexo, Collectors.counting()));

        Map<String, Double> percentages = new HashMap<>();

        for (Map.Entry<String, Long> entry : totalCountByGender.entrySet()) {
            String gender = entry.getKey();
            long totalCount = entry.getValue();
            long highBmiCount = highBmiCountByGender.getOrDefault(gender, 0L);
            double percentage = (totalCount > 0) ? (100.0 * highBmiCount / totalCount) : 0.0;
            percentages.put(gender, percentage);
        }

        return percentages;
    }

    public Map<String, Double> calculateAverageAgePerBloodType(List<Person> people) {
        Map<String, Double> bloodTypeAges =  new HashMap<>();
        Map<String, Integer> countPerBloodType = new HashMap<>();

        for (Person person : people) {
            String bloodType = person.getTipo_sanguineo();
            double exactAge = util.calculateExactAge(person.getData_nasc());
            bloodTypeAges.put(bloodType, bloodTypeAges.getOrDefault(bloodType, 0.0) + exactAge);
            countPerBloodType.put(bloodType,  countPerBloodType.getOrDefault(bloodType, 0) + 1);
        }

        Map<String, Double> averageAgesPerBloodType = new HashMap<>();
        for (String bloodType : bloodTypeAges.keySet()) {
            double totalAge = bloodTypeAges.get(bloodType);
            int count = countPerBloodType.get(bloodType);
            double averageAge = totalAge / count;

            averageAgesPerBloodType.put(bloodType, averageAge);
        }

        return averageAgesPerBloodType;
    }


    public Map<String, Integer> countPossibleDonors(List<Person> people) {
        Map<String, Integer> donorCountMap = new HashMap<>();
        Map<String, String[]> compatibilityMap = util.getBloodCompatibilityMap();

        for (String recipientType : compatibilityMap.keySet()) {
            donorCountMap.put(recipientType, 0);
        }

        for (Person person : people) {
            String donorBloodType = person.getTipo_sanguineo();

            if (!person.canDonate()) {
                continue;
            }

            for (Map.Entry<String, String[]> entry : compatibilityMap.entrySet()) {
                String recipientType = entry.getKey();
                String[] compatibleDonors = entry.getValue();

                for (String compatibleDonor : compatibleDonors) {
                    if (donorBloodType.equals(compatibleDonor)) {
                        donorCountMap.put(recipientType, donorCountMap.get(recipientType) + 1);
                        break;
                    }
                }
            }
        }

        return donorCountMap;
    }
}