package io.github.malenkix.pdfimages.ui;

import io.github.malenkix.pdfimages.i18n.Bundle;
import io.github.malenkix.pdfimages.i18n.I18n;
import io.github.malenkix.pdfimages.viewmodels.PdfImage;
import io.github.malenkix.pdfimages.viewmodels.PdfObject;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
import io.github.malenkix.swing.BorderPanel;
import io.github.malenkix.swing.Borders;
import io.github.malenkix.swing.Dialogs;
import io.github.malenkix.swing.Panel;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

/**
 *
 * @author Maik
 */
public class PdfEditorPanel extends BorderPanel {

    private final JCheckBox detailRemovePageCheckBox = new JCheckBox();
    private final BorderPanel optionsHelperPanel = new BorderPanel(5, 5);
    private final Panel optionsPanel = new Panel(new GridLayout(0, 2, 5, 5));
    private final ButtonGroup optionButtonGroup = new ButtonGroup();
    private final JRadioButton optionNoneButton = new JRadioButton();
    private final JRadioButton optionBlankButton = new JRadioButton();
    private final JRadioButton optionWhiteButton = new JRadioButton();
    private final JRadioButton optionColorButton = new JRadioButton();
    private final BorderPanel optionColorPreviewPanel = new BorderPanel(5, 5);
    private final JRadioButton optionBorderButton = new JRadioButton();
    private final JRadioButton optionCircleButton = new JRadioButton();
    private final JButton optionApplyButton = new JButton();

    private final PdfScrollPane pdfScrollPane;
    private JFrame frame;

    private PdfObject object;

    public PdfEditorPanel(PdfScrollPane pdfScrollPane) {
        super(5, 5);
        this.pdfScrollPane = pdfScrollPane;
        optionsPanel.setBorder(Borders.compound(Borders.matteGray(1, 0, 0, 0), Borders.empty(5, 0, 0, 0)));
        optionButtonGroup.add(optionNoneButton);
        optionButtonGroup.add(optionBlankButton);
        optionButtonGroup.add(optionWhiteButton);
        optionButtonGroup.add(optionColorButton);
        optionButtonGroup.add(optionBorderButton);
        optionButtonGroup.add(optionCircleButton);
        optionNoneButton.setSelected(true);
        optionColorPreviewPanel.setBorder(Borders.lineGray());
        super.addTop(detailRemovePageCheckBox);
        super.addCenter(optionsHelperPanel);
        optionsHelperPanel.addTop(optionsPanel);
        optionsPanel.add(optionNoneButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionBlankButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionWhiteButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionColorButton);
        optionsPanel.add(optionColorPreviewPanel);
        optionsPanel.add(optionBorderButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionCircleButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionApplyButton);
        detailRemovePageCheckBox.addActionListener(e -> {
            if (object != null && object.isPage()) {
                ((PdfPage) object).setForRemoval(detailRemovePageCheckBox.isSelected());
                pdfScrollPane.requestListFocus();
                pdfScrollPane.requestListRepaint();
            }
        });
        optionNoneButton.addActionListener(e -> onOptionSelected());
        optionBlankButton.addActionListener(e -> onOptionSelected());
        optionWhiteButton.addActionListener(e -> onOptionSelected());
        optionColorButton.addActionListener(e -> onOptionSelected());
        optionBorderButton.addActionListener(e -> onOptionSelected());
        optionCircleButton.addActionListener(e -> onOptionSelected());
        optionApplyButton.addActionListener(e -> {
            if (object != null && !object.isPage()) {
                final PdfImage imageModel = (PdfImage) object;
                pdfScrollPane.getModels(object::hasSameOriginalAs).forEach(other -> {
                    if (other instanceof PdfImage) {
                        final PdfImage otherImageModel = (PdfImage) other;
                        otherImageModel.setBlank(imageModel.isBlank());
                        otherImageModel.setColor(imageModel.getColor());
                        otherImageModel.setBorder(imageModel.hasBorder());
                        otherImageModel.setEllipsis(imageModel.hasEllipsis());
                    }
                });
                pdfScrollPane.requestListFocus();
                pdfScrollPane.requestListRepaint();
            }
        });
    }

    private void onOptionSelected() {
        if (object != null && !object.isPage()) {
            final PdfImage imageModel = (PdfImage) object;
            imageModel.setBlank(false);
            imageModel.setColor(null);
            imageModel.setBlank(false);
            imageModel.setEllipsis(false);
            optionColorPreviewPanel.setBackground(null);
            if (optionBlankButton.isSelected()) {
                imageModel.setBlank(true);
            } else if (optionWhiteButton.isSelected()) {
                imageModel.setColor(Color.WHITE);
            } else if (optionColorButton.isSelected()) {
                final Color color = Dialogs.chooseColor(frame, Color.WHITE);
                imageModel.setColor(color);
                if (color == null) {
                    optionNoneButton.setSelected(true);
                }
                optionColorPreviewPanel.setBackground(color);
            } else if (optionBorderButton.isSelected()) {
                imageModel.setBorder(true);
            } else if (optionCircleButton.isSelected()) {
                imageModel.setEllipsis(true);
            }
            pdfScrollPane.requestListRepaint();
        }
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setObject(PdfObject object) {
        this.object = object;
        setPageControlsEnabled(false);
        setImageControlsEnabled(false);
        detailRemovePageCheckBox.setSelected(false);
        optionButtonGroup.clearSelection();
        optionColorPreviewPanel.setBackground(null);
        if (object != null) {
            setPageControlsEnabled(object.isPage());
            detailRemovePageCheckBox.setSelected(object.isForRemoval());
            setImageControlsEnabled(!object.isPage() && !object.isForRemoval());
            if (!object.isPage()) {
                final PdfImage imageModel = (PdfImage) object;
                optionNoneButton.setSelected(!imageModel.hasUpdate());
                optionBlankButton.setSelected(imageModel.isBlank());
                optionWhiteButton.setSelected(imageModel.isWhite());
                optionColorButton.setSelected(imageModel.hasColor() && !imageModel.isWhite());
                optionColorPreviewPanel.setBackground(imageModel.hasColor() && !imageModel.isWhite() ? imageModel.getColor() : null);
                optionBorderButton.setSelected(imageModel.hasBorder());
                optionCircleButton.setSelected(imageModel.hasEllipsis());
            }
        }
    }

    public void setPageControlsEnabled(boolean enabled) {
        detailRemovePageCheckBox.setEnabled(enabled);
    }

    public void setImageControlsEnabled(boolean enabled) {
        optionNoneButton.setEnabled(enabled);
        optionBlankButton.setEnabled(enabled);
        optionWhiteButton.setEnabled(enabled);
        optionColorButton.setEnabled(enabled);
        optionBorderButton.setEnabled(enabled);
        optionCircleButton.setEnabled(enabled);
        optionApplyButton.setEnabled(enabled);
    }

    public void localize() {
        final Bundle bundle = I18n.getCurrentBundle();
        detailRemovePageCheckBox.setText(bundle.getRemovePage());
        optionNoneButton.setText(bundle.getOptionNone());
        optionBlankButton.setText(bundle.getOptionBlank());
        optionWhiteButton.setText(bundle.getOptionWhite());
        optionColorButton.setText(bundle.getOptionColor());
        optionBorderButton.setText(bundle.getOptionBorder());
        optionCircleButton.setText(bundle.getOptionCircle());
        optionApplyButton.setText(bundle.getOptionApply());
    }
}
