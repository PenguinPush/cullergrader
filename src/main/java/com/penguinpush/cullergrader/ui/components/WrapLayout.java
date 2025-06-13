package com.penguinpush.cullergrader.ui.components;

import com.penguinpush.cullergrader.config.AppConstants;
import javax.swing.*;
import java.awt.*;

public class WrapLayout extends FlowLayout {

    int gridMediaWidth;
    int gridMediaHeight;
    boolean singleRow;

    public WrapLayout(int align, boolean singleRow) {
        super(align);
        this.singleRow = singleRow;
    }

    private Dimension layoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Container parent = target.getParent();
            JScrollPane gridScrollPanel = (JScrollPane) parent.getParent();
            JScrollBar verticalScrollBar = gridScrollPanel.getVerticalScrollBar();
            int gridScrollBarWidth = gridScrollPanel.isVisible() ? verticalScrollBar.getWidth() : 0;

            int column = 1;
            int column_max = 1;
            int row = 1;
            int count = target.getComponentCount();
            int maxWidth = target.getParent().getWidth();

            if (!singleRow) {
                // dynamically calculate horizontal gap to fit, only if it's not in single-row form
                setHgap(calculateHgap(maxWidth, gridScrollBarWidth));
            }
            Dimension layoutSize = new Dimension(0, 0);

            for (int index = 0; index < count; index++) {
                Component gridMedia = target.getComponent(index);
                gridMediaWidth = gridMedia.getWidth();
                gridMediaHeight = gridMedia.getHeight();

                if (columnToWidth(column) + gridMediaWidth > maxWidth && !singleRow) {
                    // add to the height and loop back, save max width
                    column_max = Math.max(column, column_max);
                    column = 1;
                    row += 1;
                } else {
                    column += 1;
                }
            }

            if (column == 1) {
                // empty bottom row
                row -= 1;
            }

            if (singleRow) {
                column_max = Math.max(column, column_max) - 1;
            }

            layoutSize.width = columnToWidth(column_max) + getHgap();
            layoutSize.height = rowToHeight(row) + getVgap();
            
            JScrollBar horizontalScrollBar = gridScrollPanel.getHorizontalScrollBar();
            if (horizontalScrollBar.isVisible()) {
                layoutSize.height += horizontalScrollBar.getHeight();
            }

            return layoutSize;
        }
    }

    private int columnToWidth(int column) {
        return (column) * (gridMediaWidth + getHgap());
    }

    private int rowToHeight(int row) {
        return (row) * (gridMediaHeight + getVgap());
    }

    private int calculateHgap(int availableWidth, int scrollBarWidth) {
        int minHgap = AppConstants.GRIDMEDIA_HGAP_MIN;

        int maxColumns = (availableWidth + minHgap - scrollBarWidth) / (gridMediaWidth + minHgap);
        maxColumns = Math.max(1, maxColumns);

        for (int hgap = gridMediaWidth; hgap >= minHgap; hgap--) {
            int totalWidth = maxColumns * gridMediaWidth + (maxColumns + 1) * hgap;
            if (totalWidth <= availableWidth) {
                return hgap;
            }
        }

        return minHgap;
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target);
    }
}
