package com.axiom.ui.uiConstructor;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by theun on 27.02.2017.
 */
public class UIConstructorPanel extends VBox {

  private final Package pakage;
  private UIElementConstructor constructor;

  private LinkedHashMap<String, AttributedConstructor> constructors = new LinkedHashMap<>();

  private Button saveChanges = new Button("Save Changes");
  private Button loadChanges = new Button("Load changes");

  private HBox upDown = new HBox(5);
  private Button upButton = new Button("↑");
  private Button downButton = new Button("↓");

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
    upDown.getChildren().addAll(upButton,downButton);
    addBox.getChildren().addAll(idField, addButton, saveChanges, loadChanges);
    this.getChildren().addAll(constructor, addBox, upDown, listView, deleteButton);
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
        constructors = (LinkedHashMap<String, AttributedConstructor>) ois.readObject();

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

    upButton.setOnAction(event -> {
      String keyToUp = listView.getSelectionModel().getSelectedItem();
      List<String> keys = constructors.keySet()
              .stream()
              .collect(Collectors.toList());
      int oldIndex = keys.indexOf(keyToUp);
      List<String> ordered;
      LinkedHashMap<String, AttributedConstructor> sortedConstructors;
      if (oldIndex > 0 ){
        String keyToDown = keys.get(oldIndex -1);
        ordered = keys.stream()
                .sorted((a,b) ->{
                  if ( b == keyToDown && a == keyToUp){
                    return -1;
                  }
                  return 1;
                })
        .collect(Collectors.toList());

        sortedConstructors = new LinkedHashMap<>();

        Iterator<String> it = ordered.iterator();
        while (it.hasNext()){
          String key = it.next();
          sortedConstructors.put(key, constructors.get(key));
        }

        constructors = sortedConstructors;
      }

      listView.setItems(FXCollections.observableArrayList(constructors.keySet()));

    });

    downButton.setOnAction(event -> {
      String keyToD = listView.getSelectionModel().getSelectedItem();
      List<String> keys = constructors.keySet()
              .stream()
              .collect(Collectors.toList());
      int oldIndex = keys.indexOf(keyToD);
      List<String> ordered;
      LinkedHashMap<String, AttributedConstructor> sortedConstructors;
      if (oldIndex < keys.size()-1 ){
        String keyToU = keys.get(oldIndex +1);
        ordered = keys.stream()
                .sorted((a,b) ->{
                  if ( a == keyToU && b == keyToD){
                    return -1;
                  }
                  return 1;
                })
                .collect(Collectors.toList());

        sortedConstructors = new LinkedHashMap<>();

        Iterator<String> it = ordered.iterator();
        while (it.hasNext()){
          String key = it.next();
          sortedConstructors.put(key, constructors.get(key));
        }

        constructors = sortedConstructors;
      }

      listView.setItems(FXCollections.observableArrayList(constructors.keySet()));
    });
  }

  public Iterable<AttributedConstructor> getConstructors() {
    return constructors.values();
  }
}
