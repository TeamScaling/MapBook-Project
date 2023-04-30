package com.scaling.libraryservice.search.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString @Getter
public class TitleQuery {

    private final TitleType titleType;

    private final String engToken;

    private final String korToken;

    private final String engKorToken;

}
