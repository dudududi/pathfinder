package com.agh.dudek;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 9/21/16.
 */
public class MapRenderer {

    private Map map;
    private List<RenderableProvider> buildings;
    private RenderableProvider path;
    private List<Disposable> disposables;
    private ModelInstance mesh;
    private ModelBatch modelBatch;
    private Camera camera;
    private Environment environment;
    private Model origin;
    private ModelBuilder modelBuilder;



    public MapRenderer(Map map, Camera camera, Environment environment){
        this.map = map;
        this.camera = camera;
        this.environment = environment;

        this.buildings = new ArrayList<>();
        this.disposables = new ArrayList<>();
        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();

        origin = modelBuilder.createXYZCoordinates(10, new Material(ColorAttribute.createDiffuse(new Color(Color.RED))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);

        disposables.add(modelBatch);
        disposables.add(origin);

        generateMap();
    }

    public void render(){
        modelBatch.begin(camera);
        modelBatch.render(buildings, environment);
        modelBatch.render(mesh, environment);
        modelBatch.render(new ModelInstance(origin, computePosition(0, 0, 0)), environment);
        if (path != null) {
            modelBatch.render(path, environment);
        }
        modelBatch.end();

    }

    public void drawPath(List<Position> path){
        Model pathPart = modelBuilder.createSphere(Map.NODE_SIZE, Map.NODE_SIZE, Map.NODE_SIZE, 10, 10,
                new Material(ColorAttribute.createDiffuse(new Color(Color.RED))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);

        ModelCache modelCache = new ModelCache();
        modelCache.begin();
        for (Position pos: path){
            modelCache.add(new ModelInstance(pathPart, computePosition(pos)));
        }
        modelCache.end();
        this.path = modelCache;

        disposables.add(pathPart);
        disposables.add(modelCache);
    }

    public void dispose(){
        for (Disposable disposable: disposables){
            disposable.dispose();
        }
    }

    private void generateMap(){
        mesh = new ModelInstance(map.getModel()); //place mesh in the center of view
        disposables.add(map.getModel());

        for(Building b: map.getBuildings()){
            if (b.shouldMergePoints()) {
                List<ModelInstance> pointInstances = new ArrayList<>();
                for (Building.Point point: b.getBuildingPoints()){
                    pointInstances.add(new ModelInstance(b.getModel(), computePosition(point)));
                }
                ModelCache cache = new ModelCache();
                cache.begin();
                cache.add(pointInstances);
                cache.end();
                buildings.add(cache);
                disposables.add(cache);
            } else {
                ModelInstance instance = new ModelInstance(b.getModel(), computePosition(b));
                buildings.add(instance);
            }
            disposables.add(b.getModel());
        }
    }


    // Remember that mesh is drawn in XZ dimension! Therefore we have to reverse our model
    // so now ground is placed in XZ, not in XY dimension.
    // Additionally, mesh is placed in center of view - (0,0) in the view is not (0,0) on the model.
    private Vector3 computePosition(int x, int y, int z){
        Vector3 position = new Vector3(x * Map.NODE_SIZE, z, y * Map.NODE_SIZE);

        float xShift = Map.NODE_SIZE * map.getWidth() / 2;
        float zShift = Map.NODE_SIZE * map.getDepth() / 2;
        position.sub(xShift, 0, zShift);

        return position;
    }

    private Vector3 computePosition(Position pos){
        Vector3 position = new Vector3(pos.getX() * Map.NODE_SIZE, pos.getZ() * Map.NODE_SIZE, pos.getY() * Map.NODE_SIZE);

        float xShift = Map.NODE_SIZE * map.getWidth() / 2 - Map.NODE_SIZE / 2;
        float zShift = Map.NODE_SIZE * map.getDepth() / 2 - Map.NODE_SIZE / 2;
        float hShift = -Map.NODE_SIZE / 2;
        position.sub(xShift, hShift, zShift);

        return position;
    }

    private Vector3 computePosition(Building b) {
        Vector3 position = new Vector3(b.getX() * Map.NODE_SIZE, b.getHeight() * Map.NODE_SIZE / 2, b.getY() * Map.NODE_SIZE);

        float xShift = Map.NODE_SIZE * map.getWidth() / 2 - b.getWidth() * 2;
        float zShift = Map.NODE_SIZE * map.getDepth() / 2 - b.getDepth() * 2;
        position.sub(xShift, 0, zShift);

        return position;
    }

    private Vector3 computePosition(Building.Point b) {
        Vector3 position = new Vector3(b.x * Map.NODE_SIZE, b.height * Map.NODE_SIZE / 2, b.y * Map.NODE_SIZE);

        float xShift = Map.NODE_SIZE * map.getWidth() / 2 -  Map.NODE_SIZE / 2;
        float zShift = Map.NODE_SIZE * map.getDepth() / 2 -  Map.NODE_SIZE / 2;
        position.sub(xShift, 0, zShift);

        return position;
    }
}
