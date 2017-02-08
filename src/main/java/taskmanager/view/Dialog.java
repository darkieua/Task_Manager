package taskmanager.view;

import taskmanager.controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Darkie on 07.02.2017.
 */
public abstract class Dialog extends JDialog {
    private Controller controller;
    private int WINDOW_HEIGHT;
    private int WINDOW_WEIGHT;

    public Dialog (Frame owner, String title) {
        super(owner, title);
    }

    public Dialog (Controller controller) {
        this.controller = controller;
    }

    protected void onOK() {
        // add your code here
        dispose();
    }

    protected void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
