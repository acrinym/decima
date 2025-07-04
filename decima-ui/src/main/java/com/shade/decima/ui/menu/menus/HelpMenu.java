package com.shade.decima.ui.menu.menus;

import com.shade.decima.ui.Application;
import com.shade.decima.ui.help.HelpViewer;
import com.shade.decima.ui.updater.UpdateService;
import com.shade.platform.ui.menus.*;
import com.shade.platform.ui.menus.Menu;
import com.shade.platform.ui.menus.MenuItem;
import com.shade.platform.ui.util.UIUtils;
import com.shade.util.NotNull;

import javax.swing.*;
import java.text.MessageFormat;
import java.time.ZoneOffset;

import static com.shade.decima.ui.menu.MenuConstants.*;

@MenuRegistration(id = APP_MENU_HELP_ID, name = "&Help", order = 4000)
public final class HelpMenu extends Menu {
    @MenuItemRegistration(parent = APP_MENU_HELP_ID, name = "&Help", keystroke = "F1", group = APP_MENU_HELP_GROUP_HELP, order = 1000)
    public static class HelpItem extends MenuItem {
        @Override
        public void perform(@NotNull MenuItemContext ctx) {
            HelpViewer.showViewer();
        }
    }

    @MenuItemRegistration(parent = APP_MENU_HELP_ID, name = "&Check for Updates\u2026", group = APP_MENU_HELP_GROUP_ABOUT, order = 1000)
    public static class CheckForUpdatesItem extends MenuItem {
        @Override
        public void perform(@NotNull MenuItemContext ctx) {
            UpdateService.getInstance().checkForUpdatesModal(JOptionPane.getRootFrame());
        }
    }

    @MenuItemRegistration(parent = APP_MENU_HELP_ID, name = "&About", group = APP_MENU_HELP_GROUP_ABOUT, order = 2000)
    public static class AboutItem extends MenuItem {
        private static final MessageFormat MESSAGE = new MessageFormat("""
            <h1>{0}</h1>
            A tool for viewing and editing data in games powered by Decima engine.
            <br><br>
            <table>
            <tr><td><b>Version:</b></td><td>{1} (Built on {2,date,short}), commit: <a href="https://github.com/ShadelessFox/decima/commit/{3}">{3}</a></tr>
            <tr><td><b>VM Version:</b></td><td>{4}; {5} ({6} {7})</td></tr>
            <tr><td><b>VM Vendor:</b></td><td>{8}, <a href="{9}">{9}</a></td></tr>
            </table>
            <br>
            See <a href="https://github.com/ShadelessFox/decima">https://github.com/ShadelessFox/decima</a> for more information.
            """);

        @Override
        public void perform(@NotNull MenuItemContext ctx) {
            var properties = System.getProperties();
            var application = Application.getInstance();
            String text = MESSAGE.format(new Object[]{
                application.getTitle(),
                application.getVersion(),
                application.getBuildTime().toEpochSecond(ZoneOffset.UTC) * 1000,
                application.getBuildNumber(),
                properties.get("java.version"),
                properties.get("java.vm.name"),
                properties.get("java.vm.version"),
                properties.get("java.vm.info"),
                properties.get("java.vendor"),
                properties.get("java.vendor.url")
            });

            JOptionPane.showMessageDialog(
                JOptionPane.getRootFrame(),
                UIUtils.createBrowseText(text),
                "About",
                JOptionPane.PLAIN_MESSAGE
            );

        }
    }
}
