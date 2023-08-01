package com.scaling.libraryservice.search.engine;

import static com.scaling.libraryservice.search.engine.TitleType.*;
import static com.scaling.libraryservice.search.engine.Token.ETC_TOKEN;
import static com.scaling.libraryservice.search.engine.Token.NN_TOKEN;

import com.scaling.libraryservice.commons.timer.MeasureTaskTime;
import com.scaling.libraryservice.search.exception.NotQualifiedQueryException;
import com.scaling.libraryservice.search.engine.TitleQuery.TitleQueryBuilder;
import com.scaling.libraryservice.search.engine.filter.FilterStream;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 검색어에 대한 분석을 수행하는 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TitleAnalyzer {

    private final EunjeonTokenizer tokenizer;
    private final FilterStream filterStream;
    private static final int TOKEN_MIN_SIZE = 1;
    private static final int TOKEN_MAX_SIZE = 2;

    @MeasureTaskTime
    public TitleQuery analyze(String query, boolean filterOn) throws NotQualifiedQueryException {

        // filterOn에 따라 filter를 진행 할지 결정 된다.
        query = filterStream.doFiltering(query, filterOn);

        // tokenizer를 거치면 원본 검색어가 수정되기 때문에 필요 모드에서 사용하기 위해 원본을 저장 한다.
        String originalQuery = query;

        Map<Token, List<String>> titleMap = tokenizer.tokenize(query);

        // title 분석에 대한 결과를 담을 TitleQuery의 builder를 만든다.
        TitleQueryBuilder titleQueryBuilder = new TitleQueryBuilder().userQuery(originalQuery);

        // 형태소 분석을 마친 결과를 TitleQuery의 멤버 변수에 맞게 각각 담는다.
        putAnalyzedWord(titleMap, titleQueryBuilder);

        // 어떤 검색 모드를 실시 할지 결정 한다.
        return determineTitleType(
            titleMap.get(NN_TOKEN).size(),
            titleMap.get(ETC_TOKEN).size(),
            titleQueryBuilder);
    }

    // 형태소 분석을 마친 결과를 TitleQuery의 멤버 변수에 맞게 각각 담는다.
    private void putAnalyzedWord(Map<Token, List<String>> titleMap,
        TitleQueryBuilder titleQueryBuilder) {

        titleMap.forEach((key, tokens) -> {
            if (key == NN_TOKEN) {
                titleQueryBuilder.nnToken(String.join(" ", tokens));
            } else {
                titleQueryBuilder.etcToken(String.join(" ", tokens));
            }
        });
    }

    // 자연어 분석으로 나뉘어진 단어(token)의 유형별 갯수에 따라 TitleType을 결정 한다.
    private TitleQuery determineTitleType(int nnCnt, int etcCnt,
        TitleQueryBuilder titleQueryBuilder) {

        // 명사 토큰이 최대치 이상이면 명사만을 가지고 검색하는 전략을 택한다. 아닐 때는 다른 어절도 고려한다.
        return nnCnt >= TOKEN_MAX_SIZE ?
            titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build() :
            considerEtcToken(nnCnt, etcCnt, titleQueryBuilder);
    }

    private TitleQuery considerEtcToken(int nnCnt, int etcCnt,
        TitleQueryBuilder titleQueryBuilder) {

        // 명사를 제외한 토큰들의 갯수가 최소를 넘으면
        return etcCnt >= TOKEN_MIN_SIZE ?
            considerNnToken(nnCnt, titleQueryBuilder) :
            decideNnSearchMode(nnCnt, titleQueryBuilder);
    }


    private TitleQuery considerNnToken(int nnCnt, TitleQueryBuilder titleQueryBuilder) {

        String userQuery = titleQueryBuilder.getUserQuery();

        return nnCnt < TOKEN_MIN_SIZE ?
            // 명사 토큰의 갯수가 최소값을 넘지 못하면 검색어를 가지고 전체 검색 모드
            titleQueryBuilder.titleType(TOKEN_ALL_ETC).etcToken(userQuery).build()
            : titleQueryBuilder.titleType(TOKEN_COMPLEX).build();
    }

    // 명사 이외의 토큰이 최소값을 못 넘으면 명사만 가지고 검색하는 전략을 택한다
    // 명사 토큰을 검색할 모드를 결정 한다( boolean or natural)
    private TitleQuery decideNnSearchMode(int nnCnt,
        TitleQueryBuilder titleQueryBuilder) {

        String userQuery = titleQueryBuilder.getUserQuery();

        if (isNotNnTokenEqualUserQuery(titleQueryBuilder)) {
            return titleQueryBuilder.titleType(TOKEN_COMPLEX).etcToken(userQuery).build();
        }

        return nnCnt == TOKEN_MIN_SIZE ?
            titleQueryBuilder.titleType(TOKEN_ONE).build()
            : titleQueryBuilder.titleType(TOKEN_TWO_OR_MORE).build();
    }

    private boolean isNotNnTokenEqualUserQuery(TitleQueryBuilder builder) {
        return !builder.getUserQuery().equals(builder.getNnToken());
    }

}
