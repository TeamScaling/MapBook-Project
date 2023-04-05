package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Book;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {

    @PersistenceContext
    private final EntityManager manager;

    public List<Book> findBooksByToken(List<String> tokens) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();

        CriteriaQuery<Book> cq = cb.createQuery(Book.class);

        Root<Book> book = cq.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();

        for(String token : tokens){

            predicates.add(cb.like(book.get("title"),"%"+token+"%"));
        }


        cq.select(book).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));


        return manager.createQuery(cq).getResultList();
    }

}
