package ua.edu.sumdu.j2se.dmitry.controller;

import ua.edu.sumdu.j2se.dmitry.view.ConfirmExitDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by darkie on 29.01.17.
 */
public class ConfirmExitDialogController extends Controller {

    private Controller controller;
    private ToolbarController toolbar;

    public ConfirmExitDialogController (Controller controller) {
        this.controller = controller;
        this.toolbar = new ToolbarController(this.controller);
    }

    public void throwConfirmExitDialog() {
        ConfirmExitDialog exit = new ConfirmExitDialog(this.controller);
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
                controller.closeApplication();
            }
        });
    }

    public void setDontSaveButtonListener (ConfirmExitDialog dialog) {
        dialog.getDonTSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.closeApplication();
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
