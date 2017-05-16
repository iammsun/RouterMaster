package com.simon.router;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sunmeng on 16/8/23.
 */
public class RouterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ROUTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        if (data == null) {
            finish();
            return;
        }
        try {
            new Navigator.Builder(this).setExtras(getIntent().getExtras())
                    .build().open(data, REQUEST_CODE_ROUTER);
        } catch (MappingNotFoundException e) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_ROUTER == requestCode) {
            setResult(resultCode, data);
            finish();
        }
    }
}
