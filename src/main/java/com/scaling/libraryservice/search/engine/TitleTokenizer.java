package com.scaling.libraryservice.search.engine;

import java.util.List;
import java.util.Map;

public interface TitleTokenizer {

    Map<Token, List<String>> tokenize(String target);
}
