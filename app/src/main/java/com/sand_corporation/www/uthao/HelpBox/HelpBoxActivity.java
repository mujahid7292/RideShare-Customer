package com.sand_corporation.www.uthao.HelpBox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sand_corporation.www.uthao.R;

public class HelpBoxActivity extends AppCompatActivity {

    private ImageView ic_back_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_box);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
