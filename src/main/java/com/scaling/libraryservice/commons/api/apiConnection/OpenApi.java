package com.scaling.libraryservice.commons.api.apiConnection;

public enum OpenApi {

    DATA4_Lib(1),KAKAO_BOOK(2),SLACK_BOT(3);

    private int id;

    OpenApi(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
