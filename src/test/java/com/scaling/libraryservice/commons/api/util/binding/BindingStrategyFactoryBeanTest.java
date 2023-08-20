package com.scaling.libraryservice.commons.api.util.binding;

import static org.junit.jupiter.api.Assertions.*;

import com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider;

class BindingStrategyFactoryBeanTest {

    public static void main(String[] args) throws ClassNotFoundException {


        Class<?> kakaoBookProvider = Class.forName("com.scaling.libraryservice.commons.api.service.provider.KakaoBookProvider");

        System.out.println(kakaoBookProvider);

    }

}