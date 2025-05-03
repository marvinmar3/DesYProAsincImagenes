package com.asincrono.imagenes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageSaver {
    public static void saveImage(BufferedImage image, String filename) {
        try {
            Path outputDir = Paths.get("imagenes_filtradas");
            if (!Files.exists(outputDir)) {
                Files.createDirectory(outputDir);
                System.out.println("✅ Carpeta creada: imagenes_filtradas");
            }

            File output = new File(outputDir.toFile(), filename);
            ImageIO.write(image, "jpg", output);
            System.out.println("✅ Imagen guardada: " + filename);

        } catch (IOException e) {
            System.err.println("Error al guardar " + filename + ": " + e.getMessage());
        }
    }
}
