package com.verisence.zoackadventures.utils.dialogs;

public class DialogInfo {
    private DialogType dialogType;
    private String infoText;


    public DialogInfo(DialogType dialogType, String infoText) {
        this.dialogType = dialogType;
        this.infoText = infoText;

    }

    public DialogInfo() {
    }

    public DialogType getDialogType() {
        return dialogType;
    }

    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }
}
