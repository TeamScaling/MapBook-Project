package com.scaling.libraryservice.service;

import com.scaling.libraryservice.dto.BookApiDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.repository.LibraryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookOpenApiService {

    @Value("${data4.bookExist.api.url}")
    private String API_URL;

    @Value("${data4.bookExist.api.authKey}")
    private String AUTH_KEY;

    @Value("${data4.bookExist.api.format}")
    private String RESP_FORMAT;


    private final LibraryRepository libraryRepo;

    // 오픈API에 보내는 요청을 병렬 처리하여 속도를 올린다.
    @Transactional(readOnly = true)
    public List<RespBookMapDto> getMarkerData(String isbn, String area) {

        List<Library> libraryList = libraryRepo.findLibInfo(area);

        ExecutorService service = Executors.newFixedThreadPool(10);

        List<Callable<RespBookMapDto>> tasks = new ArrayList<>();

        for (Library l : libraryList) {

        }

        tasks.add(new Callable<RespBookMapDto>() {
            @Override
            public RespBookMapDto call() {

                BookApiDto dto = sendQuery(isbn, String.valueOf(141628));

                return new RespBookMapDto(dto,  libraryList.get(0));
            }
        });


        List<Future<RespBookMapDto>> futures = null;

        try {
            futures = service.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }

        List<RespBookMapDto> result = new ArrayList<>();

        for (Future<RespBookMapDto> f : futures) {
            try {
                result.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                log.info(e.getMessage());
            }
        }

        service.shutdown();

        return result;
    }

    public List<BookApiDto> queryExistLocation(String isbn, String area) {

        List<Integer> libList
            = libraryRepo.findLibInfo(area).stream().map(Library::getLibNo).toList();

        List<BookApiDto> result = new ArrayList<>();

        for (Integer i : libList) {

            BookApiDto dto = sendQuery(isbn, String.valueOf(i));

            result.add(dto);
        }

        return result;
    }


    public BookApiDto sendQuery(String isbn, String libCode) {

        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        String body = "";

        UriComponentsBuilder builder
            = UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey",
                AUTH_KEY)
            .queryParam("libCode", libCode)
            .queryParam("isbn13", isbn)
            .queryParam("format", RESP_FORMAT);

        ResponseEntity<String> res
            = template.exchange(builder.toUriString(),
            HttpMethod.GET, new HttpEntity<>(body, headers), String.class);

        return jsonToDto(res.getBody());
    }


    private BookApiDto jsonToDto(String res) {

        JSONObject jsonObj = new JSONObject(res);

        JSONObject req
            = jsonObj.getJSONObject("response").getJSONObject("request");

        JSONObject result
            = jsonObj.getJSONObject("response").getJSONObject("result");

        return new BookApiDto(req, result);
    }

}
