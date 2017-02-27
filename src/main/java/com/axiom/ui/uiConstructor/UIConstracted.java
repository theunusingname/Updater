package com.axiom.ui.uiConstructor;

import java.lang.reflect.Constructor;

/**
 * Created by theun on 27.02.2017.
 */
public interface UIConstracted<C> {
    Constructor<C> getConstructor() throws NoSuchMethodException;
}
