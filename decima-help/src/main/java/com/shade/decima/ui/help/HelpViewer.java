package com.shade.decima.ui.help;

import com.shade.platform.ui.util.UIUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple dialog that shows bundled wiki pages offline.
 */
public class HelpViewer extends JDialog {
    private final JEditorPane viewer;
    private final Map<String, String> pages = new LinkedHashMap<>();

    public HelpViewer() {
        super((Frame) null, "Decima Help", false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);

        viewer = UIUtils.createText("", new InternalLinkListener());
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createIndex(), new JScrollPane(viewer)));
    }

    private JComponent createIndex() {
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String name : new String[]{
                "Home", "Getting-started", "CLI", "Core-editor", "Corefiles", "Model-export", "Archives", "Troubleshooting"}) {
            pages.put(name.replace('-', ' '), "/wiki/" + name + ".md");
            model.addElement(name.replace('-', ' '));
        }

        JList<String> list = new JList<>(model);
        list.addListSelectionListener(e -> loadPage(pages.get(list.getSelectedValue())));
        list.setSelectedIndex(0);
        return new JScrollPane(list);
    }

    private void loadPage(String resource) {
        if (resource == null) {
            viewer.setText("<html><body>No page</body></html>");
            return;
        }
        try (InputStream is = HelpViewer.class.getResourceAsStream(resource)) {
            if (is == null) {
                viewer.setText("<html><body>Page not found: " + resource + "</body></html>");
            } else {
                byte[] data = is.readAllBytes();
                String markdown = new String(data);
                String html = toHtml(markdown);
                viewer.setText(html);
                viewer.setCaretPosition(0);
            }
        } catch (IOException e) {
            viewer.setText("<html><body>Error loading page: " + e.getMessage() + "</body></html>");
        }
    }

    private static String toHtml(String md) {
        String html = md;
        html = html.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        html = html.replaceAll("(?m)^###### (.*)$", "<h6>$1</h6>");
        html = html.replaceAll("(?m)^##### (.*)$", "<h5>$1</h5>");
        html = html.replaceAll("(?m)^#### (.*)$", "<h4>$1</h4>");
        html = html.replaceAll("(?m)^### (.*)$", "<h3>$1</h3>");
        html = html.replaceAll("(?m)^## (.*)$", "<h2>$1</h2>");
        html = html.replaceAll("(?m)^# (.*)$", "<h1>$1</h1>");
        html = html.replaceAll("\*\*(.+?)\*\*", "<b>$1</b>");
        html = html.replaceAll("\*(.+?)\*", "<i>$1</i>");
        html = html.replaceAll("`([^`]+)`", "<code>$1</code>");
        html = html.replaceAll("\n\n", "</p><p>");
        html = html.replaceAll("\n", "<br>");
        html = html.replaceAll("\[([^\]]+)\]\(([^)]+)\)", "<a href=\"$2\">$1</a>");
        html = "<html><body><p>" + html + "</p></body></html>";
        return html;
    }

    private class InternalLinkListener implements HyperlinkListener {
        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                return;
            }
            String desc = e.getDescription();
            // internal link to page
            if (pages.containsKey(desc)) {
                loadPage(pages.get(desc));
            } else if (e.getURL() != null) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    UIUtils.showErrorDialog(ex, "Unable to open " + e.getURL());
                }
            }
        }
    }

    public static void showViewer() {
        new HelpViewer().setVisible(true);
    }
}
