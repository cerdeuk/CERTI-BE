package org.sopt.certi_server.domain.acquisition.entity.enums;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmallCardType {
	FIRST("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dskyblue-3.png",  0),
	SECOND("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dyellow-3.png",  1),
	THIRD("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dblue-3.png",  2),
	FOURTH("https://sopt-certi-bucket.s3.ap-northeast-2.amazonaws.com/certi/color%3Dwhite-3.png", 3);

	public static final int CARD_TOTAL = 4;
	private static final SmallCardType[] VALUES = values();
	private final String cardFrontImageUrl;
	private final int index;

	public static SmallCardType issueRandomSmallCardType() {
		int index = ThreadLocalRandom.current().nextInt(VALUES.length);
		return VALUES[index];
	}
	public static SmallCardType issueNextSmallCardType(int index) {
		return VALUES[(index + 1) % CARD_TOTAL];
	}

}
