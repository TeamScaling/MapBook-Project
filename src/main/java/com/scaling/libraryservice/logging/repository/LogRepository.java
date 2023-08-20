package com.scaling.libraryservice.logging.repository;

import com.scaling.libraryservice.batch.logTransfer.entity.SlackLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<SlackLog,Long> {

}
