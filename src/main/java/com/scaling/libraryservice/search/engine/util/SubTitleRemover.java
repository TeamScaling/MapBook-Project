package com.scaling.libraryservice.search.engine.util;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;


public class SubTitleRemover {


    public static String removeSubTitle(@NonNull String title) {

       return isRequiredRemove(title)? getRemovedSubTitle(title) : title;
    }

    private static String getRemovedSubTitle(String title){

        return title.split("[:=]", 2)[0].trim();
    }

    private static boolean isRequiredRemove(String title){
        return title.contains(":") || title.contains("=");
    }


}
