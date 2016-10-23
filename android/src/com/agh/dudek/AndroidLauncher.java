package com.agh.dudek;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.agh.dudek.PathfinderApp;

import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication {

	private static final int DEFAULT_BUILDINGS_HEIGHT = 7;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Map map = createMapFromIntent(getIntent());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		PathfinderApp pathfinderApp = new PathfinderApp();
		pathfinderApp.setMap(map);
		initialize(pathfinderApp, config);
	}

	@SuppressWarnings("unchecked")
	private Map createMapFromIntent(Intent intent){
		int width = intent.getExtras().getInt(MapActivity.WIDTH);
		int depth = intent.getExtras().getInt(MapActivity.DEPTH);
		int height = intent.getExtras().getInt(MapActivity.HEIGHT);
		ArrayList<Point> buildingPoints = intent.getParcelableArrayListExtra(MapActivity.BUILDINGS_POINTS);

		Map map = new Map(width, depth, height);
		for (Point point: buildingPoints){
			map.addNodeManually(point.x, point.y, DEFAULT_BUILDINGS_HEIGHT);
		}
		return  map;
	}
}
