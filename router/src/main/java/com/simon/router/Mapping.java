package com.simon.router;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by sunmeng on 16/8/15.
 */
class Mapping<T> {

    protected final Uri matcher;
    private final T target;

    Mapping(Uri matcher, T target) {
        this.matcher = matcher;
        this.target = target;
    }

    T getTarget() {
        return target;
    }

    boolean match(Uri uri) {
        if (uri == null) {
            return false;
        }
        if (!TextUtils.equals(matcher.getScheme(), uri.getScheme())) {
            return false;
        }
        if (!TextUtils.equals(matcher.getHost(), uri.getHost())) {
            return false;
        }
        if (!TextUtils.equals(matcher.getPath(), uri.getPath())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Mapping
                && match(((Mapping) obj).matcher);
    }

    @Override
    public String toString() {
        return String.format("%s->%s", matcher.toString(), target);
    }
}
