package control.invest.IC.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfToImageService {

    private static final String OUTPUT_DIRECTORY = "C:\\Projects\\project-IC\\IC\\img\\";

    public List<File> convertPdfToImages(File pdfFile, Rectangle region, String name) throws IOException {
        List<File> imageFiles = new ArrayList<>();

        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists()) {
            outputDir.mkdirs();//cria o diretorio
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int pageCount = document.getNumberOfPages();

            int dpi = 300;//ajusta a qualidade da imagem, valores maiores requerem mais tempo pra processar

            for (int i = 0; i < pageCount; i++) {
                float scale = dpi / 72f;

                Rectangle scaledRegion = new Rectangle(
                        (int) (region.x * scale),
                        (int) (region.y * scale),
                        (int) (region.width * scale),
                        (int) (region.height * scale)
                );
                BufferedImage fullImage = pdfRenderer.renderImage(i, scale);

                int imageWidth = fullImage.getWidth();
                int imageHeight = fullImage.getHeight();

                int croppedX = Math.max(0, scaledRegion.x);
                int croppedY = Math.max(0, scaledRegion.y);
                int croppedWidth = Math.min(scaledRegion.width, imageWidth - croppedX);
                int croppedHeight = Math.min(scaledRegion.height, imageHeight - croppedY);

                if (croppedWidth <= 0 || croppedHeight <= 0) {
                    throw new IllegalArgumentException("A região de recorte está fora dos limites da imagem renderizada.");
                }
                BufferedImage croppedImage = pdfRenderer.renderImage(i, scale).getSubimage(scaledRegion.x, scaledRegion.y, scaledRegion.width, scaledRegion.height);


                File outputFile = new File(OUTPUT_DIRECTORY + "page_" + name + i + "_cropped.png");

                ImageIO.write(croppedImage, "png", outputFile);

                imageFiles.add(outputFile);
            }
        }

        return imageFiles;
    }
}
