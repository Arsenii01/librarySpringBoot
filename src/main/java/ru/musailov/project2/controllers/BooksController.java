package ru.musailov.project2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.musailov.project2.models.Book;
import ru.musailov.project2.models.Person;
import ru.musailov.project2.services.BookService;
import ru.musailov.project2.services.PeopleService;


import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {


    private final BookService bookService;

    private final PeopleService peopleService;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) boolean sortByYear) {
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books",
                    bookService.findWithPagination(page, booksPerPage, sortByYear));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Optional<Book> book = bookService.findById(id);
        model.addAttribute("book", book.get());

        Person owner = book.get().getOwner();
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("book", bookService.findById(id).get());
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                           BindingResult bindingResult,
                           @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/set")
    public String set(@ModelAttribute("person") Person person,
                      @PathVariable("id") int id) {
        bookService.set(id, person);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "books/search";
    }

    @PostMapping("/search")
    public String searchBook(Model model,
                             @RequestParam(name = "title", required = false) String title) {
        model.addAttribute("books", bookService.findWithTitle(title));
        return "books/search";
    }
}
