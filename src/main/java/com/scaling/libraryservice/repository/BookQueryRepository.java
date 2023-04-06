package com.scaling.libraryservice.repository;

import com.scaling.libraryservice.entity.Book;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {

  @PersistenceContext
  private final EntityManager manager;

  public Page<Book> findBooksByToken(List<String> tokens, Pageable pageable) {
    CriteriaBuilder cb = manager.getCriteriaBuilder();

    CriteriaQuery<Book> cq = cb.createQuery(Book.class);

    Root<Book> book = cq.from(Book.class);

    List<Predicate> predicates = new ArrayList<>();

    for (String token : tokens) {

      predicates.add(cb.like(book.get("title"), "%" + token + "%"));
    }

    cq.select(book).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

    // 페이징으로 추가된 부분
    TypedQuery<Book> query = manager.createQuery(cq);
    int total = query.getResultList().size();

    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
    query.setMaxResults(pageable.getPageSize());

    List<Book> books = query.getResultList();

    return new PageImpl<>(books, pageable, total);
  }

}
