package com.axiom.ui.uiConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by theun on 27.02.2017.
 */
public class AttributedConstructor {
  private final Constructor constructor;
  private final String[] args;

  public AttributedConstructor(Constructor constructor, String[] args) {
    this.constructor = constructor;
    this.args = args;
  }

  public Object invokeConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(args);
  }
}
