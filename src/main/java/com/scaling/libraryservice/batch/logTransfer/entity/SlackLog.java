package com.scaling.libraryservice.batch.logTransfer.entity;

import com.scaling.libraryservice.mapBook.entity.LibraryMeta;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Entity
@Getter @Setter
@ToString
public class SlackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime logDateTime;

    @ManyToOne
    private Task task;

    private String title;

    private Double taskTime;

    @Nullable
    @ManyToOne
    private LibraryMeta libraryMeta;

    public SlackLog() {
    }

    public SlackLog(Task task, @Nullable LibraryMeta libraryMeta,SlackLogDto slackLogDto) {
        this.logDateTime = slackLogDto.getLogDateTime();
        this.task = task;
        this.title = slackLogDto.getTitle();
        this.taskTime = slackLogDto.getTaskTime();
        this.libraryMeta = libraryMeta;
    }


}
