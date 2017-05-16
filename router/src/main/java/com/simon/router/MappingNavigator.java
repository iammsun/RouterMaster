package com.simon.router;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunmeng on 16/8/15.
 */
class MappingNavigator<T> {

    final List<Mapping<T>> MAPPING_LIST = new ArrayList<>();

    void map(String format, T target) {
        Uri uri = Uri.parse(format);
        T old = nav(uri);
        if (old != null) {
            throw new IllegalStateException(
                    String.format("duplicate:\n%s->%s\n%s->%s", format, old, format, target));
        }
        MAPPING_LIST.add(new Mapping<>(uri, target));
    }

    T nav(Uri uri) {
        for (Mapping<T> mapping : MAPPING_LIST) {
            if (mapping.match(uri)) {
                return mapping.getTarget();
            }
        }
        return null;
    }
}
