package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by kkuznetsov on 05.04.2017.
 */
@UIConstructedClass
public class DeleteInSpecifiedNodeByAttr extends DocumentChange {

    private final String parentAttrName;
    private final String parentAttrValue;
    private final String toDeleteAttrName;
    private final String toDeleteAttrValue;

    @UIConstructor(args = {"parentAttrName", "parentAttrValue", "toDeleteAttrName", "toDeleteAttrValue"})
    public DeleteInSpecifiedNodeByAttr(String parentAttrName, String parentAttrValue, String toDeleteAttrName, String toDeleteAttrValue) {
        this.parentAttrName = parentAttrName;
        this.parentAttrValue = parentAttrValue;
        this.toDeleteAttrName = toDeleteAttrName;
        this.toDeleteAttrValue = toDeleteAttrValue;
    }


    @Override
    public void apply(Document doc) {
        List<Node> nodes = documentWalker(doc.getChildNodes());
        nodes.stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(parentAttrName) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(parentAttrName)
                        .getNodeValue()
                        .equals(parentAttrValue))
                .flatMap(this::childesWithAttributes)
                .forEach(node -> {
                    Node item = node.getAttributes().getNamedItem(toDeleteAttrName);
                    if (item != null && item.getNodeValue().equals(toDeleteAttrValue)) {
                        node.getParentNode().removeChild(node);
                    }
                });

    }
}
