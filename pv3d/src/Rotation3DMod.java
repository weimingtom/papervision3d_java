import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @see http://stackoverflow.com/questions/21641260/polygon-vertices-as-uv-coordinates
 * @author Administrator
 *
 */
public class Rotation3DMod extends JPanel{
	BufferedImage img;

    BufferedImage rotatedImage;
    final int ROTATION_DEGREES = 30;

    int vanishX = 0;
    int vanishY = 0;
    int vanishZ = -1000;

    public Rotation3DMod(){

        try {
            //Grabbed an image from the java folder - hopefully your computer has it
            img = ImageIO.read(new File("test.jpg"));
            setPreferredSize(new Dimension(img.getWidth(this) * 2,img.getHeight(this) * 2));


            double rads = Math.toRadians(ROTATION_DEGREES);
            double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
            int w = img.getWidth();
            int h = img.getHeight();
            int newWidth = (int) Math.floor(w * cos + h * sin);
            int newHeight = (int) Math.floor(h * cos + w * sin);

            rotatedImage = new BufferedImage(img.getWidth(this) * 2, img.getWidth(this) * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotatedImage.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - w) / 2, (newHeight - h) / 2);

            int x = w / 2;
            int y = h / 2;

            at.rotate(Math.toRadians(ROTATION_DEGREES), x, y);
            g2d.setTransform(at);
            g2d.drawImage(img, 0, 0, this);
            g2d.dispose();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(rotatedImage, 0, 0, this);
    }

    public static void main(String[] args){
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Rotation3DMod());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
