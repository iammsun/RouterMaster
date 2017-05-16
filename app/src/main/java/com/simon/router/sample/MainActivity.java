package com.simon.router.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.simon.router.Navigator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = Navigator.newInstance("simon://app/fragment/b");
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


}
