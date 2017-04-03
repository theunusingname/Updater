package com.axiom.updater;

import com.axiom.ui.ProgressWidget;
import com.axiom.updater.processing.Change;
import com.axiom.updater.source.DocumentSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by theun on 10.01.2017.
 */
public class Updater {

    private String defaultProps;

    private boolean swithPropertiesToBp = false;
    private List<Change> changes = new ArrayList<>();
    private List<File> affectedFiles = new ArrayList<>();
    private List<File> xmlFiles = new ArrayList<>();
    private List<File> props = new ArrayList<>();
    private String mainPath;



    public Updater(String path) {
        mainPath = path;
        directoryWalker(new File(mainPath));

        FileFilter xmlFilter = pathname -> pathname.getPath().endsWith(".xml");
        FileFilter propsFilter = pathname -> pathname.getPath().endsWith("default.properties");

        props = affectedFiles.stream().filter(propsFilter::accept).collect(Collectors.toList());
        xmlFiles = affectedFiles.stream().filter(xmlFilter::accept).collect(Collectors.toList());

    }

    public void setSwithPropertiesToBp(boolean swithPropertiesToBp) {
        this.swithPropertiesToBp = swithPropertiesToBp;
    }

    public void setDefaultProps(String defaultProps) {
        if (defaultProps != null)
        this.defaultProps = defaultProps;
    }

    public void addChange(Change change){
        if (change != null)
            changes.add(change);
    }



    public String getCurrentDefaultProperties() throws FileNotFoundException{
        Scanner scanner = new Scanner(props.stream().findFirst().orElse(new File("")));
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()){
            builder.append(scanner.nextLine()+"\n");
        }
        return builder.toString();
    }

    public void update() throws ParserConfigurationException, IOException, SAXException, TransformerException {

        int count = xmlFiles.size() + props.size();


        final ProgressWidget progressWidget = new ProgressWidget(0, count - 2, "Progress");

        if (changes.size() > 0) {
            for (File file : xmlFiles) {

                progressWidget.setTextInfo("updating: " + file.getName());

                Document document = DocumentSource.read(file);

                changes.stream().forEachOrdered(change -> change.apply(document));

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(file);
                Source input = new DOMSource(document);
                transformer.transform(input, output);

                progressWidget.incrementProgress(1);
            }
        } else {
            progressWidget.incrementProgress(xmlFiles.size());
        }

        for (File file : props) {

            progressWidget.setTextInfo("updating: "+ file.getName());

            File prop;
            if (swithPropertiesToBp){
                prop = new File(file.getAbsolutePath().replace("default.properties", "default.bp"));
            } else {
                prop = file;
            }

            System.out.println("updating properties in " + prop );
            Writer writer = new FileWriter(prop, false);
            writer.write(defaultProps);
            writer.close();

            progressWidget.incrementProgress(1);
        }


        System.out.println("finish");

    }


    private void directoryWalker(File location) {
        if (location.isDirectory()) {
            for (File file : location.listFiles()) {
                if (file.isDirectory()) {
                    directoryWalker(file);
                } else {
                    affectedFiles.add(file);
                }
            }
        }
    }


}
