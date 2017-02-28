package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;



/**
 * Created by kkuznetsov on 2/13/2017.
 */
@UIConstructedClass
public class DeleteByAttr extends DocumentChange {

    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"attrName", "attrValue"})
    public DeleteByAttr(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public void apply(Document doc) {
        List<Node> nodes = super.documentWalker(doc.getChildNodes());
        List<Node> toDeleteNodes = nodes
                .stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(attrName) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(attrName)
                        .getNodeValue()
                        .equals(attrValue))
                .collect(Collectors.toList());
        for (Node node : toDeleteNodes){
            System.out.println("delete node with attr: " + attrValue);
            node.getParentNode().removeChild(node);
        }
    }


}
