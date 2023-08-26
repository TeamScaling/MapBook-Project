package com.scaling.libraryservice.batch.jobController;

import static com.scaling.libraryservice.commons.api.apiConnection.OpenApi.BATCH_KEY;

import com.scaling.libraryservice.commons.api.service.AuthKeyLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobRestController {
    private final JobExplorer jobExplorer;
    private final JobOperator jobOperator;
    private final String COMMON_PATTERN = "The request to %s the %s was %s";
    private final AuthKeyLoader authKeyLoader;

    @PostMapping("/batch/start")
    public ResponseEntity<String> start(@RequestBody ReqJobDto reqJobDto) {

        return template(reqJobDto,
            "start",
            () -> jobOperator.start(reqJobDto.getJobName(), reqJobDto.getParameters()),
            reqJobDto.getAuthKey()
        );
    }

    @PostMapping("/batch/restart")
    public ResponseEntity<String> reStart(@RequestBody ReqJobDto reqJobDto) {

        JobInstance lastJobInstance = jobExplorer.getLastJobInstance(reqJobDto.getJobName());
        assert lastJobInstance != null;

        return template(reqJobDto,
            "reStart",
            () -> jobOperator.restart(lastJobInstance.getInstanceId()),
            reqJobDto.getAuthKey()
        );
    }


    @PostMapping("/batch/stop")
    public ResponseEntity<String> stop(@RequestBody ReqJobDto reqJobDto) {

        JobInstance lastJobInstance = jobExplorer.getLastJobInstance(reqJobDto.getJobName());
        assert lastJobInstance != null;

        return template(reqJobDto,
            "stop",
            () -> jobOperator.stop(lastJobInstance.getInstanceId()),
            reqJobDto.getAuthKey()
        );
    }

    private ResponseEntity<String> template(ReqJobDto reqJobDto, String request,
        ThrowingSupplier<?> supplier, String authKey) {

        if (!authKeyLoader.checkAuthKey(BATCH_KEY, authKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("You do not have permission to access this.");
        }

        try {
            supplier.get();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(getFailMessage(request, reqJobDto));
        }

        return ResponseEntity.ok(getSuccessMessage(request, reqJobDto));
    }

    private String getSuccessMessage(String request, ReqJobDto reqJobDto) {
        return String.format(COMMON_PATTERN, request, reqJobDto.getJobName(), "successful");
    }

    private String getFailMessage(String request, ReqJobDto reqJobDto) {
        return String.format(COMMON_PATTERN, request, reqJobDto.getJobName(), "failed");
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
