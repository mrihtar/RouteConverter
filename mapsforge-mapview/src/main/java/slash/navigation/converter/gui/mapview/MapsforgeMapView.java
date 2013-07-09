/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/

package slash.navigation.converter.gui.mapview;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.awt.AwtGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.FileSystemTileCache;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import slash.navigation.base.NavigationPosition;
import slash.navigation.converter.gui.augment.PositionAugmenter;
import slash.navigation.converter.gui.models.CharacteristicsModel;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.converter.gui.models.PositionsSelectionModel;
import slash.navigation.converter.gui.models.UnitSystemModel;

import java.awt.*;
import java.io.File;

import static org.mapsforge.map.rendertheme.InternalRenderTheme.OSMARENDER;

/**
 * Implementation for a component that displays the positions of a position list on a map
 * using the rewrite branch of the mapsforge project.
 *
 * @author Christian Pesch
 */

public class MapsforgeMapView implements MapView {
    private static final GraphicFactory GRAPHIC_FACTORY = AwtGraphicFactory.INSTANCE; // TODO also defined in AwtGraphicMapView

    private AwtGraphicMapView mapView;

    public MapsforgeMapView() {
        mapView = createMapView();
        addLayers(mapView);
        mapView.getModel().mapViewPosition.setZoomLevel((byte) 8);
        mapView.getModel().mapViewPosition.setCenter(new LatLong(53.5, 10.0));
    }

    private AwtGraphicMapView createMapView() {
        AwtGraphicMapView mapView = new AwtGraphicMapView();
        mapView.addComponentListener(new MapViewComponentListener(mapView, mapView.getModel().mapViewModel));

        MapViewMouseEventListener mapViewMouseEventListener = new MapViewMouseEventListener(mapView.getModel().mapViewPosition);
        mapView.addMouseListener(mapViewMouseEventListener);
        mapView.addMouseMotionListener(mapViewMouseEventListener);
        mapView.addMouseWheelListener(mapViewMouseEventListener);

        return mapView;
    }

    private void addLayers(AwtGraphicMapView mapView) {
        Layers layers = mapView.getLayerManager().getLayers();
        TileCache tileCache = createTileCache();

        // layers.add(createTileDownloadLayer(tileCache, mapView.getModel().mapViewPosition));
        layers.add(createTileRendererLayer(tileCache, mapView.getModel().mapViewPosition));
        // layers.add(new TileGridLayer(GRAPHIC_FACTORY));
        // layers.add(new TileCoordinatesLayer(GRAPHIC_FACTORY));
    }

    private static Layer createTileRendererLayer(TileCache tileCache, MapViewPosition mapViewPosition) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapViewPosition, GRAPHIC_FACTORY);
        tileRendererLayer.setMapFile(new File("C:/development/mapsforge-maps/germany.map"));
        tileRendererLayer.setXmlRenderTheme(OSMARENDER);
        return tileRendererLayer;
    }

    private static TileCache createTileCache() {
        TileCache firstLevelTileCache = new InMemoryTileCache(64);
        File cacheDirectory = new File(System.getProperty("java.io.tmpdir"), "mapsforge");
        TileCache secondLevelTileCache = new FileSystemTileCache(1024, cacheDirectory, GRAPHIC_FACTORY);
        return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
    }

    public void initialize(PositionsModel positionsModel, PositionsSelectionModel positionsSelectionModel, CharacteristicsModel characteristicsModel, PositionAugmenter positionAugmenter, boolean recenterAfterZooming, boolean showCoordinates, boolean showWaypointDescription, TravelMode travelMode, boolean avoidHighways, boolean avoidTolls, UnitSystemModel unitSystemModel) {
        // TODO implement me
    }

    public boolean isSupportedPlatform() {
        return true;
    }

    public boolean isInitialized() {
        return false;  // TODO implement me
    }

    public Throwable getInitializationCause() {
        return null;  // TODO implement me
    }

    public void dispose() {
        mapView.destroy();
    }

    public Component getComponent() {
        return mapView;
    }

    public void resize() {
        // TODO implement me
    }

    public void setRecenterAfterZooming(boolean recenterAfterZooming) {
        // TODO implement me
    }

    public void setShowCoordinates(boolean showCoordinates) {
        // TODO implement me
    }

    public void setShowWaypointDescription(boolean showWaypointDescription) {
        // TODO implement me
    }

    public void setTravelMode(TravelMode travelMode) {
        // TODO implement me
    }

    public void setAvoidHighways(boolean avoidHighways) {
        // TODO implement me
    }

    public void setAvoidTolls(boolean avoidTolls) {
        // TODO implement me
    }

    public NavigationPosition getCenter() {
        return null;  // TODO implement me
    }

    public void setCenter(NavigationPosition center) {
        // TODO implement me
    }

    public void print(String title, boolean withDirections) {
        // TODO implement me
    }

    public void addMapViewListener(MapViewListener listener) {
        // TODO implement me
    }

    public void removeMapViewListener(MapViewListener listener) {
        // TODO implement me
    }

    public void insertAllWaypoints(int[] startPositions) {
        // TODO implement me
    }

    public void insertOnlyTurnpoints(int[] startPositions) {
        // TODO implement me
    }

    public void setSelectedPositions(int[] selectedPositions, boolean replaceSelection) {
        // TODO implement me
    }
}
