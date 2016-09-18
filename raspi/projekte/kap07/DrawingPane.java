package raspi.projekte.kap07;

import javax.swing.*;
import java.awt.*;

public class DrawingPane extends JPanel {
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(234,23,235));
        g.fillOval(30, 20, 100, 50);
        g.setColor(new Color(23,234,235));
        g.fillRect(10, 10, 20, 20);

    }
}