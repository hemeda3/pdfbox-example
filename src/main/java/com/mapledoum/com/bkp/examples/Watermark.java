package com.mapledoum.com.bkp.examples;

import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.util.HashMap;

//https://memorynotfound.com/apache-pdfbox-add-watermark-pdf-document/
public class Watermark {

    public static void main(String[] args) throws Exception{
        PDDocument realDoc = PDDocument.load(new File("/tmp/example.pdf"));

        HashMap<Integer, String> overlayGuide = new HashMap<Integer, String>();
        for(int i=0; i<realDoc.getNumberOfPages(); i++){
            overlayGuide.put(i+1, "/tmp/watermark.pdf");
        }
        Overlay overlay = new Overlay();
        overlay.setInputPDF(realDoc);
        overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
        overlay.overlay(overlayGuide);

        realDoc.save(new File("/tmp/watermarked.pdf"));
    }
}
