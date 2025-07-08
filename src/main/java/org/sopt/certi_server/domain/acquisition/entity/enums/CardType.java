package org.sopt.certi_server.domain.acquisition.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@RequiredArgsConstructor
public enum CardType {

    FIRST("", ""), SECOND("", ""), THIRD("", ""), FOURTH("", "");

    private final String cardFrontImageUrl;
    private final String cardBackImageUrl;

    private static final CardType[] VALUES = values();

    public static CardType issueRandomCardType(){
        int index = ThreadLocalRandom.current().nextInt(VALUES.length);
        return VALUES[index];
    }
}
