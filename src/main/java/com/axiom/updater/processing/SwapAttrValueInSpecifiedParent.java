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
public class SwapAttrValueInSpecifiedParent extends DocumentChange {

    private final String parentAttrName;
    private final String parentAttrValue;
    private final String childAttrName;
    private final String old;
    private final String newVal;

    @UIConstructor(args = {"parent attr name", "parent attr value", "child attr name", "child attr old value", "child attr new value"})
    public SwapAttrValueInSpecifiedParent(String parentAttrName, String parentAttrValue, String childAttrName, String old, String newVal) {
        this.parentAttrName = parentAttrName;
        this.parentAttrValue = parentAttrValue;
        this.childAttrName = childAttrName;
        this.old = old;
        this.newVal = newVal;
    }


    @Override
    public void apply(Document doc) {

        List<Node> nodes = super.documentWalker(doc.getChildNodes());
        nodes.stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(parentAttrName) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(parentAttrName)
                        .getNodeValue()
                        .equals(parentAttrValue))
                .flatMap(this::childesWithAttributes)
                .forEach(node ->
                {

                    Node item = node.getAttributes().getNamedItem(childAttrName);
                    if (item != null && item.getNodeValue().equals(old)) {
                        item.setNodeValue(newVal);
                    }

                });
    }
}
