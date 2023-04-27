package com.scaling.libraryservice.mapBook.domain;

import org.joda.time.DateTime;

public interface ApiObservable {

    public Integer getErrorCnt();

    public DateTime getClosedTime();

    public boolean apiAccessible();

    public void closeAccess();

    public void openAccess();

    public void upErrorCnt();

    String getApiUrl();

}
