package io.github.malenkix.pdfimages;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Maik
 */
public class PdfList extends JScrollPane {

    private final DefaultListModel<PdfModel> models = new DefaultListModel<>();
    private final JList<PdfModel> list = new JList<>(models);

    public PdfList() {
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(16);
        super.setViewportView(list);
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
        list.setCellRenderer((list, value, index, selected, hasFocus) -> {

            final JLabel label = new JLabel(new ImageIcon(value.getPreview()));
            label.setEnabled(list.isEnabled());

            final boolean forRemoval = value.isForRemoval() || (value.getParent() != null && value.getParent().isForRemoval());
            final boolean edited = !value.isPage() && (value.isBlank() || value.getColor() != null);

            if (forRemoval && selected) {
                label.setBorder(new CompoundBorder(new LineBorder(Color.RED, 2), new EmptyBorder(3, 3, 3, 3)));
            } else if (forRemoval) {
                label.setBorder(new CompoundBorder(BorderFactory.createDashedBorder(Color.RED, 2f, 1f, 1f, false), new EmptyBorder(3, 3, 3, 3)));
            } else if (selected && edited) {
                label.setBorder(new CompoundBorder(new LineBorder(Color.ORANGE, 2), new EmptyBorder(3, 3, 3, 3)));
            } else if (edited) {
                label.setBorder(new CompoundBorder(BorderFactory.createDashedBorder(Color.ORANGE, 2f, 1f, 1f, false), new EmptyBorder(3, 3, 3, 3)));
            } else if (selected) {
                label.setBorder(new CompoundBorder(new LineBorder(Color.BLUE, 2), new EmptyBorder(3, 3, 3, 3)));
            } else if (hasFocus) {
                label.setBorder(new CompoundBorder(new LineBorder(Color.BLUE, 1), new EmptyBorder(4, 4, 4, 4)));
            } else {
                label.setBorder(new EmptyBorder(5, 5, 5, 5));
            }
            return label;
        });
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.setDragEnabled(false);
    }

    public void setListEnabled(boolean enabled) {
        list.setEnabled(enabled);
    }

    public void addListSelectionListener(Runnable runnable) {
        list.addListSelectionListener(e -> runnable.run());
    }

    public PdfModel getListSelectedValue() {
        return list.getSelectedValue();
    }

    public void requestFocusForList() {
        list.requestFocus();
    }

    public void requestRepaintForList() {
        list.repaint();
    }

    public List<PdfModel> getListModels() {
        return Collections.list(models.elements());
    }

    public void clearListModels() {
        models.clear();
    }
    
    public void addListModel(PdfModel model) {
        models.addElement(model);
    }
    
    public void clearListSelection() {
        list.clearSelection();
    }
}
