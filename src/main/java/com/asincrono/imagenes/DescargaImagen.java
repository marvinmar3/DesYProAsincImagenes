package com.asincrono.imagenes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;

public class DescargaImagen {

    public static BufferedImage descargaImagen(String urlStr) throws IOException {
        URL url= new URL(urlStr);
        return ImageIO.read(url);
    }

    public static String extraerNomImagen(String urlStr){
        // extrae el numero del id de la imagen en la url
        String[] parts= urlStr.split("/");
        return "img" + parts[parts.length-3]; //ejemplo img25
    }
}
