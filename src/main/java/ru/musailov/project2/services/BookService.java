package ru.musailov.project2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.musailov.project2.models.Book;
import ru.musailov.project2.models.Person;
import ru.musailov.project2.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {


    private final BooksRepository booksRepository;
    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();

    }

    public List<Book> findWithPagination(int page, int booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }


    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
    @Transactional
    public void update(int id, Book bookToUpdate) {
        bookToUpdate.setId(id);
        booksRepository.save(bookToUpdate);
    }
    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.get().setOwner(null);
        book.get().setTakenAt(null);
    }
    @Transactional
    public void set(int id, Person person) {
        Optional<Book> book = booksRepository.findById(id);
        book.get().setOwner(person);
        book.get().setTakenAt(new Date());
    }

    public List<Book> findWithTitle(String title) {
        return booksRepository.findByNameStartingWith(title);
    }
}
