
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 *
 * @author Lycorice
 * @see https://github.com/145314042/myProject/blob/master/PBO2/src/graphic/Shape.java
 */
public class TestShape extends JPanel {
    @Override
    public void paintComponent (Graphics graphics){
        super.paintComponent(graphics);
        setBackground(Color.CYAN);
        
        graphics.setColor(Color.PINK);
        graphics.fillArc(20,20,100,100,45,180);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Ini adalah Arc", 30, 40);
        graphics.setColor(Color.BLUE);
        graphics.fillOval(100,20,100,150);
        graphics.setColor(Color.CYAN);
        graphics.fillOval(120,45,60,100);
        
        Graphics2D triangle = (Graphics2D) graphics;
        BufferedImage bufferedImage = 
                new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 10, 10);
        g2.setColor(Color.BLUE);
        g2.fillOval(0, 0, 10, 10);
//        g2.setColor(Color.YELLOW);
//        g2.fillOval(0, 0, 8, 8);
        Rectangle2D rect = new Rectangle2D.Double(5, 5, 8, 8);
        triangle.setPaint(new TexturePaint(bufferedImage, rect));
        triangle.fillPolygon(new int[] {320, 230, 270}, new int[] {20, 20, 80}, 3);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame ("DrawingArcs");
        frame.add(new TestShape());
        frame.setSize(400, 250);
        frame.setLocation(470, 260);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

