package com.scaling.libraryservice.search.util;

import com.scaling.libraryservice.search.util.TitleDivider.Language;
import java.util.List;
import java.util.Map;

public interface TitleTokenizer {

    List<String> tokenize(String target);

    Map<Language, List<String>> tokenize2(String target);
}
