package com.axiom.updater.processing;


import com.sun.istack.internal.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
public abstract class DocumentChange implements Change {

    public static List<Node> documentWalker(NodeList nodeList) {
        ArrayList<Node> accu = new ArrayList<>();
        if (nodeList.getLength() != 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                accu.add(nodeList.item(i));
                accu.addAll(documentWalker(nodeList.item(i).getChildNodes()));
            }
        }
        return accu;
    }

    public Stream<Node> childesWithAttributes(Node node) {
        List<Node> result = new ArrayList<>();
        NodeList childs = node.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            if (childs.item(i).hasAttributes()) {
                result.add(childs.item(i));
            }
        }
        return result.stream();
    }


    public static void addNodes(Node destination, Node ... nodes){
        Arrays.stream(nodes)
                .forEach(destination::appendChild);
    }

    public static Node constructNode(Document document, String elementName, @Nullable Node ... attrs){
       Node node = document.createElement(elementName);

       if (attrs != null) {
           for (Node attr : attrs) {
               node.getAttributes().setNamedItem(attr);
           }
       }
       return node;
    }

    public static Node constructAttrNode(Document document, String name, String value){
        Node node = document.createAttribute(name);
        node.setNodeValue(value);
        return node;
    }

    public static List<Node> getFilteredByAttr(List<Node> nodes, String parentNodeAttrName, String parentNodeAttrValue) {
        return nodes
                .stream()
                .filter(Node::hasAttributes)
                .filter(node -> node.getAttributes().getNamedItem(parentNodeAttrName) != null)
                .filter(node -> node
                        .getAttributes()
                        .getNamedItem(parentNodeAttrName)
                        .getNodeValue()
                        .equals(parentNodeAttrValue))
                .collect(Collectors.toList());
    }
}
