package com.axiom.updater.processing;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
public interface Change<T> {
    void apply (T obj);

}
