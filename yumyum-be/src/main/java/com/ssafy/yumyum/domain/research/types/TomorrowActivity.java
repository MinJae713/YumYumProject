package com.ssafy.yumyum.domain.research.types;

// 내일 활동량
public enum TomorrowActivity {
    LOW(0),    // 없음(집콕)
    NORMAL(5), // 평소
    HIGH(10)  ;  // 활동적

    private final int score;

    TomorrowActivity(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }

    public static TomorrowActivity fromScore(int score) {
        for (TomorrowActivity type : values()) {
            if (type.score == score) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown score: " + score);
    }

}
