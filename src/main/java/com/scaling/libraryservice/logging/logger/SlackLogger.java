package com.scaling.libraryservice.logging.logger;

public interface
SlackLogger<V> {

    void sendLogToSlack(V value);

}
