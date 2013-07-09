package slash.navigation.converter.gui.mapview;

import org.mapsforge.map.model.MapViewModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MapViewComponentListener extends ComponentAdapter {
    private final AwtGraphicMapView mapView;
    private final MapViewModel mapViewModel;

    public MapViewComponentListener(AwtGraphicMapView mapView, MapViewModel mapViewModel) {
        this.mapView = mapView;
        this.mapViewModel = mapViewModel;
    }

    public void componentResized(ComponentEvent componentEvent) {
        Dimension size = mapView.getSize();
        mapViewModel.setDimension(new org.mapsforge.core.model.Dimension(size.width, size.height));
    }
}
