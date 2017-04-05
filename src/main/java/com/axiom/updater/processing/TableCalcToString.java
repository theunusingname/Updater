package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kkuznetsov on 03.04.2017.
 */
@UIConstructedClass
public class TableCalcToString extends DocumentChange {

    private final String objType;
    private Document document;

    @UIConstructor(args = {"object type"})
    public TableCalcToString(String objType) {
        this.objType = objType;
    }


    @Override
    public void apply(Document doc) {
        NodeList objects = doc.getElementsByTagName("object");
        for (int i = 0; i < objects.getLength(); i++){
             Node type = objects.item(i).getAttributes().getNamedItem("type");
             if (type.getChildNodes().item(0).getNodeValue().equals( objType)){
                 document = doc;
                 changeCalcFormulas();
             }
        }
    }

    private void changeCalcFormulas(){
        List<Node> nodes = documentWalker(document.getChildNodes());
        List<Node> nodesToChange = getFilteredByAttr(getFilteredByAttr(nodes, "name", "calcFormula"),
                "valueType", "table");

        nodesToChange.stream().forEach(node -> {
            List<Node> nodesWithValue = documentWalker(node.getChildNodes())
                    .stream()
                    .filter(child -> child.getNodeValue()!=null)
                    .collect(Collectors.toList());
            String formula = nodesWithValue.stream().map(Node::getNodeValue).reduce(String::concat).orElse(""); // TODO: 04.04.2017 запилить в каждом вхождении начало с $
            try {
                if (formula.length() > 0) {
                    formula = formula.substring(formula.indexOf('\"'), formula.lastIndexOf("\"")).replace('\"', '$');

                }
            }catch (Exception e){
                System.out.println("cant parse formula in " + document.getDocumentURI());
            }

            node.getAttributes().getNamedItem("valueType").setNodeValue("string");
            node.getAttributes().setNamedItem(constructAttrNode(document, "value", formula));
            try {
                documentWalker(node.getChildNodes()).forEach(node::removeChild);
            }catch (DOMException e){
                if (e.code != DOMException.NOT_FOUND_ERR){
                    throw e;
                }
            }
        });

    }
}
