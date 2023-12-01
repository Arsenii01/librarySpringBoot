package ru.musailov.project2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.musailov.project2.models.Person;
import ru.musailov.project2.repositories.PeopleRepository;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> foundPerson = peopleRepository.findByFullName(person.getFullName());
        if (foundPerson.isPresent() && person.getId() != foundPerson.get().getId()) {
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже зарегистрирован");
        }

    }
}
