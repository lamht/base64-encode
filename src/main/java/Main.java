package com.myproject;

import org.apache.commons.codec.binary.Base64;
import com.formdev.flatlaf.FlatDarkLaf;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        // Set FlatLaf Look and Feel (modern look)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create the frame with JGoodies FormLayout
        JFrame frame = new JFrame("Base64 Encoder/Decoder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define the layout for the frame with added margin (using 10dlu and empty row/column)
        FormLayout layout = new FormLayout(
            "10dlu, right:pref, 5dlu, fill:pref:grow, 10dlu",  // Add margin to left and right sides
            "10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu"  // Extra row for bottom margin
        );
        frame.setLayout(layout);

        // Create components
        JLabel inputLabel = new JLabel("Enter Base64:");
        JTextField inputField = new JTextField(20);

        // Create combo box for selecting Encode or Decode
        String[] options = {"Encode", "Decode"};
        JComboBox<String> actionComboBox = new JComboBox<>(options);

        // Create button for processing
        JButton processButton = new JButton("Process");

        // Output section
        JLabel outputLabel = new JLabel("Result:");
        JTextArea outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);

        // Use CellConstraints for proper placement of components
        CellConstraints cc = new CellConstraints();
        frame.add(inputLabel, cc.xy(2, 2));  // Label at (2, 2)
        frame.add(inputField, cc.xy(4, 2));  // Input field at (4, 2)
        frame.add(new JLabel("Action:"), cc.xy(2, 4));  // Action label at (2, 4)
        frame.add(actionComboBox, cc.xy(4, 4));  // ComboBox at (4, 4)
        frame.add(processButton, cc.xy(4, 6));  // Button at (4, 6)
        frame.add(outputLabel, cc.xy(2, 8));  // Output label at (2, 8)
        frame.add(new JScrollPane(outputArea), cc.xy(4, 8));  // Text area at (4, 8)

        // Action listener for the process button
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                String action = (String) actionComboBox.getSelectedItem();
                
                // Run encoding/decoding task in background thread
                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        try {
                            if ("Encode".equals(action)) {
                                // Base64 Encode
                                return Base64.encodeBase64String(inputText.getBytes());
                            } else {
                                // Base64 Decode
                                byte[] decoded = Base64.decodeBase64(inputText);
                                return new String(decoded);
                            }
                        } catch (Exception ex) {
                            return "Error in encoding/decoding!";
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            // Get the result from the background task
                            String result = get();
                            outputArea.setText(result);
                        } catch (Exception ex) {
                            outputArea.setText("Error in encoding/decoding!");
                        }
                    }
                }.execute();  // Execute the background task
            }
        });

        // Finalize and display the frame
        frame.pack();
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true);
    }
}
