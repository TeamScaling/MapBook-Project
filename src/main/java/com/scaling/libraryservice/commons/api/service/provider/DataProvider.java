package com.scaling.libraryservice.commons.api.service.provider;

import com.scaling.libraryservice.commons.api.apiConnection.ApiConnection;
import java.util.List;

/**
 * 다양한 소스에서 데이터를 제공하는 인터페이스입니다.
 * <p>
 * 이 인터페이스는 특정 데이터 소스에 접근하고, 그 소스에서 데이터를 가져오는 역할을 합니다.
 * 구현 클래스는 특정 데이터 연결 정보를 입력으로 받아, 해당 데이터 소스에서 데이터를 가져와야 합니다.
 * </p>
 *
 * @param <T> 이 인터페이스를 통해 제공될 데이터의 타입
 */
public interface DataProvider<T> {

    /**
     * 주어진 데이터 연결 정보를 사용하여 데이터를 가져오는 메소드입니다.
     * <p>
     * 이 메소드는 특정 데이터 연결 정보와 작업을 수행할 스레드의 개수를 인자로 받아, 연결 정보에 해당하는 데이터 소스에서 데이터를 가져옵니다.
     * </p>
     *
     * @param connections 데이터를 가져올 소스에 접근하기 위한 연결 정보
     * @param nThreads 작업을 수행할 스레드의 개수
     * @return 데이터 소스에서 가져온 데이터 리스트
     */
    List<T> provideDataList(List<? extends ApiConnection> connections,int nThreads);

}
