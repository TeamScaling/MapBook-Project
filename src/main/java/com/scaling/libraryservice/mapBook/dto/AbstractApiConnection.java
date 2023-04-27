package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import org.joda.time.DateTime;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class AbstractApiConnection implements ConfigureUriBuilder {

    private static boolean apiAvailable = true;

    private static Integer errorCnt = 0;

    private static DateTime closedTime = null;

    public abstract UriComponentsBuilder configUriBuilder(String target);

    public boolean isApiAvailable() {
        return apiAvailable;
    }

    public void setApiAvailable(boolean apiAvailable) {
        apiAvailable = apiAvailable;
    }

    public static Integer getErrorCnt() {
        return errorCnt;
    }

    public void upErrorCnt() {
        ++errorCnt;
    }

    public DateTime getClosedTime() {
        return closedTime;
    }

    public void setClosedTime(DateTime closedTime) {
        closedTime = closedTime;
    }
}
