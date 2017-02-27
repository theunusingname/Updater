package com.axiom.updater.processing;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
public class DeleteByAttributeWithChild extends DocumentChange {

    private final String parentAttrName;
    private final String parentAttrValue;
    private final String childAttrName;
    private final String childAttrVlue;

    public DeleteByAttributeWithChild(String parentAttrName, String parentAttrValue, String childAttrName, String childAttrVlue) {
        this.parentAttrName = parentAttrName;
        this.parentAttrValue = parentAttrValue;
        this.childAttrName = childAttrName;
        this.childAttrVlue = childAttrVlue;
    }


    @Override
    public void apply(Document doc) {

        List<Node> nodes = super.documentWalker(doc.getChildNodes());
        List<Node> toDeleteNodes = nodes
                .stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(parentAttrName) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(parentAttrName)
                        .getNodeValue()
                        .equals(parentAttrValue))
                .flatMap(this::childesWithAttributes)
                .filter(node ->
                {

                    Node item = node.getAttributes().getNamedItem(childAttrName);
                    if (item != null) {
                        return item.getNodeValue().equals(childAttrVlue);
                    }
                    return false;
                })
                .collect(Collectors.toList());


        for (Node toDelete : toDeleteNodes) {
            System.out.println("deleting node: " + toDelete.getAttributes().getNamedItem(childAttrName).getNodeValue());
            Node parentToDelete = toDelete.getParentNode();
            parentToDelete.getParentNode().removeChild(parentToDelete);
        }
    }
}
