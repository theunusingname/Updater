package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkuznetsov on 3/24/2017.
 */
@UIConstructedClass
public class DeleteAndUpChildsInParent extends DocumentChange {

    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"attrName", "attrValue"})
    public DeleteAndUpChildsInParent(String attrName, String attrValue) {

        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public void apply(Document doc) {

        List<Node> nodes = DocumentChange.getFilteredByAttr(documentWalker(doc.getChildNodes()), attrName, attrValue);

        nodes.forEach(node ->{
            NodeList childs = node.getChildNodes();
            List <Node> nodesToLift = new ArrayList<>();
            for (int i = 0; i < childs.getLength(); i++){
                nodesToLift.add(childs.item(i));
            }
            nodesToLift.forEach(child -> {
                node.removeChild(child);
                node.getParentNode().appendChild(child);
            });
            node.getParentNode().removeChild(node);
        });


    }
}
