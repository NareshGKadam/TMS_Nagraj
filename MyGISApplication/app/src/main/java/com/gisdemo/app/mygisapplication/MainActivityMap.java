/*
package com.gisdemo.app.mygisapplication;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

public class MainActivityMap extends AppCompatActivity {
    GraphicsOverlay mGraphicsOverlay;
    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mMapView = findViewById(R.id.mapView);  //17.446937, 78.374391)
        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 17.446937, 78.374391, 16);
        mMapView.setMap(map);


// create a new graphics overlay and add it to the mapview
         mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        BitmapDrawable pinStarBlueDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.rsz_clocation);
        final PictureMarkerSymbol pinStarBlueSymbol = new  PictureMarkerSymbol(pinStarBlueDrawable);
        //Optionally set the size, if not set the image will be auto sized based on its size in pixels,
        //its appearance would then differ across devices with different resolutions.
        pinStarBlueSymbol.setHeight(40);
        pinStarBlueSymbol.setWidth(40);
        //Optionally set the offset, to align the base of the symbol aligns with the point geometry
        pinStarBlueSymbol.setOffsetY(
                11); //The image used for the symbol has a transparent buffer around it, so the offset is not simply height/2
        pinStarBlueSymbol.loadAsync();
        //[DocRef: END]
        pinStarBlueSymbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
//                //add a new graphic with the same location as the initial viewpoint
//                Point pinStarBluePoint = new Point(226773, 6550477);
//
//                Graphic pinStarBlueGraphic = new Graphic(pinStarBluePoint, pinStarBlueSymbol);
//                mGraphicsOverlay.getGraphics().add(pinStarBlueGraphic);
            }
        });



    }

    @Override
    protected void onPause(){
        mMapView.pause();
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }

    
}
*/
