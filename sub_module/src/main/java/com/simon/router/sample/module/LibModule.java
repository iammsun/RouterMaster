package com.simon.router.sample.module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.simon.router.Navigator;
import com.simon.router.annotations.Call;

/**
 * Created by sunmeng on 2017/2/17.
 */
public class LibModule {

    @Call
    public static void toast(Context context, Bundle extras, long aa) {
        Toast.makeText(context, "simon://app/b", Toast.LENGTH_SHORT).show();
        new Navigator.Builder(context).setExtras(extras).build().open("simon://app/b");
        Navigator.call("p");
    }

    @Call("p")
    public static void print() {
        Log.d("sunmeng", "================");
    }
}
