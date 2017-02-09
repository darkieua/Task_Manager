package taskmanager.view;

import taskmanager.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Darkie on 07.02.2017.
 */
public abstract class Dialog extends JDialog {
    protected MainController mainController;
    protected int WINDOW_HEIGHT;
    protected int WINDOW_WEIGHT;

    public Dialog (Frame owner, String title) {
        super(owner, title);
    }

    public Dialog (MainController mainController) {
        this.mainController = mainController;
    }

    protected void onOK() {
        dispose();
    }

    protected void onCancel() {
        dispose();
    }


}
