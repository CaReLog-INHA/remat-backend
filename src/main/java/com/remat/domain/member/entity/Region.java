package com.remat.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Region {
    // 특별시
    SEOUL("Seoul", "서울특별시"),

    // 광역시
    BUSAN("Busan", "부산광역시"),
    DAEGU("Daegu", "대구광역시"),
    INCHEON("Incheon", "인천광역시"),
    GWANGJU("Gwangju", "광주광역시"),
    DAEJEON("Daejeon", "대전광역시"),
    ULSAN("Ulsan", "울산광역시"),

    // 특별자치시
    SEJONG("Sejong", "세종특별자치시"),

    // 도 지역 주요 시
    SUWON("Suwon", "수원시"),
    SEONGNAM("Seongnam", "성남시"),
    GOYANG("Goyang", "고양시"),
    YONGIN("Yongin", "용인시"),
    BUCHEON("Bucheon", "부천시"),
    ANSAN("Ansan", "안산시"),
    CHEONGJU("Cheongju", "청주시"),
    JEONJU("Jeonju", "전주시"),
    ANYANG("Anyang", "안양시"),
    POHANG("Pohang", "포항시"),
    CHANGWON("Changwon", "창원시"),
    GIMHAE("Gimhae", "김해시"),
    JINJU("Jinju", "진주시"),
    CHEONAN("Cheonan", "천안시"),
    ASAN("Asan", "아산시"),
    GUMI("Gumi", "구미시"),
    GYEONGSAN("Gyeongsan", "경산시"),
    NAMYANGJU("Namyangju", "남양주시"),
    HWASEONG("Hwaseong", "화성시"),
    PYEONGTAEK("Pyeongtaek", "평택시"),
    GIMPO("Gimpo", "김포시"),
    PAJU("Paju", "파주시"),
    UIJEONGBU("Uijeongbu", "의정부시"),
    SIHEUNG("Siheung", "시흥시"),
    GWANGMYEONG("Gwangmyeong", "광명시"),
    HANAM("Hanam", "하남시"),
    ICHEON("Icheon", "이천시"),
    YANGJU("Yangju", "양주시"),
    OSAN("Osan", "오산시"),
    GUNPO("Gunpo", "군포시"),
    GWACHEON("Gwacheon", "과천시"),
    GURI("Guri", "구리시"),
    POCHEON("Pocheon", "포천시"),
    YANGPYEONG("Yangpyeong", "양평군"),
    YEOJU("Yeoju", "여주시"),
    CHUNCHEON("Chuncheon", "춘천시"),
    WONJU("Wonju", "원주시"),
    GANGNEUNG("Gangneung", "강릉시"),
    SOKCHO("Sokcho", "속초시"),
    JEJU("Jeju", "제주시"),
    SEOGWIPO("Seogwipo", "서귀포시");

    private final String englishName;
    private final String koreanName;

    public static Region fromKoreanName(String koreanName) {
        for (Region region : values()) {
            if (region.getKoreanName().equals(koreanName)) {
                return region;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 지역입니다: " + koreanName);
    }

    public static Region fromEnglishName(String englishName) {
        for (Region region : values()) {
            if (region.getEnglishName().equalsIgnoreCase(englishName)) {
                return region;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 지역입니다: " + englishName);
    }
}
