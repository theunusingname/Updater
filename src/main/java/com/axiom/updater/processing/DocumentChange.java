package com.axiom.updater.processing;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
public abstract class DocumentChange implements Change {

    protected static List<Node> documentWalker(NodeList nodeList) {
        ArrayList<Node> accu = new ArrayList<>();
        if (nodeList.getLength() != 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                accu.add(nodeList.item(i));
                accu.addAll(documentWalker(nodeList.item(i).getChildNodes()));
            }
        }
        return accu;
    }

    protected Stream<Node> childesWithAttributes(Node node) {
        List<Node> result = new ArrayList<>();
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i).hasAttributes()) {
                result.add(childs.item(i));
            }
        }
        Package pac = this.getClass().getPackage();
        return result.stream();
    }
}
