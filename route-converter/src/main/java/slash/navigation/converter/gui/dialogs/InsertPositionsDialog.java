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

package slash.navigation.converter.gui.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import slash.common.io.CompactCalendar;
import slash.navigation.BaseNavigationPosition;
import slash.navigation.BaseRoute;
import slash.navigation.converter.gui.RouteConverter;
import slash.navigation.converter.gui.helper.DialogAction;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.converter.gui.renderer.GoogleMapsPositionListCellRenderer;
import slash.navigation.googlemaps.GoogleMapsPosition;
import slash.navigation.googlemaps.GoogleMapsService;
import slash.navigation.gui.SimpleDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Dialog for inserting {@link slash.navigation.BaseNavigationPosition}s into the current {@link slash.navigation.BaseRoute}.
 *
 * @author Christian Pesch
 */

public class InsertPositionsDialog extends SimpleDialog {
    private JPanel contentPane;

    private JLabel labelSelection;
    private JButton buttonSelectAll;
    private JButton buttonClearSelection;
    private JButton buttonInsertAllWaypoints;
    private JButton buttonInsertOnlyTurnpoints;
    private JLabel labelProgress;
    private JTextField textFieldSearch;
    private JButton buttonSearchPositions;
    private JList listResult;
    private JButton buttonInsertSinglePosition;
    private int insertedPositionsCount = 0;

    public InsertPositionsDialog() {
        super(RouteConverter.getInstance().getFrame(), "insert-positions");
        setTitle(RouteConverter.getBundle().getString("insert-positions-title"));
        setContentPane(contentPane);

        listResult.setCellRenderer(new GoogleMapsPositionListCellRenderer());

        buttonSelectAll.addActionListener(new DialogAction(this) {
            public void run() {
                selectAll();
            }
        });

        buttonClearSelection.addActionListener(new DialogAction(this) {
            public void run() {
                clearSelection();
            }
        });

        buttonInsertAllWaypoints.addActionListener(new DialogAction(this) {
            public void run() {
                insertAllWaypoints();
            }
        });

        buttonInsertOnlyTurnpoints.addActionListener(new DialogAction(this) {
            public void run() {
                insertOnlyTurnpoints();
            }
        });

        buttonSearchPositions.addActionListener(new DialogAction(this) {
            public void run() {
                searchPositions();
            }
        });

        buttonInsertSinglePosition.addActionListener(new DialogAction(this) {
            public void run() {
                insertSinglePosition();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        contentPane.registerKeyboardAction(new DialogAction(this) {
            public void run() {
                close();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        updateSelectionCount();
        RouteConverter.getInstance().getPositionsModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                insertedPositionsCount += e.getLastRow() - e.getFirstRow() + 1;
                labelProgress.setText(MessageFormat.format(RouteConverter.getBundle().getString("insert-added-positions"), insertedPositionsCount));
            }
        });
    }

    private void updateSelectionCount() {
        int selectedPositionCount = RouteConverter.getInstance().getSelectedPositions().length;
        labelSelection.setText(MessageFormat.format(RouteConverter.getBundle().getString("selected-positions"), selectedPositionCount));
    }

    private void selectAll() {
        RouteConverter.getInstance().selectAll();
        int selectedPositionCount = RouteConverter.getInstance().getSelectedPositions().length;
        labelSelection.setText(MessageFormat.format(RouteConverter.getBundle().getString("selected-all-positions"), selectedPositionCount));
    }

    private void clearSelection() {
        RouteConverter.getInstance().clearSelection();
        updateSelectionCount();
    }

    private void insertAllWaypoints() {
        insertedPositionsCount = 0;
        RouteConverter.getInstance().insertAllWaypoints();
    }

    private void insertOnlyTurnpoints() {
        insertedPositionsCount = 0;
        RouteConverter.getInstance().insertOnlyTurnpoints();
    }

    private void searchPositions() {
        DefaultListModel listModel = new DefaultListModel();
        listResult.setModel(listModel);
        GoogleMapsService service = new GoogleMapsService();
        try {
            List<GoogleMapsPosition> positions = service.getPositionsFor(textFieldSearch.getText());
            for (GoogleMapsPosition position : positions) {
                listModel.addElement(position);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, MessageFormat.format(RouteConverter.getBundle().getString("insert-error"), e.getMessage()), getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertSinglePosition() {
        insertedPositionsCount = 0;
        PositionsModel positionsModel = RouteConverter.getInstance().getPositionsModel();
        int[] selectedIndices = RouteConverter.getInstance().getSelectedPositions();
        int selectedIndex = selectedIndices.length > 0 ? selectedIndices[0] : 0;
        Object[] objects = listResult.getSelectedValues();
        for (Object object : objects) {
            GoogleMapsPosition position = (GoogleMapsPosition) object;
            positionsModel.add(selectedIndex + 1, position.getLongitude(), position.getLatitude(), position.getElevation(),
                    null, CompactCalendar.getInstance(), position.getComment());
        }
    }

    private void savePreferences() {
    }

    private void close() {
        savePreferences();
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(10, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 20), null, null, 0, false));
        labelSelection = new JLabel();
        labelSelection.setText("-");
        panel2.add(labelSelection, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("selection"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSelectAll = new JButton();
        this.$$$loadButtonText$$$(buttonSelectAll, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("select-all"));
        panel3.add(buttonSelectAll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonClearSelection = new JButton();
        this.$$$loadButtonText$$$(buttonClearSelection, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("clear-selection"));
        panel3.add(buttonClearSelection, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 10), null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonInsertAllWaypoints = new JButton();
        buttonInsertAllWaypoints.setText("alle Wegpunkte");
        panel5.add(buttonInsertAllWaypoints, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonInsertOnlyTurnpoints = new JButton();
        buttonInsertOnlyTurnpoints.setText("nur Abbiegepunkte");
        panel5.add(buttonInsertOnlyTurnpoints, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Einf�gen der Wegpunkte aus der Routenberechnung zwischen den markierten Positionen...");
        panel5.add(label2, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 10), null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Einf�gen von einzelnen Positionen per Suche nach:");
        panel7.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldSearch = new JTextField();
        panel7.add(textFieldSearch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonSearchPositions = new JButton();
        buttonSearchPositions.setText("Suchen");
        panel7.add(buttonSearchPositions, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel8, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonInsertSinglePosition = new JButton();
        buttonInsertSinglePosition.setText("Einf�gen");
        panel8.add(buttonInsertSinglePosition, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel8.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel9, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel9.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listResult = new JList();
        scrollPane1.setViewportView(listResult);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel10, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelProgress = new JLabel();
        labelProgress.setText("-");
        panel10.add(labelProgress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("slash/navigation/converter/gui/RouteConverter").getString("progress"));
        panel10.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel11, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 10), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
