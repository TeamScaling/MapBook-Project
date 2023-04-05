package com.scaling.libraryservice.search.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
public class Rental extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}


