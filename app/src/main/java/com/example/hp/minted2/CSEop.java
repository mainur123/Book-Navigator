package com.example.hp.minted2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CSEop extends AppCompatActivity {
    private Button borrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cseop);
        borrow = (Button) findViewById(R.id.CSEopbuttonId) ;
        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSEop.this,return1.class);
                startActivity(intent);
            }
        });
    }
}
