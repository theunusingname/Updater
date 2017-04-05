package com.axiom.ui;

import com.axiom.ui.uiConstructor.UIConstructorPanel;
import com.axiom.updater.Updater;
import com.axiom.updater.processing.Change;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by theun on 10.02.2017.
 */
public class App extends Application {

    private File exportDir;
    private Updater updater;

    private final VBox rootNode = new VBox(10);
    private HBox choseFilesBox = new HBox(10);
    private HBox textBox = new HBox(30);
    private VBox newPropsBox = new VBox(5);
    private TextArea defprops = new TextArea();
    private TextArea oldDefProps = new TextArea();

    private Button newPropsApplyButton;
    private CheckBox saveAsXML;
    private Button openButton;
    private Button submit;
    private Button clear;
    private Button copy;
    private TextField directory;

    private UIConstructorPanel uIconstructor;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("update defaults");

        Scene mainScene = new Scene(rootNode, 800, 600);
        primaryStage.setScene(mainScene);

        newPropsApplyButton = new Button("Apply");
        openButton = new Button("Open");
        newPropsApplyButton.setDisable(true);
        directory = new TextField("Clik open to select export folder");

        saveAsXML = new CheckBox("Save default.properties as defaulp.bp");
        saveAsXML.setIndeterminate(false);
        directory.setEditable(false);

        defprops.setText("paste new properties here");
        oldDefProps.setEditable(false);
        oldDefProps.setText("old default.properties");

        submit = new Button("Submit");
        copy = new Button(">>");
        submit.setDisable(true);

        clear = new Button("Clear");
        clear.setDisable(true);

        uIconstructor = new UIConstructorPanel(Change.class.getPackage());

        choseFilesBox.setScaleShape(true);
        choseFilesBox.getChildren().addAll(openButton, directory);
        newPropsBox.getChildren().addAll(defprops, newPropsApplyButton, saveAsXML);
        textBox.getChildren().addAll(oldDefProps, copy, newPropsBox);
        textBox.fillHeightProperty().setValue(true);
        textBox.minHeight(400);
        rootNode.getChildren().addAll(choseFilesBox, textBox, uIconstructor, clear, submit);

        this.setActions(primaryStage);

        primaryStage.show();

    }

    private void setActions(Stage primaryStage) {

        primaryStage.setOnCloseRequest(event -> {
            try {
                this.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        openButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            exportDir = chooser.showDialog(primaryStage);
            if (exportDir != null) {
                directory.setText(exportDir.getAbsolutePath());
                updater = new Updater(exportDir.getAbsolutePath());
                oldDefProps.setText(updater.getCurrentDefaultProperties());
                newPropsApplyButton.setDisable(false);
            }
        });

        newPropsApplyButton.setOnAction(new ApplyHandler());

        saveAsXML.setOnAction(event -> {
            updater.setSwithPropertiesToBp(saveAsXML.isSelected());
        });

        copy.setOnAction(event -> {
            defprops.setText(oldDefProps.getText());
            new ApplyHandler().handle(event);
        });

        clear.setOnAction(event -> {
            updater.clear();
            defprops.setText("");
            uIconstructor.clear();
            submit.setDisable(true);
        });

        submit.setOnAction(event -> {
            try {
                List<Change> changes = StreamSupport
                        .stream(uIconstructor.getConstructors().spliterator(), false).map(constructor -> {
                            try {
                                return (Change) constructor.invokeConstructor();
                            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .collect(Collectors.toList());

                changes.forEach(change -> updater.addChange(change));
                updater.update();
            } catch (ParserConfigurationException | TransformerException | SAXException | IOException e) {
                e.printStackTrace();
            }
            clear.setDisable(false);
        });
    }

    private class ApplyHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            if (updater != null) {
                updater.setDefaultProps(defprops.getText());
                System.out.println("new props set to: \n" + defprops.getText());
                submit.setDisable(false);
            }
        }
    }
}

