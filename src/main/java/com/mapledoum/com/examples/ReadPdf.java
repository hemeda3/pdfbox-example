package com.mapledoum.com.examples;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class ReadPdf {

    public static void main(String[] args) throws Exception{

        try (PDDocument document = PDDocument.load(new File("/tmp/example.pdf"))) {

            if (!document.isEncrypted()) {
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);
                String lines[] = pdfFileInText.split("\\r?\\n");
                for (String line : lines) {
                    System.out.println(line);
                }
            }
        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

}
