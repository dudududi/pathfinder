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

	}

	private void setup(){
		setup3D();
		setupMenu();
		setupMap();

        multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(cameraInputController);

		Gdx.input.setInputProcessor(multiplexer);
	}

	private void setup3D(){
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 100f, 10f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
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

        final MarkInputController markInputController = new MarkInputController();

		final TextButton addNewBuildingButton = new TextButton("Add new building", style);
		addNewBuildingButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
                multiplexer.removeProcessor(cameraInputController);
                multiplexer.addProcessor(markInputController);
                isInEditMode = !isInEditMode;
                if (isInEditMode) {
                    camera.position.set(0, Map.NODE_SIZE * 40, 0);
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
				List<Position> path = map.findPath(new Position(0, 0, 9), new Position(9, 9, 0));
				renderer.drawPath(path);
			}
		});
		findPathButton.setY(style.font.getLineHeight());

		stage.addActor(addNewBuildingButton);
		stage.addActor(findPathButton);
	}

	private void setupMap(){
		map = new Map(10, 10, 10);
		//map.addBuilding(new Building(0, 0, 3, 3, 1));
		map.addBuilding(new Building(3, 2, 2, 2, 4)); //!!!!!!
		map.addBuilding(new Building(5, 7, 2, 2, 3));
		//map.addBuilding(new Building(10, 15, 8, 4, 15));
		//map.addBuilding(new Building(20, 20, 3, 3, 2));
		map.createGraph();
	}


}
