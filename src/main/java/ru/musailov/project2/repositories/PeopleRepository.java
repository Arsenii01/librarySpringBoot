package ru.musailov.project2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.musailov.project2.models.Person;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByFullName(String name);
}
