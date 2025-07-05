package org.sopt.certi_server.domain.acquisition.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardType {

    FIRST("", ""), SECOND("", ""), THIRD("", ""), FOURTH("", "");

    private final String cardFrontImageUrl;
    private final String cardBackImageUrl;

}
