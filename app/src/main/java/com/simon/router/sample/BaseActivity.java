package com.simon.router.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by sunmeng on 16/8/26.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TextView textView = (TextView) findViewById(R.id.content);
        Bundle bundle = getIntent().getExtras();
        textView.append("===========start==========\n");
        if (bundle != null)
            for (String key : bundle.keySet()) {
                textView.append(key);
                textView.append("=");
                textView.append(bundle.get(key).toString());
                textView.append("\n");
            }
        textView.append("===========end==========");
    }
}
