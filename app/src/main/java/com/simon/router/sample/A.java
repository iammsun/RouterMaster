package com.simon.router.sample;

import android.os.Bundle;

import com.simon.router.annotations.Nav;

@Nav({"simon://app/a", "//app/a"})
public class A extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
