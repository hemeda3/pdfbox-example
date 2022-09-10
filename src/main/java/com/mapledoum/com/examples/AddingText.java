package com.mapledoum.com.examples;

import java.io.File;
import java.io.IOException;

import com.ibm.icu.text.ArabicShaping;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import static com.mapledoum.com.utils.bidiReorder;
import static com.mapledoum.com.utils.reverseNumbersInString;

public class AddingText {

    public static void main(String[] args)throws IOException {


        clearPDF();
        //Loading an existing document
        File file = new File("target/blank.pdf");
        PDDocument doc = PDDocument.load(file);

        //Retrieving the pages of the document
        PDPage page = doc.getPage(1);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        //Begin the Content stream
        contentStream.beginText();

        //Setting the font to the Content stream
        File f = new File("/Users/ahmedyousri/Downloads/pdfcalc/src/main/java/com/mapledoum/com/NotoNaskhArabic-VariableFont_wght.ttf");

        contentStream.setFont(  PDType0Font.load(doc, f), 10);

        contentStream.setLeading(14.5f);

         contentStream.newLineAtOffset(25, 700);

        String s ="جملة بالعربي لتجربة الكلاس اللذي يساعد علي وصل الحروف بشكل صحيح";
        contentStream.showText(bidiReorder(s));
        contentStream.newLine();


        //Ending the content stream
        contentStream.endText();

        System.out.println("Multiple Text Content is added in the PDF Document.");

        //Closing the content stream
        contentStream.close();

        //Saving the document
        doc.save("target/blank.pdf");

        //Closing the document
        doc.close();
    }


    public static void clearPDF() throws IOException {
        //Loading an existing document
        File file = new File("target/blank.pdf");
        PDDocument doc = PDDocument.load(file);

        //Retrieving the pages of the document
        PDPage page = doc.getPage(1);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        //Begin the Content stream
        contentStream.beginText();


        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 24);

        //Setting the leading
        contentStream.setLeading(14.5f);

        //Setting the position for the line
        contentStream.newLineAtOffset(25, 700);

        String text = "";
        String Line1 = "";
        String Line2 = "";

        //Adding text in the form of string
        contentStream.showText(text);
        contentStream.newLine();
        contentStream.showText(Line1);
        contentStream.newLine();
        contentStream.showText(Line2);

        //Ending the content stream
        contentStream.endText();

        System.out.println("Multiple Text Content is added in the PDF Document.");

        //Closing the content stream
        contentStream.close();

        //Saving the document
        doc.save("target/blank.pdf");

        //Closing the document
        doc.close();
    }




    public static void create(String[] args)throws IOException {
        //Creating PDF document object
        PDDocument doc = new PDDocument();

        for (int i=0; i<5; i++) {
            //Creating a blank page
            PDPage blankPage = new PDPage();

            //Adding the blank page to the document
            doc.addPage( blankPage );
        }

        //Saving the document
        doc.save("target/blank.pdf");

        System.out.println("PDF created");

        //Closing the document
        doc.close();
    }
}
