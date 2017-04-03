package com.axiom.updater.source;

import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kkuznetsov on 03.04.2017.
 */
public class DocumentSource {

    public static @Nullable
    Document read(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactoryImpl.newInstance();
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        return builder.parse(file);

    }

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

    public static List<Node> getNodesFromFile(File file) {
        try {
            return documentWalker(read(file).getChildNodes());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
