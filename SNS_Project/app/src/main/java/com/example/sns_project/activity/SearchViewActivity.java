package com.example.sns_project.activity;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.sns_project.R;

import java.util.Arrays;
import java.util.List;


public class SearchViewActivity extends BasicActivity {
    private List<String> items = Arrays.asList("어벤져스", "베트맨", "베트맨2", "배구", "슈퍼맨");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item03_java);

        SearchView searchView = findViewById(R.id.search_view);
        final TextView resultTextView = findViewById(R.id.textView);
        resultTextView.setText(getResult());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resultTextView.setText(search(newText));
                return true;
            }
        });
    }

    private String search(String query) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            if (item.toLowerCase().contains(query.toLowerCase())) {
                sb.append(item);
                if (i <= items.size() - 1) {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    private String getResult() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            sb.append(item);
            if (i <= items.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
