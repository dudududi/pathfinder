package com.agh.dudek;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

/**
 * Created by dudek on 10/24/16.
 */
public class ErrorDialog extends Dialog {
    public ErrorDialog(String errorMessage, Skin skin) {
        super("Error", skin);
        text(errorMessage);

        TextButton confirmButton = new TextButton("OK", skin);
        setObject(confirmButton, "Error");
        getButtonTable().add(confirmButton).width(Value.percentWidth(2f)).height(Value.percentHeight(2f));
    }

    @Override
    protected void result(Object object) {
        remove();
    }
}
