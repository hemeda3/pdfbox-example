package com.mapledoum.com.bkp.examples;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SplitPdf {

    public static void main(String[] args) throws Exception{

        try (PDDocument document = PDDocument.load(new File("/tmp/example.pdf"))) {

            // Instantiating Splitter class
            Splitter splitter = new Splitter();

            // splitting the pages of a PDF document
            List<PDDocument> Pages = splitter.split(document);

            // Creating an iterator
            Iterator<PDDocument> iterator = Pages.listIterator();

            // Saving each page as an individual document
            int i = 1;
            while (iterator.hasNext()) {
                PDDocument pd = iterator.next();
                pd.save("/tmp/split_" + i + ".pdf");
                i++;
            }

        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

}
