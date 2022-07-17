package com.github.artyitis.discordbot.button;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public abstract class MyButton {

    protected Button button;

    public MyButton(ButtonType type, String id, String label) {
        switch (type) {
            case PRIMARY:
                button = Button.primary(id, label);
                break;
            case SECONDARY:
                button = Button.secondary(id, label);
                break;
            case SUCCESS:
                button = Button.success(id, label);
                break;
            case DANGER:
                button = Button.danger(id, label);
                break;
        }
        Buttonlist.getInstance().addButton(this);
    }

    public MyButton(ButtonType type, String id, Emoji emoji) {
        switch (type) {
            case PRIMARY:
                button = Button.primary(id, emoji);
                break;
            case SECONDARY:
                button = Button.secondary(id, emoji);
                break;
            case SUCCESS:
                button = Button.success(id, emoji);
                break;
            case DANGER:
                button = Button.danger(id, emoji);
                break;
        }
        Buttonlist.getInstance().addButton(this);
    }

    public abstract void execute(ButtonInteractionEvent event);

    public Button getButton() {
        return button;
    }
}
