package com.excilys.shooflers.dashboard.server.converter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Loïc Ortola on 14/06/2016.
 */
public class PowerpointToPdfConverter implements ContentConverter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PowerpointToPdfConverter.class);
    private static final int MAX_WIDTH_TO_SCALE = 600;

    @Override
    public String getOutputExtension() {
        return ".pdf";
    }

    @Override
    public boolean supports(String mimeType) {
        return "application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(mimeType);
    }

    @Override
    public boolean supportsExtension(String extension) {
        return ".pptx".equals(extension);
    }

    @Override
    public void convert(InputStream inputStream, OutputStream outputStream) {
        SlideShow ss = null;
        PDDocument doc = null;
        try {
            ss = SlideShowFactory.create(inputStream);

            List<Slide> slides = ss.getSlides();

            Dimension size = ss.getPageSize();

            int scale = 1;
            if (size.width < MAX_WIDTH_TO_SCALE) {
                scale = 2;
                LOGGER.debug("Current slides are small. Will use a scale factor of 2.");
            }

            int width = (size.width * scale);
            int height = (size.height * scale);
            doc = new PDDocument();
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (Slide slide : slides) {
                Graphics2D graphics = img.createGraphics();
                DrawFactory.getInstance(graphics).fixFonts(graphics);
                // Default rendering options
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                // Scale image
                graphics.scale(scale, scale);
                // Draw on image
                slide.draw(graphics);

                // Draw on new PDF page
                PDPage page = new PDPage(new PDRectangle(width, height));
                doc.addPage(page);
                PDImageXObject pdImage = LosslessFactory.createFromImage(doc, img);
                PDPageContentStream contents = new PDPageContentStream(doc, page);
                contents.drawImage(pdImage, 0, 0);
                contents.close();
                graphics.dispose();
            }
            // Flush buffer
            img.flush();
            // Save document
            doc.save(outputStream);
        } catch (IOException e) {
            LOGGER.warn("Failed to perform conversion");
            throw new IllegalStateException(e);
        } finally {
            try {
                doc.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close pdf document", e);
            }
            try {
                ss.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close Slideshow", e);
            }
        }

    }
}
