package com.scaling.libraryservice;

import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.search.util.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LearningTest {

    @Autowired
    private Tokenizer tokenizer;

    @Test
    public void test() {

        ExecutorService service = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " hello");
                }
            });
        }

        service.shutdown();
    }

    @Test
    public void hashSet_List() {

        Map<String, String> map = new HashMap<>();

        var result = map.computeIfAbsent("a", s -> "A");

        System.out.println(result);
        System.out.println(map.get("a"));
    }

    @Test
    public void caffeine_cache() {
        /* given */

        ReqMapBookDto dto = new ReqMapBookDto("12345", 37.3, 124.4, null, null);
        ReqMapBookDto dto2 = new ReqMapBookDto("12345", 37.3, 124.4, null, null);


        /* when */

        System.out.println(dto.equals(dto2));

        /* then */
    }

    @Test
    public void lnp() {

        // Setup Stanford CoreNLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Input text
        String text = "windwos API정복";

        // Annotate text
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        // Extract tokens
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                System.out.println(word);
            }
        }
    }

    @Test
    public void english_korean(){
        String text = "email에 꼭 필요한 알짜표현!!";

        text = text.replaceAll("([a-zA-Z])([가-힣])", "$1 $2");

        Pattern pattern = Pattern.compile("[가-힣]+|\\b\\w+\\b");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    public void tokenizer(){
        /* given */
        String text = "e-mail에 꼭 필요한 알짜표현!!";
        /* when */
        var result= tokenizer.tokenize(text);
        /* then */

        System.out.println(result);
    }


}
