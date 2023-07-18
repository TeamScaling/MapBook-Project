package com.scaling.libraryservice.search.service;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySQL8DialectCustom extends MySQL8Dialect {

    public MySQL8DialectCustom(){
        super();

        registerFunction(
                "BooleanMatch",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in boolean mode)")
        );

        registerFunction(
                "NaturalMatch",
                new SQLFunctionTemplate(StandardBasicTypes.DOUBLE, "match(?1) against (?2 in natural language mode)")
        );
    }

}