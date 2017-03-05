import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @see http://stackoverflow.com/questions/21641260/polygon-vertices-as-uv-coordinates
 * @author Administrator
 *
 */
public class Rotation3D extends JPanel{
    Image img;

    BufferedImage rotatedImage;
    final int ROTATION_DEGREES = 30;

    int vanishX = 0;
    int vanishY = 0;
    int vanishZ = -1000;

    public Rotation3D(){

        try {
            //Grabbed an image from the java folder - hopefully your computer has it
            img = ImageIO.read(new File("test.jpg"));
            setPreferredSize(new Dimension(img.getWidth(this) * 2,img.getHeight(this) * 2));

            //Create a buffered image with the appropriate size, and draw the image on it
            BufferedImage shadedImage = new BufferedImage(img.getWidth(this), img.getWidth(this), BufferedImage.TYPE_INT_ARGB);
            shadedImage.getGraphics().drawImage(img, 0, 0, this);
            Raster r = shadedImage.getData();

            //Not really necessary unless you're using Magic Vanishingpoint Technology®
            vanishX = shadedImage.getWidth() /2;
            vanishY = shadedImage.getHeight() /2;

            //Create a Wraster for the transformed image
            WritableRaster wr = r.createCompatibleWritableRaster();

            //Do the transformation
            for(int i = 0; i < shadedImage.getWidth(); i++){
                for(int j = 0; j < shadedImage.getHeight(); j++){
                    //Remapping the pixel based on a matrix rotation
                    int[] result = r.getPixel(i, j, new int[4]);
                    Double radians = Math.toRadians(ROTATION_DEGREES);
                    Double newX, newY, newZ;
                    //newX = ((i-vanishX) * Math.cos(radians)) + vanishX; // places the rotation in the middle of the image
                    // x * cos(θ) + y * 0 + z * sin(θ)
                    newX = i * Math.cos(radians); //places the rotation in the y=0 axis
                    // x * 0 + y * 1 + z * 0
                    newY = j * 1.0;
                    // x * -sin(θ) + y * 0 + z * cos(θ)
                    newZ= i * Math.sin(radians) * -1;

                    //Apply Magic Vanishingpoint Technology®
                    //(Not actually trademarked or correct - just something thrown together)
                    if(newZ < vanishZ){
                        newX = 0.0;
                        newY = 0.0;
                    }else if(newZ < 0){
                        double magicVanish =  newZ / vanishZ;
                        newX += magicVanish * newX;
                        newY += magicVanish * newY;
                    }

                    //Print the pixel if it fits on the screen to the new Raster
                    if(newX > 0 && newX < shadedImage.getWidth() && newY > 0 && newY < shadedImage.getHeight())
                        wr.setPixel(newX.intValue(), newY.intValue(), result);
                }
            }

            //Create an image based on the raster.
            rotatedImage = new BufferedImage(img.getWidth(this), img.getWidth(this), BufferedImage.TYPE_INT_ARGB);
            rotatedImage.setData(wr);

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
        frame.add(new Rotation3D());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
