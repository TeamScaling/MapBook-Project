package com.scaling.libraryservice.commons.api.service;

import com.scaling.libraryservice.commons.api.entity.AuthKey;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.repository.AuthKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthKeyLoader {

    private final AuthKeyRepository authKeyRepo;

    // 내부 DB에 있는 API 관련 AuthKey를 반환 한다.
    public AuthKey loadAuthKey(@NonNull OpenApi openApi){

        return authKeyRepo.findById(openApi.getId())
            .orElseThrow(IllegalArgumentException::new);
    }

    public boolean checkAuthKey(OpenApi openApi,String authKey){

        return authKeyRepo.findById(openApi.getId())
            .map(storedKey -> storedKey.getAuthKey().equals(authKey))
            .orElseThrow(IllegalAccessError::new);
    }

}
