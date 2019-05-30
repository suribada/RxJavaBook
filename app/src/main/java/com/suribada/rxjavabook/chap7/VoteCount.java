package com.suribada.rxjavabook.chap7;

public class VoteCount {
    int candidate1Vote;
    int candidate2Vote;

    public VoteCount(int candidate1Vote, int candidate2Vote) {
        this.candidate1Vote = candidate1Vote;
        this.candidate2Vote = candidate2Vote;
    }

    public void add(VoteCount another) {
        this.candidate1Vote += another.candidate1Vote;
        this.candidate2Vote += another.candidate2Vote;
    }

    @Override
    public String toString() {
        int sum = candidate1Vote + candidate2Vote;
        float candidate1Percent = candidate1Vote / (float) sum;
        float candidate2Percent = candidate2Vote / (float) sum;
        return "전체 투표수=" + sum + ", 후보1 득표율=" + candidate1Percent
                + ", 후보2 득표율=" + candidate2Percent;
    }
}
