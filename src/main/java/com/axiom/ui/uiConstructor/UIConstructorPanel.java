package com.axiom.ui.uiConstructor;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by theun on 27.02.2017.
 */
public class UIConstructorPanel extends VBox {

  private final Package pakage;
  private UIElementConstructor constructor;

  private Map<String, AttributedConstructor> constructors = new HashMap<>();

  private Button saveChanges = new Button("Save Changes");
  private Button loadChanges = new Button("Load changes");

  private Button addButton = new Button("Add");
  private Button deleteButton = new Button("Delete");
  private TextField idField = new TextField("element ID");

  private HBox addBox = new HBox(5);

  ListView<String> listView = new ListView<String>();

  public UIConstructorPanel(Package pakage){
    super();

    this.setSpacing(10);

    this.pakage = pakage;
    constructor = new UIElementConstructor(pakage);
    addBox.getChildren().addAll(idField, addButton, saveChanges, loadChanges);
    this.getChildren().addAll(constructor, addBox, listView, deleteButton);
    setActions();
  }

  private void setActions(){
    addButton.setOnAction(event ->{
      AttributedConstructor attributedConstructor = constructor
          .getAttributedConstructor();
      if (attributedConstructor != null){
        String id = idField.getText();
        if (constructors.containsKey(id)){
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Element with ID "+ id + " is already exist");
          alert.showAndWait();
          return;
        }
        constructors.put(idField.getText() ,attributedConstructor);
        listView.setItems(FXCollections.observableArrayList(constructors.keySet()));
        constructor = new UIElementConstructor(pakage);
        this.getChildren().set(0,constructor);
      }
    });

    deleteButton.setOnAction(event -> {
       listView.getSelectionModel().getSelectedItems().forEach(item ->
           constructors.remove(item));
       listView.setItems(FXCollections.observableArrayList(constructors.keySet()));
    });

    loadChanges.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      File fileToLoad = fileChooser.showOpenDialog(null);
      try {
        FileInputStream fis = new FileInputStream(fileToLoad);
        ObjectInputStream ois = new ObjectInputStream(fis);
        constructors = (Map<String, AttributedConstructor>) ois.readObject();

        listView.setItems(FXCollections.observableArrayList(constructors.keySet()));
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    });

    saveChanges.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      File fileToSave = fileChooser.showSaveDialog(null);
      try {
        FileOutputStream fos = new FileOutputStream(fileToSave);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(constructors);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public Iterable<AttributedConstructor> getConstructors() {
    return constructors.values();
  }
}
