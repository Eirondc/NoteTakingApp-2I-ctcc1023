package notetakingapp;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;

public class NoteTakingApp {

    private JFrame frame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private Stack<String> undoStack, redoStack;
    private boolean isDarkMode = false;

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(() -> new NoteTakingApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // Create JFrame for the application window
        frame = new JFrame("Note Taking App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the text area for note-taking
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.getDocument().addUndoableEditListener(e -> saveForUndoRedo());

        // Add a scroll pane to the text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Set up file chooser for opening/saving files
        fileChooser = new JFileChooser();

        undoStack = new Stack<>();
        redoStack = new Stack<>();

        // Create menu bar and items
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newNoteItem = new JMenuItem("New");
        JMenuItem openNoteItem = new JMenuItem("Open");
        JMenuItem saveNoteItem = new JMenuItem("Save");
        JMenuItem saveAsNoteItem = new JMenuItem("Save As...");

        newNoteItem.addActionListener(e -> newNote());
        openNoteItem.addActionListener(e -> openNote());
        saveNoteItem.addActionListener(e -> saveNote());
        saveAsNoteItem.addActionListener(e -> saveAsNote());

        fileMenu.add(newNoteItem);
        fileMenu.add(openNoteItem);
        fileMenu.add(saveNoteItem);
        fileMenu.add(saveAsNoteItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");

        undoItem.addActionListener(e -> undo());
        redoItem.addActionListener(e -> redo());

        editMenu.add(undoItem);
        editMenu.add(redoItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem darkModeItem = new JMenuItem("Toggle Dark Mode");
        darkModeItem.addActionListener(e -> toggleDarkMode());

        viewMenu.add(darkModeItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    private void newNote() {
        textArea.setText("");
        currentFile = null;
        undoStack.clear();
        redoStack.clear();
    }

    private void openNote() {
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                undoStack.clear();
                redoStack.clear();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
            }
        }
    }

    private void saveNote() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
            }
        } else {
            saveAsNote();
        }
    }

    private void saveAsNote() {
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
            }
        }
    }

    private void saveForUndoRedo() {
        undoStack.push(textArea.getText());
        if (undoStack.size() > 10) {
            undoStack.remove(0); // Limit the undo stack size to 10
        }
        redoStack.clear(); // Clear redo stack when making new changes
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            String lastState = undoStack.pop();
            redoStack.push(textArea.getText());
            textArea.setText(lastState);
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            String nextState = redoStack.pop();
            undoStack.push(textArea.getText());
            textArea.setText(nextState);
        }
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.WHITE);
            frame.getContentPane().setBackground(Color.DARK_GRAY);
        } else {
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.BLACK);
            frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        }
    }
}
