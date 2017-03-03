package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import javafx.scene.layout.HBox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apishchin on 2/27/2017.
 */
@UIConstructedClass
public class DeleteAndSwapByAttr extends DocumentChange {

    private final String attrNameDel;
    private final String attrValueDel;
    private final String attrName;
    private final String newValue;

    private HBox pane = new HBox();

    @UIConstructor(args = {"attrNameDel", "attrValueDel", "attrName", "newValue"})
    public DeleteAndSwapByAttr(String attrNameDel, String attrValueDel, String attrName, String newValue) {
        this.attrNameDel = attrNameDel;
        this.attrValueDel = attrValueDel;
        this.attrName = attrName;
        this.newValue = newValue;
    }

    private List<Node> filter(List<Node> sourceList, String name, String value) {
        List<Node> filteredList = sourceList
                .stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(name) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(name)
                        .getNodeValue()
                        .equals(value))
                .collect(Collectors.toList());
        return filteredList;
    }

    @Override
    public void apply(Document doc) { // TODO: 3/2/2017 переделать
        List<Node> nodes = documentWalker(doc.getChildNodes());
        List<Node> toDeleteNodes = nodes.stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem("value") != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem("value")
                        .getNodeValue().equals(attrValueDel))
                .collect(Collectors.toList());

        for (Node node : toDeleteNodes) {
            Node parentNode = node.getParentNode();
            parentNode.removeChild(node);
            List<Node> childNodes = super.documentWalker(parentNode.getChildNodes());
            Node delimNode = childNodes.stream()
                    .filter(Node::hasAttributes)
                    .filter(aNode -> aNode.getAttributes().getNamedItem("value") != null)
                    .filter(aNode -> aNode.getAttributes().getNamedItem("value").getNodeValue().equals("\\t"))
                    .findFirst().orElse(null);
            if (delimNode != null) {
                delimNode.getAttributes().getNamedItem("value").setNodeValue(newValue);
            }
        }
    }


}
