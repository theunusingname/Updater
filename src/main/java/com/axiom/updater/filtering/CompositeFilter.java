package com.axiom.updater.filtering;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by kkuznetsov on 03.04.2017.
 */
public class CompositeFilter  implements Filter<Node>  {
    private final List<Filter> filters;

    public CompositeFilter(Document doc) {
        this.filters = new ArrayList<>();
    }

    public void add(Filter filter){
        filters.add(filter);
    }

    @Override
    public Collection<Node> filter() {
        List<Node> filtered = new ArrayList<>();
        filters.forEach(filter -> filtered.addAll(filter.filter()));
        return  filtered;
    }
}
