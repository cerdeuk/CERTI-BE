package org.sopt.certi_server.domain.userprecertification.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.certi_server.domain.acquisition.entity.enums.CardType;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Getter
public enum IconType {

    FIRST("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/serti_emoji-2.png", 0),
    SECOND("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/serti_emoji-3.png", 1),
    THIRD("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/serti_emoji.png", 2);

    private final String iconImageUrl;
    private final int index;

    private static final IconType[] VALUES = values();
    public static final int ICON_TOTAL = 3;


    public static IconType issueRandomIconType(){
        int index = ThreadLocalRandom.current().nextInt(VALUES.length);
        return VALUES[index];
    }

    public static IconType issueNextIconType(int index){
        return VALUES[(index + 1) % ICON_TOTAL];
    }
}
