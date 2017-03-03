package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;
import java.util.UUID;

/**
 * Created by kkuznetsov on 3/3/2017.
 */

@UIConstructedClass
public class temp extends DocumentChange {

    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"parentNodeAttrName", "parentNodeAttrValue"})
    public temp(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    @Override
    public void apply(Document doc) {
        List<Node> nodes = documentWalker(doc.getChildNodes());
        DocumentChange.getFilteredByAttr(nodes, attrName, attrValue)
                .forEach(node -> {
                    Node attrName = DocumentChange.constructAttrNode(doc, "type", "LoadSource:loaderNonKeyParameters");
                    Node attrValue = DocumentChange.constructAttrNode(doc, "version", "[1.0]");
                    DocumentChange.addNodes(node, DocumentChange.constructNode(doc, "object", attrName, attrValue));
                });
    }

}

