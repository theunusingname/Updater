package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstracted;
import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Created by kkuznetsov on 2/13/2017.
 */
@UIConstructedClass
public class DeleteByAttr extends DocumentChange implements UIConstracted<Change> {

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




    @Override
    public Constructor<Change> getConstructor() throws NoSuchMethodException {
        return (Constructor<Change>) Arrays.stream(this.getClass().getDeclaredConstructors()).filter(constructor ->
                Arrays.stream(constructor.getAnnotations())
                        .filter(annotation -> annotation instanceof UIConstructor)
                        .count() > 0)
                .findFirst()
                .orElse(null);
    }
}
