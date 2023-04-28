package com.scaling.libraryservice.search.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelationWords {
    private List<String> tokens;

    @Override
    public String toString() {
        return "Tokens{" +
            "token='" + tokens + '\'' +
            '}';
    }

}
