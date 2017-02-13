package taskmanager.controller;

import taskmanager.view.ConfirmExitDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by darkie on 29.01.17.
 */
public class ConfirmExitDialogController extends MainController {

    private MainController mainController;
    private ToolbarController toolbar;

    public ConfirmExitDialogController (MainController mainController) {
        this.mainController = mainController;
        this.toolbar = new ToolbarController(this.mainController);
    }

    public void throwConfirmExitDialog() {
        ConfirmExitDialog exit = new ConfirmExitDialog(this.mainController);
        setCancelButtonListener(exit);
        setDontSaveButtonListener(exit);
        setSaveButtonListener(exit);
        exit.pack();
        exit.setVisible(true);
    }

    public void setSaveButtonListener (ConfirmExitDialog dialog) {
        dialog.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                toolbar.saveButtonHandler();
                logger.info("Application closed with saving");
                mainController.closeApplication();
            }
        });
    }

    public void setDontSaveButtonListener (ConfirmExitDialog dialog) {
        dialog.getDonTSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.info("Application closed without saving");
                mainController.closeApplication();
            }
        });
    }

    public void setCancelButtonListener (ConfirmExitDialog dialog) {
        dialog.getButtonCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialog.dispose();
            }
        });
    }
}
