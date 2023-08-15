package com.scaling.libraryservice.batch.loanCnt.chunk;

import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.lang.Nullable;

public class LibraryCatalogTokenizer extends DelimitedLineTokenizer {
    private final FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

    private final String idRegex = "^\\\"-?\\d+\\\"$";


    @Override
    public FieldSet tokenize(@Nullable String line) {

        if(!line.split(",")[0].matches(idRegex) || !line.endsWith(",") ){
            return fieldSetFactory.create(new String[0]);
        }

        return super.tokenize(line);
    }

}
