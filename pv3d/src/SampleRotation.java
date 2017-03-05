
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/*
 * http://stackoverflow.com/questions/20275424/rotating-image-with-affinetransform
 */
public class SampleRotation {

    public static void main(String[] args) {
        new SampleRotation();
    }

    public SampleRotation() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//                    ex.printStackTrace();
//                }

                final RotationPane rotationPane = new RotationPane();
                final JSlider slider = new JSlider(0, 100);
                slider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        double angle = 720d * (slider.getValue() / 100d);
                        rotationPane.setAngle(angle);
                    }
                });
                slider.setValue(0);

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(rotationPane);
                frame.add(slider, BorderLayout.SOUTH);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class RotationPane extends JPanel {

        private BufferedImage img;
        private BufferedImage rotated;
        private double angle;
        private Point clickPoint;

        public RotationPane() {
            try {
                img = ImageIO.read(new File("test.jpg"));
                BufferedImage scaled = new BufferedImage(img.getWidth() / 2, img.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaled.createGraphics();
                g2d.setTransform(AffineTransform.getScaleInstance(0.5d, 0.5d));
                g2d.drawImage(img, 0, 0, this);
                g2d.dispose();
                img = scaled;
                setAngle(0d);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    clickPoint = e.getPoint();
                    repaint();
                }

            });

        }

        public void setAngle(double angle) {
            this.angle = angle;

            double rads = Math.toRadians(getAngle());
            double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
            int w = img.getWidth();
            int h = img.getHeight();
            int newWidth = (int) Math.floor(w * cos + h * sin);
            int newHeight = (int) Math.floor(h * cos + w * sin);

            rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = rotated.createGraphics();
            AffineTransform at = new AffineTransform();
            at.translate((newWidth - w) / 2, (newHeight - h) / 2);

            int x = clickPoint == null ? w / 2 : clickPoint.x;
            int y = clickPoint == null ? h / 2 : clickPoint.y;

            at.rotate(Math.toRadians(getAngle()), x, y);
            g2d.setTransform(at);
            g2d.drawImage(img, 0, 0, this);
            g2d.setColor(Color.RED);
            g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
            g2d.dispose();

            repaint();
        }

        public double getAngle() {
            return angle;
        }

        @Override
        public Dimension getPreferredSize() {
            return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(this), img.getHeight(this));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (rotated != null) {
                Graphics2D g2d = (Graphics2D) g.create();

                int x = (getWidth() - rotated.getWidth()) / 2;
                int y = (getHeight() - rotated.getHeight()) / 2;
                g2d.drawImage(rotated, x, y, this);

                g2d.setColor(Color.RED);

                x = clickPoint == null ? getWidth() / 2 : clickPoint.x;
                y = clickPoint == null ? getHeight()/ 2 : clickPoint.y;

                x -= 5;
                y -= 5;

                g2d.drawOval(x, y, 10, 10);
                g2d.dispose();
            }
        }        
    }    
}
