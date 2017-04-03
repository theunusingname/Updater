package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by kkuznetsov on 3/27/2017.
 */
@UIConstructedClass
public class UpNode extends DocumentChange {

    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"attrName", "attrValue"})
    public UpNode(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }


    @Override
    public void apply(Document doc) {
        List<Node> nodes = DocumentChange.getFilteredByAttr(documentWalker(doc.getChildNodes()), attrName, attrValue);


        nodes.forEach(node -> {

            Node parent = node.getParentNode().getParentNode();
            Node orphan = node.getParentNode().removeChild(node);
            if (parent != null) {
                parent.appendChild(orphan);
            }
        });


    }
}
