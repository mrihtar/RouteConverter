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

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import slash.navigation.base.NavigationPosition;
import slash.navigation.base.Wgs84Position;
import slash.navigation.converter.gui.augment.PositionAugmenter;
import slash.navigation.converter.gui.models.CharacteristicsModel;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.converter.gui.models.PositionsSelectionModel;
import slash.navigation.converter.gui.models.UnitSystemModel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static org.mapsforge.map.rendertheme.InternalRenderTheme.OSMARENDER;
import static slash.navigation.converter.gui.mapview.AwtGraphicMapView.GRAPHIC_FACTORY;

/**
 * Implementation for a component that displays the positions of a position list on a map
 * using the rewrite branch of the mapsforge project.
 *
 * @author Christian Pesch
 */

public class MapsforgeMapView implements MapView {
    private static final Preferences preferences = Preferences.userNodeForPackage(MapsforgeMapView.class);
    private static final Logger log = Logger.getLogger(MapsforgeMapView.class.getName());

    private static final String MAPSFORGE_CACHE_DIRECTORY_PREFERENCE = "mapsforgeCacheDirectory";
    private static final String CENTER_LATITUDE_PREFERENCE = "centerLatitude";
    private static final String CENTER_LONGITUDE_PREFERENCE = "centerLongitude";
    private static final String CENTER_ZOOM_PREFERENCE = "centerZoom";

    private PositionsModel positionsModel;
    private PositionsSelectionModel positionsSelectionModel;
    private CharacteristicsModel characteristicsModel;
    private UnitSystemModel unitSystemModel;

    private AwtGraphicMapView mapView;
    private static Bitmap markerIcon;

    private boolean recenterAfterZooming, showCoordinates, showWaypointDescription, avoidHighways, avoidTolls;
    private TravelMode travelMode;
    private PositionAugmenter positionAugmenter;

    // initialization

    public void initialize(PositionsModel positionsModel,
                           PositionsSelectionModel positionsSelectionModel,
                           CharacteristicsModel characteristicsModel,
                           PositionAugmenter positionAugmenter,
                           boolean recenterAfterZooming,
                           boolean showCoordinates, boolean showWaypointDescription,
                           TravelMode travelMode, boolean avoidHighways, boolean avoidTolls,
                           UnitSystemModel unitSystemModel) {
        initializeMapView();
        setModel(positionsModel, positionsSelectionModel, characteristicsModel, unitSystemModel);
        this.positionAugmenter = positionAugmenter;
        this.recenterAfterZooming = recenterAfterZooming;
        this.showCoordinates = showCoordinates;
        this.showWaypointDescription = showWaypointDescription;
        this.travelMode = travelMode;
        this.avoidHighways = avoidHighways;
        this.avoidTolls = avoidTolls;
    }

    private void initializeMapView() {
        mapView = createMapView();
        addLayers(mapView);

        // TODO MapViewPosition#setMapPosition does this in one call
        double latitude = preferences.getDouble(CENTER_LATITUDE_PREFERENCE, 35.0);
        double longitude = preferences.getDouble(CENTER_LONGITUDE_PREFERENCE, -25.0);
        setCenter(new Wgs84Position(longitude, latitude, null, null, null, null));
        int zoom = preferences.getInt(CENTER_ZOOM_PREFERENCE, 8);
        setZoom(zoom);

        try {
            markerIcon = GRAPHIC_FACTORY.createBitmap(MapsforgeMapView.class.getResourceAsStream("marker.png"));
        } catch (IOException e) {
            log.severe("Cannot create marker icon: " + e.getMessage());
        }
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

    private static File getMapsforgeCacheDirectory() {
        String directoryName = preferences.get(MAPSFORGE_CACHE_DIRECTORY_PREFERENCE, new File(System.getProperty("user.home"), ".mapsforge").getAbsolutePath());
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (!directory.mkdirs())
                throw new IllegalArgumentException("Cannot create mapsforge cache directory " + directory);
        }
        return directory;
    }

    private static Layer createTileRendererLayer(TileCache tileCache, MapViewPosition mapViewPosition) {
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapViewPosition, GRAPHIC_FACTORY);
        tileRendererLayer.setMapFile(new File(getMapsforgeCacheDirectory(), "germany.map"));
        tileRendererLayer.setXmlRenderTheme(OSMARENDER);
        return tileRendererLayer;
    }

    private static TileCache createTileCache() {
        TileCache firstLevelTileCache = new InMemoryTileCache(64);
        // TODO think about replacing with file system cache that survives restarts
        // File cacheDirectory = new File(System.getProperty("java.io.tmpdir"), "mapsforge");
        // TileCache secondLevelTileCache = new FileSystemTileCache(1024, cacheDirectory, GRAPHIC_FACTORY);
        // return new TwoLevelTileCache(firstLevelTileCache, secondLevelTileCache);
        return firstLevelTileCache;
    }

    protected void setModel(PositionsModel positionsModel,
                            PositionsSelectionModel positionsSelectionModel,
                            CharacteristicsModel characteristicsModel,
                            UnitSystemModel unitSystemModel) {
        this.positionsModel = positionsModel;
        this.positionsSelectionModel = positionsSelectionModel;
        this.characteristicsModel = characteristicsModel;
        this.unitSystemModel = unitSystemModel;

        positionsModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                addLinesToMap(MapsforgeMapView.this.positionsModel.getRoute().getPositions());
            }
        });
    }

    public boolean isSupportedPlatform() {
        return true;
    }

    public boolean isInitialized() {
        return true;
    }

    public Throwable getInitializationCause() {
        return null;
    }

    public void dispose() {
        NavigationPosition center = getCenter();
        preferences.putDouble(CENTER_LATITUDE_PREFERENCE, center.getLatitude());
        preferences.putDouble(CENTER_LONGITUDE_PREFERENCE, center.getLongitude());
        int zoom = getZoom();
        preferences.putInt(CENTER_ZOOM_PREFERENCE, zoom);

        mapView.destroy();
    }

    public Component getComponent() {
        return mapView;
    }

    public void resize() {
        // intentionally left empty
    }

    public void setRecenterAfterZooming(boolean recenterAfterZooming) {
        this.recenterAfterZooming = recenterAfterZooming;
    }

    public void setShowCoordinates(boolean showCoordinates) {
        this.showCoordinates = showCoordinates;
    }

    public void setShowWaypointDescription(boolean showWaypointDescription) {
        this.showWaypointDescription = showWaypointDescription;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public void setAvoidHighways(boolean avoidHighways) {
        this.avoidHighways = avoidHighways;
    }

    public void setAvoidTolls(boolean avoidTolls) {
        this.avoidTolls = avoidTolls;
    }


    private NavigationPosition asPosition(LatLong latLong) {
        return new Wgs84Position(latLong.longitude, latLong.latitude, null, null, null, null);
    }

    private LatLong asLatLong(NavigationPosition position) {
        return new LatLong(position.getLatitude(), position.getLongitude());
    }


    public NavigationPosition getCenter() {
        return asPosition(mapView.getModel().mapViewPosition.getCenter());
    }

    public void setCenter(NavigationPosition center) {
        mapView.getModel().mapViewPosition.setCenter(asLatLong(center));
    }

    private int getZoom() {
        return mapView.getModel().mapViewPosition.getZoomLevel();
    }

    private void setZoom(int zoom) {
        mapView.getModel().mapViewPosition.setZoomLevel((byte) zoom);
    }

    public void print(String title, boolean withDirections) {
        // TODO implement me
    }

    public void insertAllWaypoints(int[] startPositions) {
        // TODO implement me
    }

    public void insertOnlyTurnpoints(int[] startPositions) {
        // TODO implement me
    }

    private void addLinesToMap(List<NavigationPosition> positions) {
        // TODO this just adds lines, find elegant way to update/remove existing lines
        List<LatLong> latLongs = new ArrayList<>(positions.size());
        for (NavigationPosition position : positions) {
            latLongs.add(asLatLong(position));
        }
        mapView.getLayerManager().getLayers().add(new Polylines(latLongs));
    }

    public void setSelectedPositions(int[] selectedPositions, boolean replaceSelection) {
        // TODO this just adds markers, find elegant way to update/remove existing markers
        for (int selectedPosition : selectedPositions) {
            NavigationPosition position = positionsModel.getPosition(selectedPosition);
            Marker marker = new Marker(asLatLong(position), markerIcon, 0, 0);
            mapView.getLayerManager().getLayers().add(marker);
            setCenter(position);
        }
    }

    // listeners

    private final java.util.List<MapViewListener> mapViewListeners = new CopyOnWriteArrayList<MapViewListener>();

    public void addMapViewListener(MapViewListener listener) {
        mapViewListeners.add(listener);
    }

    public void removeMapViewListener(MapViewListener listener) {
        mapViewListeners.remove(listener);
    }

    private void fireCalculatedDistance(int meters, int seconds) {
        for (MapViewListener listener : mapViewListeners) {
            listener.calculatedDistance(meters, seconds);
        }
    }

    private void fireReceivedCallback(int port) {
        for (MapViewListener listener : mapViewListeners) {
            listener.receivedCallback(port);
        }
    }
}
