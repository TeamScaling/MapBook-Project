package com.scaling.libraryservice.search.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

@Slf4j
public class CacheProxy<T,E> implements MethodInterceptor {

    private Cache<T, E> anyCache;

    public CacheProxy(Cache<T, E> anyCache) {
        this.anyCache = anyCache;
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {

        E obj = (E)invocation.proceed();

        log.info("캐싱 프록시 테스트 중~~"+obj);

        return obj;
    }




}
