package com.lootlookup.views;

import com.lootlookup.LootLookupConfig;
import com.lootlookup.osrswiki.WikiItem;
import com.lootlookup.utils.Util;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.lootlookup.utils.Icons.COLLAPSE_ICON;
import static com.lootlookup.utils.Icons.EXPAND_ICON;

public class TableBox extends JPanel {
    private LootLookupConfig config;

    private WikiItem[] items;
    private ViewOption viewOption;
    private String fullHeaderStr;
    private String headerStr;
    private JButton percentBtn;

    private final JButton collapseBtn = new JButton();
    private final JPanel listViewContainer = new JPanel();
    private JPanel gridViewPanel = new JPanel();
    private final JPanel headerContainer = new JPanel();
    private final JPanel leftHeader = new JPanel();

    private final Color HEADER_BG_COLOR = ColorScheme.DARKER_GRAY_COLOR.darker();
    private final Color HOVER_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR.darker();

    private final List<WikiItemPanel> itemPanels = new ArrayList<>();
    private static int maxHeaderLength = 28;

    public TableBox(LootLookupConfig config, WikiItem[] items, ViewOption viewOption, String headerStr, JButton percentButton) {
        this.config = config;
        this.items = items;
        this.fullHeaderStr = headerStr;
        this.headerStr = headerStr;
        this.viewOption = viewOption;
        this.percentBtn = percentButton;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        buildHeader();
        buildItemsContainer();
    }

    void buildHeader() {
        buildLeftHeader();
        buildHeaderContainer();
    }

    void buildLeftHeader() {
        // Label

        if (headerStr.length() > maxHeaderLength) {
            headerStr = headerStr.substring(0, maxHeaderLength) + "…"; // Manually truncate the header
        }

        JLabel headerLabel = new JLabel(headerStr);
        headerLabel.setFont(FontManager.getRunescapeBoldFont());
        headerLabel.setForeground(ColorScheme.BRAND_ORANGE);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);

        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));
        leftHeader.setBackground(HEADER_BG_COLOR);

        buildCollapseBtn();

        leftHeader.add(Box.createRigidArea(new Dimension(5, 0)));
        leftHeader.add(collapseBtn);
        leftHeader.add(Box.createRigidArea(new Dimension(10, 0)));
        leftHeader.add(headerLabel);

    }

    void buildCollapseBtn() {

        SwingUtil.removeButtonDecorations(collapseBtn);
        collapseBtn.setIcon(EXPAND_ICON);
        collapseBtn.setSelectedIcon(COLLAPSE_ICON);
        SwingUtil.addModalTooltip(collapseBtn, "Expand Table", "Collapse Table");
        collapseBtn.setBackground(HEADER_BG_COLOR);
        collapseBtn.setUI(new BasicButtonUI()); // substance breaks the layout
        collapseBtn.addActionListener(evt -> toggleCollapse());
        Util.showHandCursorOnHover(collapseBtn);
    }

    void buildHeaderContainer() {
        headerContainer.setLayout(new BorderLayout());
        headerContainer.setBackground(HEADER_BG_COLOR);
        headerContainer.setPreferredSize(new Dimension(0, 40));

        Util.showHandCursorOnHover(headerContainer);
        headerContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                toggleCollapse();
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                headerContainer.setBackground(HOVER_COLOR);
                leftHeader.setBackground(HOVER_COLOR);
                collapseBtn.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                headerContainer.setBackground(HEADER_BG_COLOR);
                leftHeader.setBackground(HEADER_BG_COLOR);
                collapseBtn.setBackground(HEADER_BG_COLOR);
            }
        });
        if (headerStr.endsWith("…")) {
            // If header is truncated, show the full text on hover
            headerContainer.setToolTipText(fullHeaderStr);
        }

        headerContainer.add(leftHeader, BorderLayout.WEST);
        add(headerContainer);

    }

    void buildItemsContainer() {
        switch (viewOption) {
            case LIST:
                int i = 0;

                for (WikiItem item : items) {
                    WikiItemPanel itemPanel = new WikiItemPanel(item, config, i > 0, percentBtn);
                    itemPanels.add(itemPanel);
                    listViewContainer.add(itemPanel);
                    i++;
                }

                listViewContainer.setLayout(new BoxLayout(listViewContainer, BoxLayout.Y_AXIS));
                add(listViewContainer);
                break;
            case GRID:
                gridViewPanel = new GridPanel(items, config, percentBtn);
                add(gridViewPanel);
                break;
        }
    }


    void collapse() {
        if (!isCollapsed()) {
            collapseBtn.setSelected(true);
            listViewContainer.setVisible(false);
            gridViewPanel.setVisible(false);
        }
    }

    void expand() {
        if (isCollapsed()) {
            collapseBtn.setSelected(false);
            listViewContainer.setVisible(true);
            gridViewPanel.setVisible(true);
        }
    }

    void toggleCollapse() {
        if (isCollapsed()) {
            expand();
        } else {
            collapse();
        }
    }

    boolean isCollapsed() {
        return collapseBtn.isSelected();
    }
}
