package com.aseupc.flattitude.Models;

import android.text.Editable;

/**
 * Created by Vavou on 13/12/2015.
 */
public class ChatMessage {
    private String message;

    private boolean leftSide;

    public String getMessage() {
        return message;
    }

    public boolean isLeftSide() {
        return leftSide;
    }

    public ChatMessage(String message) {
        this(message, false);
    }

    public ChatMessage(String message, boolean leftSide) {
        super();
        this.message = message;
        this.leftSide = leftSide;
    }
}
