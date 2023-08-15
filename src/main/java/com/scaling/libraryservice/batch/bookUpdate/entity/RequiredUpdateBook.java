package com.scaling.libraryservice.batch.bookUpdate.entity;

import com.scaling.libraryservice.batch.bookUpdate.dto.BookApiDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "REQUIRED_UPDATE_BOOK")
@Setter
@Getter
@ToString
public class RequiredUpdateBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "LOAN_CNT")
    private int loanCnt;

    @Column(name = "TITLE_NM")
    @Nullable
    private String title;

    @Nullable
    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Nullable
    @Column(name = "AUTHR_NM")
    private String author;

    @Nullable
    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Nullable
    @Column(name = "pblicte_de")
    private String publishDate;

    @Nullable
    @Column(name = "publisher")
    private String publisher;

    @Column(name = "not_Found")
    private Boolean notFound = false;

    public RequiredUpdateBook() {
    }

    public RequiredUpdateBook(String isbn, int loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }

    public void updateData(BookApiDto bookApiDto){
        this.title = bookApiDto.getTitle();
        this.author = bookApiDto.getAuthors();
        this.content = bookApiDto.getContents();
        this.bookImg = bookApiDto.getThumbnail();
        this.publisher = bookApiDto.getPublisher();
        this.publishDate = bookApiDto.getDateTime();
        this.notFound = false;
    }

}

