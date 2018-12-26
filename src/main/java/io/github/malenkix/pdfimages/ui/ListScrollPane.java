package io.github.malenkix.pdfimages.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Maik
 * @param <T>
 */
public class ListScrollPane<T> extends JScrollPane {

    public static final int VERTICAL = JList.VERTICAL;
    public static final int VERTICAL_WRAP = JList.VERTICAL_WRAP;
    public static final int HORIZONTAL_WRAP = JList.HORIZONTAL_WRAP;

    private final DefaultListModel<T> models = new DefaultListModel<>();
    private final JList<T> list = new JList<>(models);

    public ListScrollPane(int layoutOrientation) {
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(16);
        super.setViewportView(list);
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(layoutOrientation);
        list.setVisibleRowCount(-1);
        list.setDragEnabled(false);
    }

    public ListScrollPane() {
        this(JList.VERTICAL);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        list.setEnabled(enabled);
    }

    public void requestListFocus() {
        list.requestFocus();
    }

    public void requestListRepaint() {
        list.repaint();
    }

    public void clear() {
        list.clearSelection();
        models.clear();
    }

    public void clearSelection() {
        list.clearSelection();
    }

    public void addSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }

    public void setCellRenderer(ListCellRenderer<? super T> cellRenderer) {
        list.setCellRenderer(cellRenderer);
    }

    public List<T> getModels() {
        return Collections.list(models.elements());
    }

    public List<T> getModels(Predicate<T> predicate) {
        return ListScrollPane.this.getModels().stream().filter(predicate).collect(Collectors.toList());
    }

    public T getSelectedValue() {
        return list.getSelectedValue();
    }

    public void addModel(T model) {
        if (model != null) {
            models.addElement(model);
        }
    }

    public void addModels(T[] models) {
        if (models != null) {
            for (final T model : models) {
                addModel(model);
            }
        }
    }

    public void addModels(Collection<T> models) {
        if (models != null) {
            models.forEach(this::addModel);
        }
    }
}
