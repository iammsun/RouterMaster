package com.simon.router.sample.module;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.simon.router.Navigator;
import com.simon.router.annotations.Nav;

/**
 * Created by sunmeng on 2017/4/26.
 */
@Nav({"simon://app/fragment/b"})
public class ModuleFragment extends Fragment implements View.OnClickListener {

    private WebViewClient client;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (!isOverrideUrl(uri)) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage(getActivity().getPackageName());
                startActivity(intent);
                return true;
            }
        };

        webView = (WebView) view.findViewById(R.id.webview);
        webView.setWebViewClient(client);
        webView.loadUrl("file:///android_asset/index.html");
        view.findViewById(R.id.btn_a).setOnClickListener(this);
        view.findViewById(R.id.btn_b).setOnClickListener(this);
        view.findViewById(R.id.btn_c).setOnClickListener(this);
        view.findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(v);
            }
        });
    }


    private static boolean isOverrideUrl(Uri uri) {
        return !"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())
                && !"ftp".equals(uri.getScheme());
    }

//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    public void onClick(View view) {
        new Navigator.Builder(this).build().open(((Button) view).getText().toString());
    }

    public void toast(View view) {
        Navigator.call("toast", getActivity(), null, 11);
    }
}
