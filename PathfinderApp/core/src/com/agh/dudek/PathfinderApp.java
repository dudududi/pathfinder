package com.agh.dudek;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;


public class PathfinderApp extends ApplicationAdapter {


    private  MapRenderer renderer;
	private PerspectiveCamera camera;
	private Environment environment;
	private Stage stage;
    private CameraInputController cameraInputController;
	private Map map;
	private Skin skin;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		setup();

        renderer = new MapRenderer(map, camera, environment);
	}

	@Override
	public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderer.render();
		stage.draw();
	}

	@Override
	public void dispose () {
		renderer.dispose();
		stage.dispose();
	}

	public void setMap(Map map){
		this.map = map;
	}

	private void setup(){
		if (map == null) {
			setupMap();
		}
		setup3D();
		setupMenu();


        InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(cameraInputController);

		Gdx.input.setInputProcessor(multiplexer);
	}

	private void setup3D(){
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, 400f, 0f);
		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 3000f;
		camera.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f,0.8f, 1f));
		environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, -0.8f, -0.2f));

        cameraInputController = new CameraInputController(camera);
	}

	private void setupMenu(){
		stage = new Stage();

		final TextButton resetViewButton = new TextButton("Reset view", skin);
		resetViewButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				camera.position.set(0f, 400f, 0f);
				camera.direction.set(0, 0, -1);
				camera.up.set(0, 1, 0);
				camera.lookAt(0, 0, 0);
				camera.update();
			}
		});

		final TextButton findPathButton = new TextButton("Find path", skin);
		findPathButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				createStartDialog().show(stage);
			}
		});

		Table buttonsTable = new Table();
		buttonsTable.add(findPathButton).width(Value.percentWidth(2f)).height(Value.percentHeight(2f));
		buttonsTable.row();
		buttonsTable.add(resetViewButton).padTop(5).width(Value.percentWidth(2f)).height(Value.percentHeight(2f));
		buttonsTable.setPosition(100, 100);
		stage.addActor(buttonsTable);

		Table mapInfo = new Table();
		mapInfo.add(new Label("Map width: " + map.getWidth(), skin));
		mapInfo.row();
		mapInfo.add(new Label("Map depth: " + map.getDepth(), skin));
		mapInfo.row();
		mapInfo.add(new Label("Map height: " + map.getHeight(), skin));
		mapInfo.setPosition(60, Gdx.graphics.getHeight() - 60);
		stage.addActor(mapInfo);

	}

	private void setupMap(){
		map = new Map(20, 37, 40);

		map.addBuilding(new Building(14, 8, 4, 5, 8));
		map.addBuilding(new Building(4, 2, 3, 6, 2));
		map.addBuilding(new Building(4, 12, 3, 5, 3));
		map.addBuilding(new Building(8, 16, 2, 7, 4));
		map.addBuilding(new Building(3, 19, 4, 12, 15));
		map.addBuilding(new Building(3, 19, 4, 12, 2));
		map.addBuilding(new Building(14, 20, 5, 4, 5));
		map.addBuilding(new Building(14, 30, 6, 4, 7));

		map.createGraph();
	}

	private PositionDialog createStartDialog(){
		PositionDialog startDialog = new PositionDialog("Type start position", skin);
		startDialog.setCallback(new PositionDialog.Callback() {
			@Override
			public void onPositionReceived(Position position) {
				if (isValidPosition(position)) {
					PositionDialog endDialog = createEndDialog(position);
					endDialog.show(stage);
				} else {
					this.onError("Position is outside the bounds of map");
				}
			}

			@Override
			public void onError(String errorMessage) {
				new ErrorDialog(errorMessage, skin).show(stage);
			}
		});

		return startDialog;
	}

	private PositionDialog createEndDialog(final Position startPosition) {
		final PositionDialog endDialog = new PositionDialog("Type end position", skin);
		endDialog.setCallback(new PositionDialog.Callback() {
			@Override
			public void onPositionReceived(Position endPosition) {
				if (isValidPosition(endPosition)) {
					List<Position> path = map.findPath(startPosition, endPosition);
					renderer.drawPath(path);
				} else {
					this.onError("Position is outside the bounds of map");
				}
			}

			@Override
			public void onError(String errorMessage) {
				new ErrorDialog(errorMessage, skin).show(stage);
			}
		});

		return endDialog;
	}

	private boolean isValidPosition(Position position){
		return position.getX() >= 0 && position.getY() >= 0 && position.getZ() >= 0 &&
				position.getX() < map.getWidth() && position.getY() < map.getDepth() && position.getZ() < map.getHeight();
	}



}
