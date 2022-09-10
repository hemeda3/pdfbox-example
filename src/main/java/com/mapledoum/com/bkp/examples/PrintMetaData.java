package com.mapledoum.com.bkp.examples;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PrintMetaData {

    private static final SimpleDateFormat SDF = new SimpleDateFormat();

    public static void main(String[] args) throws Exception{

        try (PDDocument document = PDDocument.load(new File("/tmp/meta-data.pdf"))) {
            PDDocumentInformation info = document.getDocumentInformation();
            System.out.println( "Page Count=" + document.getNumberOfPages());
            System.out.println( "Title=" + info.getTitle());
            System.out.println( "Author=" + info.getAuthor());
            System.out.println( "Subject=" + info.getSubject());
            System.out.println( "Keywords=" + info.getKeywords());
            System.out.println( "Creator=" + info.getCreator());
            System.out.println( "Producer=" + info.getProducer());
            System.out.println( "Creation Date=" + SDF.format(info.getCreationDate().getTime()));
            System.out.println( "Modification Date=" + SDF.format(info.getModificationDate().getTime()));
            System.out.println( "Trapped=" + info.getTrapped());

            PDDocumentCatalog cat = document.getDocumentCatalog();
            PDMetadata metadata = cat.getMetadata();
            if (metadata != null) {
                String string =  new String( metadata.toByteArray(), "ISO-8859-1");
                System.out.println( "Metadata=" + string);
            }
        } catch (IOException e){
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

}
