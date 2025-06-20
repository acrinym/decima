package com.shade.decima.ui.data.viewer.texture.menu;

import com.shade.decima.ui.data.viewer.texture.TextureViewerPanel;
import com.shade.decima.ui.data.viewer.texture.controls.BufferedImageProvider;
import com.shade.decima.ui.data.viewer.texture.controls.ImagePanel;
import com.shade.platform.ui.controls.FileChooser;
import com.shade.platform.ui.menus.MenuItem;
import com.shade.platform.ui.menus.MenuItemContext;
import com.shade.platform.ui.menus.MenuItemRegistration;
import com.shade.util.NotNull;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.IOException;

import static com.shade.decima.ui.menu.MenuConstants.*;

@MenuItemRegistration(parent = BAR_TEXTURE_VIEWER_BOTTOM_ID, name = "Import Texture\u2026", icon = "Action.importIcon", group = BAR_TEXTURE_VIEWER_BOTTOM_GROUP_GENERAL, order = 2000)
public class ImportTextureItem extends MenuItem {
    @Override
    public void perform(@NotNull MenuItemContext ctx) {
        final JFileChooser chooser = new FileChooser();
        chooser.setDialogTitle("Import texture");

        if (chooser.showOpenDialog(JOptionPane.getRootFrame()) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final ImagePanel panel = ctx.getData(TextureViewerPanel.PANEL_KEY);

        try {
            panel.setProvider(BufferedImageProvider.load(chooser.getSelectedFile()));
            panel.fit();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                JOptionPane.getRootFrame(),
                "Failed to load texture: " + e.getMessage(),
                "Import Texture",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public boolean isEnabled(@NotNull MenuItemContext ctx) {
        return ctx.getData(TextureViewerPanel.PANEL_KEY) != null;
    }
}
