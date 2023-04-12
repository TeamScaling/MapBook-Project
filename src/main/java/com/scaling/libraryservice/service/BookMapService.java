package com.scaling.libraryservice.service;

import com.scaling.libraryservice.aop.Timer;
import com.scaling.libraryservice.dto.BookApiDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.exception.OpenApiException;
import com.scaling.libraryservice.repository.LibraryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookMapService {

    @Value("${data4.bookExist.api.url}")
    private String openApiUrl;
    @Value("${data4.bookExist.api.authKey}")
    private String authKey;
    @Value("${data4.bookExist.api.format}")
    private String respFormat;
    private final LibraryRepository libraryRepo;
    private final RestTemplate restTemplate;
    private final Map<String, List<Library>> libraryMap = new ConcurrentHashMap<>();


    // consider : 코드 가독성을 위해 CompletableFuture 도입 고려.
    //오픈API에 보내는 요청을 병렬 처리하여 속도를 올린다.
    @Transactional(readOnly = true)
    @Timer
    public List<RespBookMapDto> loanAbleLibrary(String isbn, String area) throws OpenApiException {

        // map 캐싱을 기반한 메소드에서 도서관 위치 정보를 얻는다.
        List<Library> libraryList = getLibraries(area);

        ExecutorService service = Executors.newFixedThreadPool(10);

        List<Callable<RespBookMapDto>> tasks = new ArrayList<>();

        for (Library l : libraryList) {

            tasks.add(() -> {
                BookApiDto dto = sendQuery(isbn, l.getLibNo());
                return new RespBookMapDto(dto, l);
            });
        }

        List<Future<RespBookMapDto>> futures;

        List<RespBookMapDto> result = new ArrayList<>();

        try {
            futures = service.invokeAll(tasks);

            for (Future<RespBookMapDto> f : futures) {
                result.add(f.get());
            }

        } catch (InterruptedException | ExecutionException e) {
            log.info(e.toString());

        } finally {
            service.shutdown();
        }

        return result;
    }

    // map을 이용하여 library에 대한 정보 데이터를 캐싱 한다.
    private List<Library> getLibraries(String area) {

        if (libraryMap.containsKey(area)) {

            log.info("map hit !!");
            return libraryMap.get(area);

        } else {

            List<Library> libraryList = libraryRepo.findLibInfo(area);
            libraryMap.put(area, libraryList);
            log.info("no library in map !!");

            return libraryList;
        }
    }

    // Open API에 요청을 보내고, 응답 받은 json 데이터를 mapToDto 메소드에 보낸다.
    BookApiDto sendQuery(String isbn, int libCode) throws OpenApiException {

        UriComponentsBuilder builder
            = UriComponentsBuilder.fromHttpUrl(openApiUrl)
            .queryParam("authKey", authKey)
            .queryParam("libCode", libCode)
            .queryParam("isbn13", isbn)
            .queryParam("format", respFormat);

        ResponseEntity<String> resp;

        try {
            resp = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, HttpEntity.EMPTY, String.class);

        } catch (RestClientException e) {
            log.error(e.toString());

            throw new OpenApiException(e.toString(), e);
        }

        return mapToDto(resp);
    }

    // 매개 변수로 받은 응답 엔티티 객체에서 원하는 key에 해당하는 value를 추출하여 dto로 mapping 한다.
    private BookApiDto mapToDto(ResponseEntity<String> response) throws OpenApiException {

        Objects.requireNonNull(response);

        JSONObject respJsonObj =
            new JSONObject(response.getBody()).getJSONObject("response");

        if (respJsonObj.has("error")) {
            String error = respJsonObj.getString("error");
            log.info("[data4library bookExist API error] message :" + error);

            throw new OpenApiException(error);
        }

        return new BookApiDto(
            respJsonObj.getJSONObject("request")
            , respJsonObj.getJSONObject("result"));
    }

    // 단일 쓰레드로 Open API에 요청 처리하는 메소드.

    /*public List<RespBookMapDto> loanAbleLibraryCG(String isbn, String area) {

        List<Library> libList
            = libraryRepo.findLibInfo(area);

        List<RespBookMapDto> result = new ArrayList<>();

        for (Library l : libList) {

            BookApiDto dto = sendQuery(isbn, l.getLibNo());

            result.add(new RespBookMapDto(dto, l));
        }

        return result;
    }*/

}
