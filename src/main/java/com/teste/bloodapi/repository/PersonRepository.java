package com.teste.bloodapi.repository;

import com.teste.bloodapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
