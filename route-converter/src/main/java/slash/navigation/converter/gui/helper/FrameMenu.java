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

package slash.navigation.converter.gui.helper;

import slash.navigation.converter.gui.RouteConverter;

import javax.swing.*;

/**
 * Creates a {@link JMenuBar} for a {@link RouteConverter}.
 *
 * @author Christian Pesch
 */

public class FrameMenu {
    public JMenuBar createMenuBar() {
        JMenu fileMenu = JMenuHelper.createMenu("file");
        fileMenu.add(JMenuHelper.createItem("new-file"));
        fileMenu.add(JMenuHelper.createItem("open"));
        fileMenu.add(JMenuHelper.createItem("save"));
        fileMenu.add(JMenuHelper.createItem("save-as"));
        fileMenu.add(JMenuHelper.createItem("upload"));
        JMenu printMenu = JMenuHelper.createMenu("print");
        printMenu.add(JMenuHelper.createItem("print-map"));
        printMenu.add(JMenuHelper.createItem("print-map-and-route"));
        printMenu.add(JMenuHelper.createItem("print-elevation-profile"));
        fileMenu.add(printMenu);
        fileMenu.addSeparator();
        // TODO add items for last used files
        fileMenu.add(JMenuHelper.createItem("exit"));

        JMenu editMenu = JMenuHelper.createMenu("edit");
        editMenu.add(JMenuHelper.createItem("cut"));
        editMenu.add(JMenuHelper.createItem("copy"));
        editMenu.add(JMenuHelper.createItem("paste"));
        editMenu.add(JMenuHelper.createItem("select-all"));
        editMenu.addSeparator();
        editMenu.add(JMenuHelper.createItem("new-position"));
        editMenu.add(JMenuHelper.createItem("delete"));
        editMenu.addSeparator();
        JMenu completeMenu = JMenuHelper.createMenu("complete");
        completeMenu.add(JMenuHelper.createItem("add-coordinates"));
        completeMenu.add(JMenuHelper.createItem("add-elevation"));
        completeMenu.add(JMenuHelper.createItem("add-postal-address"));
        completeMenu.add(JMenuHelper.createItem("add-populated-place"));
        completeMenu.add(JMenuHelper.createItem("add-speed"));
        completeMenu.add(JMenuHelper.createItem("add-index"));
        editMenu.add(completeMenu);
        editMenu.addSeparator();
        editMenu.add(JMenuHelper.createItem("split-positionlist"));
        editMenu.add(JMenuHelper.createMenu("merge-positionlist"));
        editMenu.add(JMenuHelper.createItem("import-positionlist"));
        
        JMenu viewMenu = JMenuHelper.createMenu("view");
        viewMenu.add(JMenuHelper.createItem("show-map-and-positionlist"));
        viewMenu.add(JMenuHelper.createItem("maximize-map"));
        viewMenu.add(JMenuHelper.createItem("maximize-positionlist"));

        JMenu toolsMenu = JMenuHelper.createMenu("tools");
        toolsMenu.add(JMenuHelper.createItem("insert-positions"));
        toolsMenu.add(JMenuHelper.createItem("geocode-position"));
        toolsMenu.add(JMenuHelper.createItem("delete-positions"));
        toolsMenu.add(JMenuHelper.createItem("revert-positions"));

        JMenu extrasMenu = JMenuHelper.createMenu("extras");
        extrasMenu.add(JMenuHelper.createItem("options"));

        JMenu helpMenu = JMenuHelper.createMenu("help");
        helpMenu.add(JMenuHelper.createItem("help-topics"));
        helpMenu.add(JMenuHelper.createItem("search-for-updates"));
        helpMenu.add(JMenuHelper.createItem("about"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        menuBar.add(extrasMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        return menuBar;
    }
}
