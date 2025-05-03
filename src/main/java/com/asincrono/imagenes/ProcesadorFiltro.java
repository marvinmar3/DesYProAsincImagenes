package com.asincrono.imagenes;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;



public class ProcesadorFiltro {
    public static void applyAllFiltersAsync(BufferedImage image, String baseName, ExecutorService executor ) {
        executor.submit(() -> {
            try{
                BufferedImage bw = applyBlackAndWhite(image);
                ImageSaver.saveImage(bw, baseName + "_bw.jpg");
                System.out.println("🟦 Filtro bw aplicado a: " + baseName);
            }catch(Exception e){
                System.err.println("❌ Error al aplicar bw a " + baseName + ": " + e.getMessage());
            }

        });
        executor.submit(() -> {
            try{
                BufferedImage sepia = applySepia(image);
                ImageSaver.saveImage(sepia, baseName + "_sepia.jpg");
                System.out.println("🟦 Filtro sepia aplicado a: " + baseName);
            }catch(Exception e){
                System.err.println("❌ Error al aplicar sepia a " + baseName + ": " + e.getMessage());
            }

        });
        executor.submit(() -> {
            try {
                BufferedImage sharp = applySharpen(image);
                ImageSaver.saveImage(sharp, baseName + "_sharpen.jpg");
                System.out.println("🟦 Filtro sharpen aplicado a: " + baseName);
            } catch (Exception e) {
                System.err.println("❌ Error al aplicar sharpen a " + baseName + ": " + e.getMessage());
                e.printStackTrace();
            }
        });

    }

    public static BufferedImage applyBlackAndWhite(BufferedImage original) {
        BufferedImage resultado = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color c = new Color(original.getRGB(x, y));
                int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                Color gris = new Color(gray, gray, gray);
                resultado.setRGB(x, y, gris.getRGB());
            }
        }
        return resultado;
    }


    public static BufferedImage applySepia(BufferedImage original) {
        BufferedImage resultado = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                Color c = new Color(original.getRGB(x, y));
                int tr= (int)(0.393 * c.getRed()+ 0.769 * c.getGreen()+ 0.189 * c.getBlue());
                int tg = (int)(0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                int tb= (int)(0.272* c.getRed()+0.534*c.getGreen()+0.131 * c.getBlue());
                resultado.setRGB(x, y, new Color(
                        Math.min(255, tr),
                        Math.min(255, tg),
                        Math.min(255,tb)).getRGB()
                );
            }
        }
        return resultado;
    }

    public static BufferedImage applySharpen(BufferedImage original) {
        float[] sharpenMatrix = {
                0.0f, -1.0f,  0.0f,
                -1.0f,  5.0f, -1.0f,
                0.0f, -1.0f,  0.0f
        };

        Kernel kernel = new Kernel(3, 3, sharpenMatrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        // Convertir la imagen a un tipo compatible
        BufferedImage compatible = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compatible.createGraphics();
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();

        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
        op.filter(compatible, result);

        return result;
    }
}
