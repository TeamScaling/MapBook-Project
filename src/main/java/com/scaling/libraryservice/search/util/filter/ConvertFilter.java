package com.scaling.libraryservice.search.util.filter;

import com.scaling.libraryservice.search.util.converter.EngToKorConverter;
import java.util.Arrays;
import java.util.StringJoiner;
import org.bitbucket.eunjeon.seunjeon.Analyzer;
import org.bitbucket.eunjeon.seunjeon.LNode;

public class ConvertFilter extends AbstractTileFilter implements TitleFilter{
    private final TitleFilter nextFilter;

    public ConvertFilter(TitleFilter nextFilter) {
        this.nextFilter = nextFilter;
    }


    @Override
    public String filtering(String query){

        StringJoiner joiner = new StringJoiner(" ");

        Arrays.stream(query.split(" ")).forEach(

            splitToken -> {
                String convertedWord = EngToKorConverter.convert(splitToken);

                Analyzer.parseJava(convertedWord).forEach(
                    node -> {
                        if (isNotQualifiedToken(node)){

                            joiner.add(splitToken);
                        }else{
                            joiner.add(node.copy$default$1().surface());
                        }
                    }
                );
            }
        );

        return progressFilter(joiner.toString(),this.nextFilter);
    }

    private boolean isNotQualifiedToken(LNode node){
        return node.copy$default$1().feature().head().equals("UNKNOWN") ||
            node.copy$default$1().feature().head().equals("SP");
    }


}
