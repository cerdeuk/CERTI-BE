package org.sopt.certi_server.domain.acquisition.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
public enum CardType {

    FIRST("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D1.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D1-2.png", 0),
    SECOND("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D3.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D3-2.png", 1),
    THIRD("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D2.png", "https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3D2-2.png", 2);

    public static final int CARD_TOTAL = 3;
    private static final CardType[] VALUES = values();
    private final String cardFrontImageUrl;
    private final String cardBackImageUrl;
    private final int index;

    public static CardType issueRandomCardType() {
        int index = ThreadLocalRandom.current().nextInt(VALUES.length);
        return VALUES[index];
    }

    public static CardType issueNextCardType(int index) {
        return VALUES[(index + 1) % CARD_TOTAL];
    }
}
