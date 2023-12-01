package ru.musailov.project2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.musailov.project2.models.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameStartingWith(String title);
}
