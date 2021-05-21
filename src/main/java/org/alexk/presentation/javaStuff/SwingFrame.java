package org.alexk.presentation.javaStuff;

import lombok.val;
import org.alexk.business.DeliveryService;

import javax.swing.*;

public class SwingFrame extends JFrame {
    public SwingFrame() {
        val btn = new JButton("Save & Exit");
        btn.addActionListener(e -> {
            DeliveryService.instance.save();
            System.exit(0);
        });
        this.add(btn);

        this.setSize(200, 200);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
