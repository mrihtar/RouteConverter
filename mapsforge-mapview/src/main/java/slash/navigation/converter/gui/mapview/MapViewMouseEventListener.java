package slash.navigation.converter.gui.mapview;

import org.mapsforge.map.model.MapViewPosition;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static javax.swing.SwingUtilities.isLeftMouseButton;

public class MapViewMouseEventListener extends MouseAdapter {
    private final MapViewPosition mapViewPosition;
    private Point lastDragPoint;

    public MapViewMouseEventListener(MapViewPosition mapViewPosition) {
        this.mapViewPosition = mapViewPosition;
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (isLeftMouseButton(mouseEvent)) {
            Point point = mouseEvent.getPoint();
            if (lastDragPoint != null) {
                int moveHorizontal = point.x - lastDragPoint.x;
                int moveVertical = point.y - lastDragPoint.y;
                mapViewPosition.moveCenter(moveHorizontal, moveVertical);
            }
            lastDragPoint = point;
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (isLeftMouseButton(mouseEvent)) {
            lastDragPoint = mouseEvent.getPoint();
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        lastDragPoint = null;
    }

    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        byte zoomLevelDiff = (byte) -mouseWheelEvent.getWheelRotation();
        mapViewPosition.zoom(zoomLevelDiff);
    }
}
