package com.agh.dudek;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

/**
 * Created by dudek on 10/24/16.
 */
public class PositionDialog extends Dialog {

    private static final String CANCELED = "canceled";
    private static final String CONFIRMED = "confirmed";

    private TextField positionX;
    private TextField positionY;
    private TextField positionZ;

    private Callback callback;


    public PositionDialog(String title, Skin skin){
        super(title, skin);
        positionX = new TextField("", skin);
        positionY = new TextField("", skin);
        positionZ = new TextField("", skin);

        TextButton confirmButton = new TextButton("Set Position", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);
        setObject(confirmButton, CONFIRMED);
        setObject(cancelButton, CANCELED);

        Table buttonTable = getButtonTable();
        buttonTable.add(confirmButton).width(Value.percentWidth(2f)).height(Value.percentHeight(2f));
        buttonTable.add(cancelButton).width(Value.percentWidth(2f)).height(Value.percentHeight(2f));

        Table contentTable = getContentTable();
        contentTable.add(new Label("Position.X: ", skin));
        contentTable.add(positionX);
        contentTable.row();
        contentTable.add(new Label("Position.Y: ", skin));
        contentTable.add(positionY);
        contentTable.row();
        contentTable.add(new Label("Position.Z: ", skin));
        contentTable.add(positionZ);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    protected void result(Object object){
        if (CANCELED.equals(object)){
            callback.onError("Cancelled");
            remove();
            return;
        }
        try {
            int x = Integer.parseInt(positionX.getText());
            int y = Integer.parseInt(positionY.getText());
            int z = Integer.parseInt(positionZ.getText());
            if (callback != null) {
                callback.onPositionReceived(new Position(x, y, z));
            }
        } catch (NumberFormatException e){
            if (callback != null) {
                callback.onError("Invalid values");
            }
        } finally {
            remove();
        }
    }

    public interface Callback{
        void onPositionReceived(Position position);
        void onError(String errorMessage);
    }
}
