package org.sopt.certi_server.domain.acquisition.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
public enum CardType {

    FIRST("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dblue.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/Property+1%3D1.png",0),
    SECOND("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dskyblue.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/Property+1%3D2.png",1),
    THIRD("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dwhite.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/Property+1%3D3.png",2),
    FOURTH("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dyellow.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/Property+1%3D4.png",3);

    private final String cardFrontImageUrl;
    private final String cardBackImageUrl;
    private final int index;

    private static final CardType[] VALUES = values();
    public static final int CARD_TOTAL = 4;

    public static CardType issueRandomCardType(){
        int index = ThreadLocalRandom.current().nextInt(VALUES.length);
        return VALUES[index];
    }

    public static CardType issueNextCardType(int index){
        return VALUES[(index + 1) % CARD_TOTAL];
    }
}
