package com.scaling.libraryservice.commons.api.repository;

import com.scaling.libraryservice.commons.api.entity.AuthKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthKeyRepository extends JpaRepository<AuthKey,Integer> {

}
