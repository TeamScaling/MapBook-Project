package com.scaling.libraryservice.logging.vo;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class SlackLogVo {

    private String bot_id;
    private String type;
    private String text;

    public SlackLogVo() {
    }

    public SlackLogVo(String bot_id, String type, String text) {
        this.bot_id = bot_id;
        this.type = type;
        this.text = text;
    }


}
