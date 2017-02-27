package com.axiom.ui.uiConstructor;

import com.axiom.updater.ClassFinder;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kkuznetsov on 2/15/2017.
 */
public class UIConstructorPanelElement extends VBox {


  List<Class> classes;
  Constructor constructor;
  String[] argsNames;

  VBox selectBox = new VBox(5);
  HBox argsBox = new HBox(10);
  ComboBox<String> avalaibleClasses = new ComboBox<>();
  Button addButton = new Button("Add");

  public UIConstructorPanelElement(Package pakage) {
    super();
    String pakStr = pakage.toString();
    classes = ClassFinder.find(pakStr.substring(8)).stream()
        .filter(aClass -> aClass.isAnnotationPresent(UIConstructedClass.class))
        .collect(Collectors.toList());

    avalaibleClasses.setItems(
        FXCollections.observableList(classes.stream().map(Class::getName).collect(Collectors.toList())));

    setActions();
    selectBox.getChildren().addAll(avalaibleClasses, addButton);

    this.getChildren().addAll(selectBox, argsBox);
  }

  private void setActions() {

    avalaibleClasses.setOnShowing(event -> {
      String className = avalaibleClasses.getValue();
      if (className != null) {
        Class clazz = classes.stream()
            .filter(aClass -> aClass.getName().endsWith(className))
            .findFirst().orElse(null);
        if (clazz != null) {

          constructor = Arrays.stream(clazz.getConstructors())
              .filter(predicate -> predicate.isAnnotationPresent(UIConstructor.class))
              .findFirst().orElse(null);

          UIConstructor uiConstructor = (UIConstructor) constructor.getAnnotation(UIConstructor.class);

          argsNames = uiConstructor.args();

          for (String arg : argsNames) {
            argsBox.getChildren().add(0, new TextField(arg));
          }

        }
      }
    });

    addButton.setOnAction(event -> {

    });
  }


}