package com.axiom.ui.uiConstructor;

import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by theun on 27.02.2017.
 */
public class UIConstructorPanel extends VBox {

  private final Package pakage;
  private ArrayList<UIConstructorPanelElement> elements = new ArrayList<>();

  public UIConstructorPanel(Package pakage){
    super();
    this.pakage = pakage;
    elements.add(new UIConstructorPanelElement(pakage));

    this.getChildren().addAll(elements);
  }
}
