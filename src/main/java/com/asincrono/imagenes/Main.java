package com.asincrono.imagenes;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> urls = Files.readAllLines(Paths.get("urls.txt"));

            // 🧵 Executor para descargas
            ExecutorService executorDescarga = Executors.newFixedThreadPool(5);

            // 🧵 Executor para filtros
            ExecutorService executorFiltros = Executors.newFixedThreadPool(10);

            for (String url : urls) {
                if (url.trim().isEmpty()) continue; //salta las lineas vacias
                executorDescarga.submit(() -> {
                    try {
                        BufferedImage image = DescargaImagen.descargaImagen(url);
                        String name = DescargaImagen.extraerNomImagen(url);

                        // Usamos executorFiltros para aplicar filtros en paralelo
                        ProcesadorFiltro.applyAllFiltersAsync(image, name, executorFiltros);

                    } catch (Exception e) {
                        System.out.println("Error con la imagen: " + url);
                        e.printStackTrace();
                    }
                });
            }

            // Esperar a que terminen TODAS las descargas antes de apagar el executor de filtros
            executorDescarga.shutdown();
            executorDescarga.awaitTermination(10, TimeUnit.MINUTES); // ajusta según tu caso

            executorFiltros.shutdown();
            executorFiltros.awaitTermination(10, TimeUnit.MINUTES);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al leer urls.txt o interrupción: " + e.getMessage());
        }
    }
}