package com.mapledoum.com.bkp.examples;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class Decrypt {

    public static void main(String[] args) throws Exception{

        try (PDDocument document = PDDocument.load(new File("/tmp/encrypt.pdf"), "password")) {
            document.setAllSecurityToBeRemoved(true);

            PDFTextStripper reader = new PDFTextStripper();
            String pageText = reader.getText(document);
            System.out.println(pageText);

        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

}
