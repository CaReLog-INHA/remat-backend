package com.remat.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Region {
    // 수도권
    SEOUL("Seoul", "서울특별시"),
    INCHEON("Incheon", "인천광역시"),
    GYEONGGI("Gyeonggi", "경기도"),

    // 광역시 / 세종
    BUSAN("Busan", "부산광역시"),
    DAEGU("Daegu", "대구광역시"),
    GWANGJU("Gwangju", "광주광역시"),
    DAEJEON("Daejeon", "대전광역시"),
    ULSAN("Ulsan", "울산광역시"),
    SEJONG("Sejong", "세종특별자치시"),

    // 지방
    GANGWON("Gangwon", "강원도"),
    CHUNGBUK("Chungbuk", "충청북도"),
    CHUNGNAM("Chungnam", "충청남도"),
    JEONBUK("Jeonbuk", "전라북도"),
    JEONNAM("Jeonnam", "전라남도"),
    GYEONGBUK("Gyeongbuk", "경상북도"),
    GYEONGNAM("Gyeongnam", "경상남도"),
    JEJU("Jeju", "제주특별자치도");

    private final String englishName;
    private final String koreanName;

}
