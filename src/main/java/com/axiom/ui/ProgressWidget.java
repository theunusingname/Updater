package com.axiom.ui;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Константин on 31.03.2016.
 */
public class ProgressWidget extends JFrame implements ChangeListener {
    JProgressBar progressBar;
    int min;
    int max;
    int currentProgres;
    boolean interuptable; // TODO: 01.04.2016
    JButton interuptbutton;
    JLabel textInfo;
    public ProgressWidget(int min, int max, String name)
    {
        super(name);
        currentProgres=min;
        this.min=min;
        this.max=max;
        progressBar=new JProgressBar(JProgressBar.HORIZONTAL,min,max);
        textInfo=new JLabel();
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(textInfo);
        add(progressBar);
        progressBar.addChangeListener(this);
        setSize(300,150);
        progressBar.setSize(getSize());
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void setProgress(int currentProgress)
    {
        this.currentProgres=currentProgress;
        progressBar.setValue(currentProgress);
        repaint();
    }

    public void incrementProgress(int incrementer)
    {
        currentProgres+=incrementer;
        progressBar.setValue(currentProgres);
        if(currentProgres == max)
            this.dispose();
        repaint();
    }

    public synchronized void setTextInfo(String text)
    {
        textInfo.setText(text);
        textInfo.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }
}