package com.penguinpush.cullergrader;

import com.penguinpush.cullergrader.logic.*;
import com.penguinpush.cullergrader.ui.GroupGridFrame;
import com.penguinpush.cullergrader.media.*;
import java.util.List;
import java.io.File;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        GroupingEngine groupingEngine = new GroupingEngine();
        List<PhotoGroup> groups = FileUtils.loadFolder(new File(""), groupingEngine);

        ImageLoader imageLoader = new ImageLoader();
        SwingUtilities.invokeLater(() -> new GroupGridFrame(groups, imageLoader, groupingEngine));
    }
}
