package com.mapledoum.com.examples;


import com.mapledoum.com.utils;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mapledoum.com.utils.bidiReorder;


public class createFormv2 {



    public static void main(String[] args) throws IOException {

        Color color = Color.BLACK;
        float fontSize = 12;

        PDDocument document = new PDDocument();

        PDAcroForm acroForm = new PDAcroForm(document);
        setUpDefaultResourceFontDR(acroForm);
        PDPage page = new PDPage(PDRectangle.A4);

        document.addPage(page);


        PDTextField textField = createPdTextField(page, acroForm);
        createArabicTextWithArabicFont(page, document);

        PDComboBox comboBox = createPdComboBox(fontSize, acroForm, page);

        // new
        acroForm.getFields().add(comboBox);
        acroForm.getFields().add(textField);
        document.getDocumentCatalog().setAcroForm(acroForm);


        try
        {
            utils.delete("target/testa.pdf");
            FileOutputStream output = new FileOutputStream("target/testa.pdf");
            document.save(output);
            document.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void setUpDefaultResourceFontDR(PDAcroForm acroForm) {
        PDFont font = PDType1Font.HELVETICA;

        PDResources dr = new PDResources();
        dr.put(COSName.getPDFName("Helv"), font);
        acroForm.setDefaultResources(dr);
    }

    private static PDComboBox createPdComboBox(float fontSize, PDAcroForm acroForm, PDPage page) {
        PDComboBox comboBox = new PDComboBox(acroForm);
        comboBox.setPartialName("test");

        // Helv instead of Helvetica
        String defaultAppearanceString = "/Helv " + fontSize + " Tf "
                + 0 + " " + 0 + " " + 0 + " rg";
        comboBox.setDefaultAppearance(defaultAppearanceString);

        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(new PDRectangle(200, 200, 100, 20));
        widget.setAnnotationFlags(4);
        widget.setPage(page);
        widget.setParent(comboBox);

        List<String> exportValues = new ArrayList<>();
        List<String> displayValues = new ArrayList<>();

        displayValues.add("öne");
        displayValues.add("two");
        displayValues.add("thrée");

        exportValues.add("1");
        exportValues.add("2");
        exportValues.add("3");

        comboBox.setOptions(exportValues, displayValues);

        List<PDAnnotationWidget> widgets = new ArrayList<>();
        widgets.add(widget);
        try {
            page.getAnnotations().add(widget);
        } catch (IOException e) {
            e.printStackTrace();
        }

        comboBox.setWidgets(widgets);
        return comboBox;
    }

    private static void createArabicTextWithArabicFont(PDPage page, PDDocument pdDocument) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(pdDocument, page);
        //Begin the Content stream
        contentStream.beginText();

        //Setting the font to the Content stream
        File f = new File("/Users/ahmedyousri/Downloads/pdfcalc/src/main/java/com/mapledoum/com/NotoNaskhArabic-VariableFont_wght.ttf");

        contentStream.setFont(  PDType0Font.load(pdDocument, f), 10);

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
    }
    private static PDTextField createPdTextField(PDPage page, PDAcroForm acroForm) throws IOException {
        PDTextField textField = new PDTextField(acroForm);
        textField.setPartialName("SampleField");
        String defaultAppearance = "/Helv 12 Tf 0 0 1 rg";

        textField.setDefaultAppearance(defaultAppearance);

        PDAnnotationWidget widgetOfTextField = textField.getWidgets().get(0);
        PDRectangle rect = new PDRectangle(50, 750, 200, 50);
        widgetOfTextField.setRectangle(rect);
        widgetOfTextField.setPage(page);

        PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
        widgetOfTextField.setAppearanceCharacteristics(fieldAppearance);
        widgetOfTextField.setPrinted(true);

        page.getAnnotations().add(widgetOfTextField);
        textField.setValue("Sample Field");

        return textField;
    }
}
