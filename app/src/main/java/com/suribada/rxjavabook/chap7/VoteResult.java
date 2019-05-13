package com.suribada.rxjavabook.chap7;

/**
 * 여기서는 굳이 쓸 필요 없음
 */
public class VoteResult {

    int sum;
    float candidate1Percent;
    float candidate2Percent;

    public VoteResult(VoteCount voteCount) {
        this.sum = voteCount.candidate1Vote + voteCount.candidate2Vote;
        this.candidate1Percent = voteCount.candidate1Vote / (float) sum;
        this.candidate2Percent = voteCount.candidate2Vote / (float) sum;
    }

    @Override
    public String toString() {
        return "전체 투표수=" + sum + ", 후보1 득표율=" + candidate1Percent + ", 후보2 득표율" + candidate2Percent;
    }

}
