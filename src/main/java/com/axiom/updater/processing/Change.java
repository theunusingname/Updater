package com.axiom.updater.processing;

import javafx.scene.Node;
import org.w3c.dom.Document;

/**
 * Created by kkuznetsov on 2/13/2017.
 */
public interface Change {
    void apply (Document doc);

}
