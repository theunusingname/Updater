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
public class AddIDProp extends DocumentChange {

    private final String parentNodeAttrName;
    private final String parentNodeAttrValue;

    @UIConstructor(args = {"parentNodeAttrName", "parentNodeAttrValue"})
    public AddIDProp(String parentNodeAttrName, String parentNodeAttrValue) {

        this.parentNodeAttrName = parentNodeAttrName;
        this.parentNodeAttrValue = parentNodeAttrValue;
    }


    @Override
    public void apply(Document doc) {
        List<Node> nodes = documentWalker(doc.getChildNodes());
        List<Node> nodesToAddID = getFilteredByAttr(nodes, parentNodeAttrName, parentNodeAttrValue);
        nodesToAddID.stream().parallel()
                .forEach(node -> {
                    Node attrName = DocumentChange.constructAttrNode(doc, "name", "id");
                    Node attrValue = DocumentChange.constructAttrNode(doc, "value", UUID.randomUUID().toString());
                    Node attrValueType = DocumentChange.constructAttrNode(doc, "valueType", "string");
                    DocumentChange.addNodes(node, DocumentChange.constructNode(doc, "property", attrName, attrValue, attrValueType));
                });
    }


}
