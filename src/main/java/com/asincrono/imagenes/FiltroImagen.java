package com.asincrono.imagenes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;


public class FiltroImagen {
    public static BufferedImage toBlackAndWhite(BufferedImage original) {
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color c = new Color(original.getRGB(x, y));
                int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                result.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return result;
    }

    public static BufferedImage toSepia(BufferedImage original) {
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color c = new Color(original.getRGB(x, y));
                int tr = (int)(0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
                int tg = (int)(0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                int tb = (int)(0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());

                Color sepia = new Color(
                        Math.min(255, tr),
                        Math.min(255, tg),
                        Math.min(255, tb)
                );
                result.setRGB(x, y, sepia.getRGB());
            }
        }

        return result;
    }

    public static BufferedImage toSharpen(BufferedImage original) {
        float[] sharpenMatrix = {
                0.0f, -1.0f,  0.0f,
                -1.0f,  5.0f, -1.0f,
                0.0f, -1.0f,  0.0f
        };

        Kernel kernel = new Kernel(3, 3, sharpenMatrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        op.filter(original, result);
        return result;
    }
}
