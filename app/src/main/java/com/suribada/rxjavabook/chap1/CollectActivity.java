package com.suribada.rxjavabook.chap1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.suribada.rxjavabook.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by lia on 2018-03-12.
 */

public class CollectActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
    }

    public void onClickButton1(View view) {
        List<SearchOption> searchOptions = Arrays.asList(new SearchOption("distance", true),
                new SearchOption("recent", false), new SearchOption("price", false),
                new SearchOption("brand", true));
        List<String> applySearchOptions = new ArrayList<>();
        Observable<String> obs = Observable.fromIterable(searchOptions)
                .filter(SearchOption::isInUse)
                .map(SearchOption::getName);
        obs.subscribe(name -> { // (1) 시작
                    applySearchOptions.add(name); // (2)
                }, e -> {
                }, () -> {
                    showData(applySearchOptions); // (3)
                }); // (1) 끝

        obs.subscribe(name -> {
            applySearchOptions.add(name);
        }, e -> {
        }, () -> {
            showData(applySearchOptions);
        });

    }

    private void showData(List<String> applySearchOptions) {
        StringBuilder sb = new StringBuilder();
        for (String each : applySearchOptions) {
            sb.append(each + "|");
        }
        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickButton2(View view) {
        List<SearchOption> searchOptions = Arrays.asList(new SearchOption("distance", true),
                new SearchOption("recent", false), new SearchOption("price", false),
                new SearchOption("brand", true));
        Single<ArrayList<String>> obs = Observable.fromIterable(searchOptions)
                .filter(SearchOption::isInUse)
                .map(SearchOption::getName)
                .collect(ArrayList<String>::new, List::add); // (1)
        obs.subscribe(this::showData);
        obs.subscribe(this::showData);
    }

    public void onClickButton3(View view) {
        List<SearchOption> searchOptions = Arrays.asList(new SearchOption("distance", true),
                new SearchOption("recent", false), new SearchOption("price", false),
                new SearchOption("brand", true));
        Single<ArrayList<String>> obs = Observable.fromIterable(searchOptions)
                .filter(SearchOption::isInUse)
                .map(SearchOption::getName)
                .collect(ArrayList<String>::new, List::add); // (1)
        obs.subscribe(this::showData);
        obs.subscribe(this::showData);
    }

    class SearchOption {
        String name;
        boolean isInUse;

        SearchOption(String name, boolean isInUse) {
            this.name = name;
            this.isInUse = isInUse;
        }

        public String getName() {
            return name;
        }

        boolean isInUse() {
            return isInUse;
        }

        void setInUse(boolean isInUse) {
            this.isInUse = isInUse;
        }
    }

}
