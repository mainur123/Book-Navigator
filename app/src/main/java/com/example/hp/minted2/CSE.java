package com.example.hp.minted2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class CSE extends AppCompatActivity {

    private ListView listview;
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cse);

        listview = (ListView) findViewById(R.id.listViewId);
        searchView = findViewById(R.id.searchViewId);

        String[] countryNames = getResources().getStringArray(R.array.book_names);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CSE.this,R.layout.sample_view1,R.id.textViewId,countryNames);
        listview.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent myintent = new Intent(view.getContext(), CSEop.class);
                    startActivityForResult(myintent, 0);
                }
            }
        });
    }
}
