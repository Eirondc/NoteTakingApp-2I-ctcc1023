import javax.swing.*;
import java.awt.*;

public class FontChooserDialog {

    public static Font showDialog(JFrame parent, Font currentFont) {
        // Create dialog box
        JDialog fontDialog = new JDialog(parent, "Choose Font", true);
        fontDialog.setLayout(new FlowLayout());

        // Font family options
        String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontFamilyComboBox = new JComboBox<>(fontFamilies);
        fontFamilyComboBox.setSelectedItem(currentFont.getFamily());

        // Font size options
        Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36};
        JComboBox<Integer> fontSizeComboBox = new JComboBox<>(fontSizes);
        fontSizeComboBox.setSelectedItem(currentFont.getSize());

        // Style options
        String[] styles = {"Plain", "Bold", "Italic"};
        JComboBox<String> styleComboBox = new JComboBox<>(styles);
        int styleIndex = currentFont.getStyle();
        styleComboBox.setSelectedIndex(styleIndex == Font.BOLD ? 1 : styleIndex == Font.ITALIC ? 2 : 0);

        // Preview label
        JLabel previewLabel = new JLabel("The quick brown fox jumps over the lazy dog.");
        previewLabel.setFont(currentFont);

        // Button to confirm font choice
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> fontDialog.dispose());

        // Add components to dialog
        fontDialog.add(new JLabel("Font Family"));
        fontDialog.add(fontFamilyComboBox);
        fontDialog.add(new JLabel("Font Size"));
        fontDialog.add(fontSizeComboBox);
        fontDialog.add(new JLabel("Font Style"));
        fontDialog.add(styleComboBox);
        fontDialog.add(previewLabel);
        fontDialog.add(okButton);

        // Positioning dialog
        fontDialog.setSize(400, 250);
        fontDialog.setLocationRelativeTo(parent);
        fontDialog.setVisible(true);

        // Return selected font
        String selectedFontFamily = (String) fontFamilyComboBox.getSelectedItem();
        int selectedFontSize = (Integer) fontSizeComboBox.getSelectedItem();
        int selectedFontStyle = styleComboBox.getSelectedIndex() == 1 ? Font.BOLD : styleComboBox.getSelectedIndex() == 2 ? Font.ITALIC : Font.PLAIN;

        return new Font(selectedFontFamily, selectedFontStyle, selectedFontSize);
    }
}
