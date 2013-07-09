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

import slash.navigation.base.NavigationPosition;
import slash.navigation.converter.gui.augment.PositionAugmenter;
import slash.navigation.converter.gui.models.CharacteristicsModel;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.converter.gui.models.PositionsSelectionModel;
import slash.navigation.converter.gui.models.UnitSystemModel;

import java.awt.*;

/**
 * Implementation for a component that displays the positions of a position list on a map
 * using the rewrite branch of the mapsforge project.
 *
 * @author Christian Pesch
 */

public class MapsforgeMapView implements MapView {
    public void initialize(PositionsModel positionsModel, PositionsSelectionModel positionsSelectionModel, CharacteristicsModel characteristicsModel, PositionAugmenter positionAugmenter, boolean recenterAfterZooming, boolean showCoordinates, boolean showWaypointDescription, TravelMode travelMode, boolean avoidHighways, boolean avoidTolls, UnitSystemModel unitSystemModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isSupportedPlatform() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isInitialized() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Throwable getInitializationCause() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Component getComponent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setRecenterAfterZooming(boolean recenterAfterZooming) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setShowCoordinates(boolean showCoordinates) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setShowWaypointDescription(boolean showWaypointDescription) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setTravelMode(TravelMode travelMode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAvoidHighways(boolean avoidHighways) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAvoidTolls(boolean avoidTolls) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public NavigationPosition getCenter() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setCenter(NavigationPosition center) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void print(String title, boolean withDirections) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addMapViewListener(MapViewListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeMapViewListener(MapViewListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void insertAllWaypoints(int[] startPositions) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void insertOnlyTurnpoints(int[] startPositions) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setSelectedPositions(int[] selectedPositions, boolean replaceSelection) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
