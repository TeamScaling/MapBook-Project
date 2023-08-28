package com.scaling.libraryservice.search.engine;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * 검색 쿼리를 분석한 결과를 저장하기 위한 클래스입니다. 각 검색어 토큰과 검색어 유형(TitleType)에 대한 정보를 저장합니다.
 */
@ToString
@Getter
public class TitleQuery {


    /**
     * 검색어 유형 정보를 저장하는 변수
     */
    private final TitleType titleType;

    /**
     * 영어 검색어 토큰을 저장하는 변수
     */
    private final String etcToken;

    /**
     * 한국어 검색어 토큰을 저장하는 변수
     */
    private final String nnToken;

    /**
     * 사용자가 입력한 검색어를 저장하는 변수
     */
    private final String userQuery;



    private TitleQuery(@NonNull TitleType titleType, @NonNull String etcToken,
        @NonNull String nnToken, String userQuery) {

        this.titleType = titleType;
        this.etcToken = etcToken;
        this.nnToken = nnToken;
        this.userQuery = userQuery;
    }

    public boolean isEmptyTitleQuery(){
        return nnToken.isBlank() && etcToken.isBlank();
    }

    @Getter
    public static class TitleQueryBuilder {
        private TitleType titleType;
        private String etcToken = "";
        private String nnToken = "";
        private String userQuery = "";

        public TitleQueryBuilder titleType(TitleType titleType) {
            this.titleType = titleType;
            return this;
        }

        public TitleQueryBuilder etcToken(String etcToken) {
            this.etcToken = etcToken;
            return this;
        }

        public TitleQueryBuilder nnToken(String nnToken) {
            this.nnToken = nnToken;
            return this;
        }

        public TitleQueryBuilder userQuery(String query) {
            this.userQuery = query;
            return this;
        }

        public TitleQuery build() {
            return new TitleQuery(titleType, etcToken, nnToken, userQuery);
        }
    }
}
