package com.axiom.ui.uiConstructor;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by theun on 27.02.2017.
 */
public class AttributedConstructor implements Externalizable {
  private Constructor constructor;
  private String[] args;

  public AttributedConstructor(){}

  public AttributedConstructor(Constructor constructor, String[] args) {
    this.constructor = constructor;
    this.args = args;
  }

  public Object invokeConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(args);
  }



  @Override
  public void writeExternal(ObjectOutput out) throws IOException {

    Class clazz = null;
    try {
      clazz = invokeConstructor().getClass();
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
    out.writeObject(clazz);
    out.writeObject(args);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    Class clazz = (Class) in.readObject();
    constructor  = Arrays.stream(clazz.getConstructors())
            .filter(predicate -> predicate.isAnnotationPresent(UIConstructor.class))
            .findFirst().orElse(null);
    args = (String[]) in.readObject();
  }
}
