package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Created by kkuznetsov on 3/3/2017.
 */

@UIConstructedClass
public class DeleteAllChilds extends DocumentChange {

    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"attrName", "attrValue"})
    public DeleteAllChilds(String attrName, String attrValue) {

        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public void apply(Document doc) {
        List<Node> nodes = documentWalker(doc.getChildNodes());
        getFilteredByAttr(nodes, attrName, attrValue)
                .forEach(node -> {
                    NodeList childs = node.getChildNodes();
                    for (int i = 0; i < childs.getLength(); i++) {
                        node.removeChild(childs.item(i));
                    }
                });
    }
}
