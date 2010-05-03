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

import slash.common.io.Transfer;
import slash.navigation.gui.Application;
import slash.navigation.gui.Constants;

import javax.help.CSH;
import javax.swing.*;
import java.util.ResourceBundle;

/**
 * A helper for simplified {@link JMenu} operations.
 *
 * @author Christian Pesch
 */

public class JMenuHelper {
    private static String getString(String key) {
        return Application.getInstance().getContext().getBundle().getString(key);
    }

    private static String getOptionalString(String key) {
        ResourceBundle bundle = Application.getInstance().getContext().getBundle();
        return bundle.containsKey(key) ? bundle.getString(key) : null;
    }
    
    public static JMenu createMenu(String name) {
        JMenu menu = new JMenu(getString(name + "-menu"));
        String mnemonic = Transfer.trim(getOptionalString(name + "-menu-mnemonic"));
        if (mnemonic != null && mnemonic.length() > 0)
            menu.setMnemonic(mnemonic.charAt(0));
        CSH.setHelpIDString(menu, name + "-menu");
        return menu;
    }

    public static JMenuItem createItem(String name, Action action) {
        action.putValue(Action.NAME, name);
        JMenuItem item = new JMenuItem(action);
        item.setText(getString(name + "-action"));
        String tooltip = Transfer.trim(getOptionalString(name + "-action-tooltip"));
        if (tooltip != null)
            item.setToolTipText(tooltip);
        String mnemonic = Transfer.trim(getOptionalString(name + "-action-mnemonic"));
        if (mnemonic != null && mnemonic.length() > 0)
            item.setMnemonic(mnemonic.charAt(0));
        String keystroke = Transfer.trim(getOptionalString(name + "-action-keystroke"));
        if (keystroke != null)
            item.setAccelerator(KeyStroke.getKeyStroke(keystroke));
        String iconUrl = Transfer.trim(getOptionalString(name + "-action-icon"));
        if (iconUrl != null)
            item.setIcon(Constants.loadIcon(iconUrl));
        String disabledIconUrl = Transfer.trim(getOptionalString(name + "-action-disabled-icon"));
        if (disabledIconUrl != null)
            item.setDisabledIcon(Constants.loadIcon(disabledIconUrl));
        CSH.setHelpIDString(item, name + "-action");
        return item;
    }
}
