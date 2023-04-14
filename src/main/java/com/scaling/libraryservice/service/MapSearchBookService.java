package com.scaling.libraryservice.service;

import com.scaling.libraryservice.dto.BookApiDto;
import com.scaling.libraryservice.dto.RespBookMapDto;
import com.scaling.libraryservice.entity.Library;
import com.scaling.libraryservice.exception.OpenApiException;
import com.scaling.libraryservice.repository.LibraryRepository;
import com.scaling.libraryservice.util.OpenApiQuerySender;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapSearchBookService {

    @Value("data4.bookExist.api.url")
    private String OPEN_API_URL;
    @Value("data4.bookExist.api.authKey")
    private String AUTH_KEY;
    private final LibraryRepository libraryRepo;
    private final Map<String, List<Library>> libraryCache = new ConcurrentHashMap<>();
    private final OpenApiQuerySender querySender;


    // consider : 코드 가독성을 위해 CompletableFuture 도입 고려.
    //오픈API에 보내는 요청을 병렬 처리하여 속도를 올린다.
    @Transactional(readOnly = true)
    public List<RespBookMapDto> loanAbleLibraries(String isbn, String area)
        throws OpenApiException {

        // map 캐싱을 기반한 메소드에서 도서관 위치 정보를 얻는다.
        List<Library> libraryList = getLibraries(area);

        ExecutorService service = Executors.newFixedThreadPool(10);

        List<Callable<RespBookMapDto>> tasks = new ArrayList<>();

        for (Library l : libraryList) {

            tasks.add(() -> {

                Map<String, String> paramMap
                    = createParamMap(isbn, l.getLibNo());

                ResponseEntity<String> resp = querySender
                    .sendParamQuery(paramMap, OPEN_API_URL);


                BookApiDto dto = bindToDto(resp);

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

        if (libraryCache.containsKey(area)) {

            log.info("cache hit !!");
            return libraryCache.get(area);

        } else {

            List<Library> libraryList = libraryRepo.findLibInfo(area);
            libraryCache.put(area, libraryList);
            log.info("no library in cache !!");

            return libraryList;
        }
    }

    private Map<String, String> createParamMap(String isbn, int libCode) {

        Map<String, String> paramMap = new HashMap<>();

        paramMap.put("authKey", AUTH_KEY);
        paramMap.put("format", "json");
        paramMap.put("libCode", String.valueOf(libCode));
        paramMap.put("isbn13", isbn);

        return paramMap;
    }


    // 매개 변수로 받은 응답 엔티티 객체에서 원하는 key에 해당하는 value를 추출하여 dto로 mapping 한다.
    private BookApiDto bindToDto(ResponseEntity<String> response) throws OpenApiException {

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

}
