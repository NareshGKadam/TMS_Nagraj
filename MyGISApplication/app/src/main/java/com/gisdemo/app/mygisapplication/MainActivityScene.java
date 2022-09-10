/*
package com.gisdemo.app.mygisapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;

public class MainActivityScene extends AppCompatActivity {

    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scene);

*/
/*

// create a scene and add a basemap to it
        ArcGISScene scene = new ArcGISScene();
        scene.setBasemap(Basemap.createImagery());


// add a scene layer
        final String sceneLayerURL =
                "http://tiles.arcgis.com/tiles/P3ePLMYs2RVChkJx/arcgis/rest/services/Buildings_Brest/SceneServer/layers/0";
        ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer(sceneLayerURL);
        scene.getOperationalLayers().add(sceneLayer);

        // create SceneView from layout
        SceneView mSceneView = (SceneView) findViewById(R.id.sceneView);
        mSceneView.setScene(scene);

*//*



*/
/*
         // create a scene and add a basemap to it
    ArcGISScene scene = new ArcGISScene();
    scene.setBasemap(Basemap.createImagery());

    //[DocRef: Name=Display Scene-android, Category=Work with 3D, Topic=Display a scene]
    // create SceneView from layout
        SceneView mSceneView = (SceneView) findViewById(R.id.sceneView);
    mSceneView.setScene(scene);
    //[DocRef: END]

    //[DocRef: Name=Add elevation to base surface-android, Category=Work with 3D, Topic=Display a scene,
    // RemoveChars=getResources().getString(R.string.elevation_image_service),
    // ReplaceChars=http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer]
    // create an elevation source, and add this to the base surface of the scene
    ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource("hiii");
    scene.getBaseSurface().getElevationSources().add(elevationSource);
    //[DocRef: END]

    // add a camera and initial camera position  /17.446937, 78.374391)
    Camera camera = new Camera(28.4,  78.374391, 17.446937, 10.0, 80.0, 0.0);
    mSceneView.setViewpointCamera(camera);*//*



// create a scene and add a basemap to it
        ArcGISScene scene = new ArcGISScene();
        scene.setBasemap(Basemap.createImagery());

        SceneView mSceneView = (SceneView) findViewById(R.id.sceneView);
        mSceneView.setScene(scene);

        // add a scene service to the scene for viewing buildings
        ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer("https://tiles.arcgis.com/tiles/P3ePLMYs2RVChkJx/arcgis/rest/services/Buildings_Brest/SceneServer");
        scene.getOperationalLayers().add(sceneLayer);

        // add a camera and initial camera position 17.446937, 78.374391)
        Camera camera = new Camera( 17.446937, 78.374391, 200, 345, 65, 0);
        mSceneView.setViewpointCamera(camera);
    }






    
}
*/
