package com.axiom.ui.uiConstructor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theun on 27.02.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UIConstructor {
    String[] args();
}
