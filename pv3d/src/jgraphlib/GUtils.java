package jgraphlib;

import java.awt.image.BufferedImage;

/**
 * @author Andrey Kokorev
 *         Created on 31.01.2015.
 * @see https://github.com/Andronnix/JGraphLib
 */
public class GUtils {
    public final static int RED   = 0xFFFF0000;
    public final static int GREEN = 0xFF00FF00;
    public final static int BLUE  = 0xFF0000FF;
    public final static int BLACK = 0xFF000000;

    public static void drawTriangle(BufferedImage img, BufferedImage textureImg,
                                    Vector3d p0, Vector3d p1, Vector3d p2,
                                    Vector3d t0, Vector3d t1, Vector3d t2,
                                    double intensity, double[][] zbuffer)
    {
        if(p0.getY() > p1.getY())
        {
            Vector3d temp = p0; p0 = p1; p1 = temp;
            temp = t0; t0 = t1; t1 = temp;
        }
        if(p0.getY() > p2.getY())
        {
            Vector3d temp = p2; p2 = p0; p0 = temp;
            temp = t2; t2 = t0; t0 = temp;
        }
        if(p1.getY() > p2.getY())
        {
            Vector3d temp = p1; p1 = p2; p2 = temp;
            temp = t1; t1 = t2; t2 = temp;
        }


        double h = p2.getY() - p0.getY();
        if(h == 0)
        {
            return;
        }

        double h1 = p1.getY() - p0.getY();

        Vector3d p3 = p0.plus(p2.minus(p0).product(h1 / h));
        Vector3d t3 = t0.plus(t2.minus(t0).product(h1 / h));

        if(h1 != 0)
        {
            drawTrianglePart(
                    img, textureImg,
                    p0, p1, p3,
                    t0, t1, t3,
                    intensity, zbuffer
            );
        }

        if(h - h1 != 0)
        {
            drawTrianglePart(
                    img, textureImg,
                    p1, p2, p3,
                    t1, t2, t3,
                    intensity, zbuffer
            );
        }

    }

    private static void drawTrianglePart(BufferedImage img, BufferedImage textureImg,
                                         Vector3d p0, Vector3d p1, Vector3d p2,
                                         Vector3d t0, Vector3d t1, Vector3d t2,
                                         double intensity, double[][] zbuffer)
    {
        int yl = (int) p0.getY();
        int yh = (int) p1.getY();
        boolean up = p2.getY() < p1.getY() - 1;
        int h = Math.abs(yh - yl);
        Vector3d v1 = p1.minus(p0);
        Vector3d v2 = (up)? p1.minus(p2) : p2.minus(p0);

        Vector3d tv1 = t1.minus(t0);
        Vector3d tv2 = (up)? t1.minus(t2) : t2.minus(t0);


        int maxw = textureImg.getWidth();
        int maxh = textureImg.getHeight();
        for (int y = yl; y <= yh; y ++)
        {
            double t = Math.abs(y - yl) * 1.0 / h;

            Vector3d l = p0.plus(v1.product(t));
            Vector3d r = (up)? p2.plus(v2.product(t)) : p0.plus(v2.product(t));

            Vector3d tl = t0.plus(tv1.product(t));
            Vector3d tr = (up)? t2.plus(tv2.product(t)) : t0.plus(tv2.product(t));

            int xl = (int)(l.getX());
            int xr = (int)(r.getX());

            if (xl > xr)
            {
                int z = xl; xl = xr; xr = z;
                Vector3d temp = tl; tl = tr; tr = temp;
            }
            Vector3d lr = r.minus(l);
            Vector3d tlr = tr.minus(tl);

            for (int x = xl; x <= xr; x++)
            {
                Vector3d p = l.plus(lr.product((x - xl)/ (1.0 * (xr - xl))));
                Vector3d tp = tl.plus(tlr.product((x - xl)/ (1.0 * (xr - xl))));
                //FIXME:
                if (x >= 0 && x < zbuffer.length && 
                	y >= 0 && y < zbuffer[x].length && 
                	p.getZ() > zbuffer[x][y])
                {
                	//FIXME:
                	//System.out.println("x=" + tp.getX() + ",y=" + (tp.getY() -1) + ",textureImg:" + textureImg.getWidth() + "x" + textureImg.getHeight());
                    int color = multiplyColor(
                    		textureImg.getRGB(Math.min((int)/*Math.round*/(tp.getX()), maxw - 1), Math.min((int)/*Math.round*/(tp.getY()), maxh - 1)),
                    		intensity
                    );
                    img.setRGB(Math.min(x, maxw - 1), Math.min(y, maxh - 1), color);
                    if(x == xl || x == xr || y == yl || y == yh)
                    {
//                        img.setRGB(x, y, RED);
                    }
                    zbuffer[x][y] = p.getZ();
                }
            }
        }
    }

    public static void drawLine(BufferedImage img, Vector3d p0, Vector3d p1, int color)
    {
        int x0 = (int) Math.round(p0.getX());
        int y0 = (int) Math.round(p0.getY());
        int x1 = (int) Math.round(p1.getX());
        int y1 = (int) Math.round(p1.getY());

        int dx = Math.abs(x0 - x1);
        int dy = Math.abs(y0 - y1);
        boolean transposed = false;
        if(dx < dy)
        {
            int t = x0; x0 = y0; y0 = t;
            t = x1; x1 = y1; y1 = t;
            transposed = true;
        }
        if(x0 > x1) {
            int t = x0; x0 = x1; x1 = t;
            t = y0; y0 = y1; y1 = t;
        }

        int error = 0;
        int de = (transposed)? dx : dy;
        int dd = (transposed)? dy : dx;
        int y = y0;
        int dey = (y1 > y0)? 1 : -1;
        for(int x = x0; x <= x1; x++)
        {
            if(!transposed)
            {
            	img.setRGB(x, y, color);
            }
            else
            {
            	img.setRGB(y, x, color);
            }
            error += de;
            if(2 * error > dd)
            {
                y += dey;
                error -= dd;
            }
        }
    }

    public static int multiplyColor(int color, double multiplier)
    {
        int a = colorA(color);
        int r = (int) (colorR(color) * multiplier);
        int g = (int) (colorG(color) * multiplier);
        int b = (int) (colorB(color) * multiplier);

        return ARGB(a, r, g, b);
    }

    public static int colorA(int color)
    {
        return (color & 0xFF000000) >> 24;
    }

    public static int colorR(int color)
    {
        return ((color & 0x00FF0000) >> 16);
    }

    public static int colorG(int color)
    {
        return ((color & 0x0000FF00) >> 8);
    }

    public static int colorB(int color)
    {
        return (color & 0x000000FF);
    }

    public static int ARGB(int a, int r, int g, int b)
    {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
