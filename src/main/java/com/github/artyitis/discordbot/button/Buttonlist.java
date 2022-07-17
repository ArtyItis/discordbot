package com.github.artyitis.discordbot.button;

import java.util.ArrayList;

public class Buttonlist extends ArrayList<MyButton> {
    private static Buttonlist INSTANCE;

    private Buttonlist() {
    }

    public static Buttonlist getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Buttonlist();
        return INSTANCE;
    }

    public void addButton(MyButton button) {
        for (MyButton myButton : INSTANCE) {
            // check if button already exists
            if (myButton.button.getId().equalsIgnoreCase(button.getButton().getId())) {
                // remove old button
                remove(myButton);
                // add new button
                add(button);
                return;
            }
        }
        add(button);
    }
}
