package com.axiom.updater.processing;

import com.axiom.ui.uiConstructor.UIConstructedClass;
import com.axiom.ui.uiConstructor.UIConstructor;
import com.axiom.updater.Updater;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkuznetsov on 3/28/2017.
 */
@UIConstructedClass
public class addFragmentToNodeFromFile extends DocumentChange {

    private final String xmlPath;
    private final String attrName;
    private final String attrValue;

    @UIConstructor(args = {"XML file path", "attrNane", "attrValue"})
    public addFragmentToNodeFromFile(String xmlPath, String attrName, String attrValue) {
        this.xmlPath = xmlPath;
        this.attrName = attrName;
        this.attrValue = attrValue;
    }


    @Override
    public void apply(Document doc) {


            List<Node> nodes = documentWalker(doc.getChildNodes());
            List<Node> nodesToAdd = getFilteredByAttr(nodes, attrName, attrValue);
            if (nodesToAdd.size() > 0) {
                try {
                Document fragment = Updater.getDocument(new File(xmlPath));
                    NodeList fragmentNodes = fragment.getChildNodes();
                    List<Node> fragmentNodesImported = new ArrayList<>();
                    for (int i = 0; i < fragmentNodes.getLength(); i++){
                        fragmentNodesImported.add(doc.importNode(fragmentNodes.item(i), true));
                    }
                nodesToAdd.forEach(node -> fragmentNodesImported.forEach(node::appendChild));

            } catch(IOException | SAXException | ParserConfigurationException e){
                e.printStackTrace();
            }
        }
    }
}
