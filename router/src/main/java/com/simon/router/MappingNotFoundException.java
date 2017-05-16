package com.simon.router;

import android.support.annotation.NonNull;

/**
 * Created by sunmeng on 2017/2/21.
 */

public class MappingNotFoundException extends IllegalStateException {

    public MappingNotFoundException() {
    }

    public MappingNotFoundException(@NonNull Throwable cause) {
        super(cause.getCause());
    }
}
