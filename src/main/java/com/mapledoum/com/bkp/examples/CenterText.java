package com.mapledoum.com.bkp.examples;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class CenterText {

    public static void main(String[] args) throws Exception{

        try {

            String title = "Apache PDFBox Center Text PDF Document";
            PDFont font = PDType1Font.HELVETICA_BOLD;
            int marginTop = 30;
            int fontSize = 16;

            final PDDocument doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            PDRectangle mediaBox = page.getMediaBox();
            doc.addPage(page);

            PDPageContentStream stream = new PDPageContentStream(doc, page);

            float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
            float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            float startX = (mediaBox.getWidth() - titleWidth) / 2;
            float startY = mediaBox.getHeight() - marginTop - titleHeight;

            stream.beginText();
            stream.setFont(font, fontSize);
            stream.newLineAtOffset(startX, startY);
            stream.showText(title);
            stream.endText();
            stream.close();

            doc.save(new File("/tmp/center-text.pdf"));

        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }

}
