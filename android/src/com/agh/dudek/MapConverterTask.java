package com.agh.dudek;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by dudek on 10/20/16.
 */
public class MapConverterTask extends AsyncTask<Bitmap, Void, ArrayList<Point>> {

    private static final double MIN_SEGMENT_FULFILLMENT = 0.85;


    private int widthDivisions;
    private int heightDivisions;
    private Context context;

    public MapConverterTask(Context context, int widthDivisions, int heightDivisions) {
        this.context = context;
        this.widthDivisions = widthDivisions;
        this.heightDivisions = heightDivisions;
    }


    @Override
    protected ArrayList<Point> doInBackground(Bitmap... bitmaps) {
        Bitmap mapBitmap = bitmaps[0];
        ArrayList<Point> mapPoints = new ArrayList<>();

        int size = mapBitmap.getWidth() * mapBitmap.getHeight();
        int[] pixels = new int[size];
        mapBitmap.getPixels(pixels, 0, mapBitmap.getWidth(), 0, 0, mapBitmap.getWidth(), mapBitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int red = Color.red(pixel);
            int green = Color.green(pixel);
            int blue = Color.blue(pixel);

            if (red == 234 && green == 234 && blue == 234){
                pixels[i] = Color.BLACK;
            }
        }

        Bitmap result = Bitmap.createBitmap(pixels, 0, mapBitmap.getWidth(), mapBitmap.getWidth(), mapBitmap.getHeight(), mapBitmap.getConfig());

        int portionPerWidth = result.getWidth() / widthDivisions;
        int portionPerHeight = result.getHeight() / heightDivisions;

        for (int i = 0; i < widthDivisions; i++) {
            for (int j = 0; j < heightDivisions; j++){
                int startX = i * portionPerWidth;
                int startY = j * portionPerHeight;
                boolean isNode = checkSegmentFulfillment(startX, startY, startX + portionPerWidth, startY + portionPerHeight, result);
                if (isNode){
                    mapPoints.add(new Point(i, j));
                }
            }
        }
        return mapPoints;
    }

    @Override
    protected void onPostExecute(ArrayList<Point> result) {
        Intent intent = new Intent(context, AndroidLauncher.class);
        intent.putParcelableArrayListExtra(MapActivity.BUILDINGS_POINTS, result);
        intent.putExtra(MapActivity.WIDTH, MapActivity.WIDTH_DIVISIONS);
        intent.putExtra(MapActivity.DEPTH, MapActivity.HEIGHT_DIVISIONS);
        intent.putExtra(MapActivity.HEIGHT, MapActivity.MAP_HEIGHT);
        context.startActivity(intent);
    }


    private boolean checkSegmentFulfillment(int startX, int startY, int endX, int endY, Bitmap bitmap){
        int pixelsColored = 0;
        int pixelsNumber = 0;
        for (int i = startX; i < endX; i++){
            for (int j = startY; j < endY; j++){
                int pixel = bitmap.getPixel(i, j);
                pixelsNumber++;
                if (pixel != Color.BLACK) {
                    pixelsColored++;
                }
            }
        }
        int fulfillmentLevel = pixelsColored / pixelsNumber;
        return fulfillmentLevel > MIN_SEGMENT_FULFILLMENT;
    }
}
