package com.scaling.libraryservice.mapBook.service.location;

import com.scaling.libraryservice.mapBook.dto.LibraryInfoDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 사용자의 위치 정보를 바탕으로 가장 가까운 도서관을 찾고, 해당 도서관의 지역 코드를 반환하는 클래스입니다.
 * {@link LocationResolver} 인터페이스를 구현하며, 입력 값으로 {@link ReqMapBookDto}를 받고 출력 값으로 해당 도서관의 지역 코드를 반환합니다.
 *
 * <p>이 클래스는 사용자의 위치 정보와 모든 도서관의 위치 정보를 비교하여 가장 가까운 도서관을 찾는 방식으로 작동합니다.
 * 이를 위해 {@link LibraryRepository}를 통해 모든 도서관의 정보를 로드하며, 이 정보는 {@link LibraryInfoDto}의 리스트 형태로 저장됩니다.
 * 위치 비교는 {@link HaversineCalculater}를 사용하여 계산되며, 가장 가까운 도서관의 지역 코드를 사용자 요청 Dto에 업데이트한 후 반환합니다.</p>
 *
 * <p>{@link LocationResolver#resolve(Object)} 메소드를 오버라이드하여 사용자의 위치 정보를 지역 코드로 변환하는 기능을 제공합니다.</p>
 */
@RequiredArgsConstructor
@Component
public class UserLibraryGeocoder implements LocationResolver<Integer,ReqMapBookDto> {

    private final LibraryRepository libraryRepo;
    private List<LibraryInfoDto> libraries;

    /**
     * 주변 대출 가능 도서관 찾기 요청 Dto에 담긴 위치 정보를 지역 코드로 변환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @throws LocationException 사용자 요청에 담긴 위치 정보가 유효하지 않을 경우.
     */
    @Override
    public Integer resolve(@NonNull ReqMapBookDto reqMapBookDto){

        if(libraries ==null){
            libraries = libraryRepo.findAll().stream().map(LibraryInfoDto::new).toList();
        }

        Integer areaCd = findNearestLibrary(reqMapBookDto).getAreaCd();

        upDateAreaCd(reqMapBookDto,areaCd);

        return areaCd;
    }

    /**
     * 사용자 요청 중 위/경도 데이터를 사용하여 가장 가까운 도서관을 찾아 반환 한다.
     *
     * @param reqMapBookDto 위치 정보를 참고할 사용자 요청 Dto
     * @return 사용자와 가장 가까운 도서관 정보를 담은 Dto
     * @throws LocationException 위/경도 데이터를 가지고 가장 가까운 도서관 찾는데 실패할 경우
     */
    private LibraryInfoDto findNearestLibrary(ReqMapBookDto reqMapBookDto)
        throws LocationException {

        return libraries.stream().min((l1, l2) -> {

                double d1 = HaversineCalculater.calculateDistance(
                    reqMapBookDto.getLat(), reqMapBookDto.getLon(), l1.getLibLat(), l1.getLibLon());

                double d2 = HaversineCalculater.calculateDistance(
                    reqMapBookDto.getLat(), reqMapBookDto.getLon(), l2.getLibLat(), l2.getLibLon());

                return Double.compare(d1, d2);
            }).orElseThrow(() -> new LocationException("최단 거리 도서관 찾기 실패"));
    }


    private void upDateAreaCd(ReqMapBookDto reqMapBookDto,Integer areaCd){

        reqMapBookDto.setAreaCd(areaCd);
    }

}
