package com.agh.dudek;

import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by dudek on 9/27/16.
 */
public class MarkInputController extends GestureDetector {

    public MarkInputController(){
        super(new MarkGestureListener());
    }

    private static class MarkGestureListener extends GestureAdapter {
        @Override
        public boolean touchDown (float x, float y, int pointer, int button) {
            System.out.println("Touched with x: " + x + ", y: " + y);
            return false;
        }
    }
}
