package com.axiom.updater.filtering;

import java.util.Collection;

/**
 * Created by kkuznetsov on 03.04.2017.
 */
public interface Filter <B> {
    Collection<B> filter();
}
