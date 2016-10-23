package com.agh.dudek;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;


public class PathfinderApp extends ApplicationAdapter {


    private  MapRenderer renderer;
	private PerspectiveCamera camera;
	private Environment environment;
	private Stage stage;
    private InputMultiplexer multiplexer;
    private CameraInputController cameraInputController;
    private boolean isInEditMode;
	private Map map;

	@Override
	public void create () {
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
		setup3D();
		setupMenu();
		if (map == null) {
			setupMap();
		}

        multiplexer = new InputMultiplexer();
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
		stage.getCamera();

		TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
		style.font = new BitmapFont();
		style.fontColor = Color.RED;
		style.font.getData().setScale(15, 15);

        final MarkInputController markInputController = new MarkInputController();

		final TextButton addNewBuildingButton = new TextButton("Add new building", style);
		addNewBuildingButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
                multiplexer.removeProcessor(cameraInputController);
                multiplexer.addProcessor(markInputController);
                isInEditMode = !isInEditMode;
                if (isInEditMode) {
                    camera.position.set(0f, 400f, 0f);
                    camera.direction.set(0, 0, -1);
                    camera.up.set(0, 1, 0);
                    camera.lookAt(0, 0, 0);
                    camera.update();
                    addNewBuildingButton.setText("Done");
                } else {
                    addNewBuildingButton.setText("Add new building");
                    multiplexer.removeProcessor(markInputController);
                    multiplexer.addProcessor(cameraInputController);
                }
			}
		});
		addNewBuildingButton.setX(0);
		addNewBuildingButton.setY(0);

		final TextButton findPathButton = new TextButton("Find path", style);
		findPathButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				List<Position> path = map.findPath(new Position(0, 0, 11), new Position(49, 129, 0));
				renderer.drawPath(path);
			}
		});
		findPathButton.setY(style.font.getLineHeight());

		stage.addActor(addNewBuildingButton);
		stage.addActor(findPathButton);
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


}
