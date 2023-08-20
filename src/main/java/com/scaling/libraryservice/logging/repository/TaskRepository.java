package com.scaling.libraryservice.logging.repository;

import com.scaling.libraryservice.batch.logTransfer.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer> {

}
