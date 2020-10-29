package com.verisence.zoackadventures.utils.dialogs;

public enum DialogType {
    CANCEL_PAYMENT,
    SUCCESSFUL_PAYMENT,
    NO_INTERNET,
    SERVICE_UNAVAILABLE,
    UNAUTHORIZED,

    //TODO confirm the scenerio that the dialogs below will be called
    TRANSACTION_NOT_PROCESSED,
    PAYMENT_TO_BE_DELIVERED
}
