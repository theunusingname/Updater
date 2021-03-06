package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
@UIConstructedClass
public class SwapAttrValue extends DocumentChange {

    private final String name;
    private final String old;
    private final String newVal;

    @UIConstructor(args = {"attr name", "old value", "new value"})
    public SwapAttrValue(String name, String old, String newVal) {
        this.name = name;
        this.old = old;
        this.newVal = newVal;
    }

    @Override
    public void apply(Document doc) {
        List<Node> nodes = super.documentWalker(doc.getChildNodes());
        nodes.stream()
                .filter(Node::hasAttributes)
                .map(Node::getAttributes)
                .map(atrrNodes -> atrrNodes.getNamedItem(name))
                .forEach(node -> {
                    if (node != null && node.getNodeValue().equals(old)) {
                        node.setNodeValue(newVal);
                    }
                });
    }
}
