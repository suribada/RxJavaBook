package com.suribada.rxjavabook.chap6;

import com.suribada.rxjavabook.chap5.SearchResult;

class SearchDetail {

    private SearchResult searchResult;

    public SearchDetail(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public String toString() {
        return "name=" + searchResult.toString();
    }

}
