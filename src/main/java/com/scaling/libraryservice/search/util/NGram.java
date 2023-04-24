package com.scaling.libraryservice.search.util;

import java.util.ArrayList;
import java.util.List;

public class NGram {
	public static List<String> getNGrams(String str, int n) {
		List<String> ngrams = new ArrayList<String>();
		str = str.toLowerCase().replaceAll("[^a-z\\s]+", "");
		for (int i = 0; i < str.length() - n + 1; i++) {
			ngrams.add(str.substring(i, i + n));
		}
		return ngrams;
	}

}

