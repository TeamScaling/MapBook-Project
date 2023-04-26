package com.scaling.libraryservice.search.dto;

import com.scaling.libraryservice.search.entity.Book;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelatedBookDto {
    private String title;

    public RelatedBookDto(Book book) {
        BookDto bookDto = new BookDto(book);
        this.title = bookDto.getTitle();
    }


    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "RelatedBookDto{" +
            "title='" + title + '\'' +
            '}';
    }

    //중복제거
// equals()와 hashCode() 메소드 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelatedBookDto that = (RelatedBookDto) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public String getRelatedTitle() {
        String[] titleParts = this.title.split(":");
        if (titleParts.length > 1) {
            String titlePrefix = titleParts[0];
            String[] titlePrefixParts = titlePrefix.trim().split("=");
            if (titlePrefixParts.length > 0) {
                titlePrefix = titlePrefixParts[0].trim();
            }
            return removeParentheses(removeDash(titlePrefix)).trim();
        }
        return removeParentheses(removeDash(this.title)).trim();
    }

    private String removeParentheses(String text) {
        return text.replaceAll("\\(.*?\\)|=.*$", "").trim();
    }

    private String removeDash(String text) {
        return text.replaceAll("-.*$", "").trim();
    }


}