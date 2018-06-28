package com.example.hp.minted2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
    private Button button1,button2, button3, button4, button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button1 =(Button) findViewById(R.id.lay2button1id);
        button2 =(Button) findViewById(R.id.lay2button2id);
        button3 =(Button) findViewById(R.id.lay2button3id);
        button4 =(Button) findViewById(R.id.lay2button4id);
        button5 =(Button) findViewById(R.id.lay2button5id);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,CSE.class);
                startActivity(intent);
            }
        });

    }
}
