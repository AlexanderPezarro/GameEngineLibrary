package main.java;

import java.awt.event.ActionEvent;

@FunctionalInterface
public interface ActionListener {
    void actionPerformed(Event event);
}