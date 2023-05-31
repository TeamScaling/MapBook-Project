package com.scaling.libraryservice.commons.api.apiConnection;

import com.scaling.libraryservice.commons.api.entity.AuthKey;
import com.scaling.libraryservice.commons.api.apiConnection.OpenApi;
import com.scaling.libraryservice.commons.api.repository.AuthKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthKeyLoader {

    private final AuthKeyRepository authKeyRepo;

    public AuthKey loadAuthKey(OpenApi openApi){
        return authKeyRepo.findById(openApi.getId()).orElseThrow(IllegalArgumentException::new);
    }

}
