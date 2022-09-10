package com.mapledoum.com;


import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.*;
import org.apache.pdfbox.pdmodel.interactive.annotation.layout.PlainText;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import static com.mapledoum.com.utils.bidiReorder;


public class createFormv1 {



    public static void main(String[] args) throws IOException {


        Color color = Color.BLACK;
        float fontSize = 12;

        PDDocument document = new PDDocument();

        PDAcroForm acroForm = new PDAcroForm(document);
        setUpDefaultResourceFontDR(acroForm);
        PDPage page = new PDPage(PDRectangle.A4);

        document.addPage(page);

        PDRectangle mediaBox = page.getMediaBox();
        float textBoxWidth = 200;
        float textBoxHeigh = 25;
        float paddingFromTop = getPaddingFromTop(textBoxHeigh, 1);

        float startY = page.getMediaBox().getUpperRightY() - textBoxHeigh - 30 - maxHeight(page) ;


//        PDTextField textField = createPdTextField("myfield1",page, acroForm, textBoxWidth,textBoxHeigh, TheDirection.RTL );
//        PDTextField textField2 = createPdTextField("myfield2",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);
//        PDTextField textField3 = createPdTextField("myfield3",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);
//        PDTextField textField4 = createPdTextField("myfield4",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);
//        PDTextField textField5 = createPdTextField(startY, "myfield5",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);
        PDTextField textField6 = createPdTextFieldReadOnly(maxHeight(page),"myfield6",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);
//        PDTextField textField51 = createPdTextFieldV2(startY,"myfield51",page, acroForm, textBoxWidth,textBoxHeigh,TheDirection.RTL);

//        createArabicTextWithArabicFont(page, document);
        PDComboBox comboBox = createPdComboBox(fontSize, acroForm, page, 100, 20);
        acroForm.getFields().add(comboBox);
//        acroForm.getFields().add(textField);
//        acroForm.getFields().add(textField2);
//        acroForm.getFields().add(textField3);
//        acroForm.getFields().add(textField4);
//        acroForm.getFields().add(textField5);
//        acroForm.getFields().add(textField51);
        acroForm.getFields().add(textField6);
        page.getAnnotations().stream().forEach(a-> System.out.println(a.getRectangle().getHeight()));

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

    private static float getPaddingFromTop(float textBoxHeigh, float multiplier) {
        return textBoxHeigh + multiplier;
    }

    private static void setUpDefaultResourceFontDR(PDAcroForm acroForm) {
        PDFont font = PDType1Font.HELVETICA;

        PDResources dr = new PDResources();
        dr.put(COSName.getPDFName("Helv"), font);
        acroForm.setDefaultResources(dr);
    }

    private static PDComboBox createPdComboBox(float fontSize, PDAcroForm acroForm, PDPage page, float textBoxWidth, float textBoxHeigh) {
        PDComboBox comboBox = new PDComboBox(acroForm);
        comboBox.setPartialName("test");


        float startX = page.getMediaBox().getUpperRightX() - textBoxWidth - 5 ;
        float startY = page.getMediaBox().getUpperRightY() - textBoxHeigh - 30 - maxHeight(page) ;

        // Helv instead of Helvetica
        String defaultAppearanceString = "/Helv " + fontSize + " Tf "
                + 0 + " " + 0 + " " + 0 + " rg";
        comboBox.setDefaultAppearance(defaultAppearanceString);

        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(new PDRectangle(startX,startY, 100, 20));
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
    private static float maxHeight(PDPage page){

        try {
            return (float) (page.getAnnotations().stream().mapToDouble(a-> a.getRectangle().getHeight()).sum());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
    private static PDTextField createPdTextField(float y, String name, PDPage page, PDAcroForm acroForm, float textBoxWidth, float textBoxHeigh, TheDirection direction) throws IOException {
        PDTextField textField = new PDTextField(acroForm);

        textField.setPartialName(name);
        String defaultAppearance = "/Helv 12 Tf 0 0 1 rg";

        float startX = page.getMediaBox().getUpperRightX() - textBoxWidth - 5 ;
        float startY = y;



        textField.setDefaultAppearance(defaultAppearance);

        PDAnnotationWidget widgetOfTextField = textField.getWidgets().get(0);

        PDRectangle rect = new PDRectangle(startX, startY, textBoxWidth, textBoxHeigh);
        widgetOfTextField.setRectangle(rect);
        widgetOfTextField.setPage(page);

        PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
        widgetOfTextField.setAppearanceCharacteristics(fieldAppearance);
        widgetOfTextField.setPrinted(true);

        page.getAnnotations().add(widgetOfTextField);

        textField.setQ(direction.dir);

//        textField.setDefaultAppearance("Sample Field");

        return textField;
    }

    private static PDTextField createPdTextFieldV2(float maxHi, float y, String name, PDPage page, PDAcroForm acroForm, float textBoxWidth, float textBoxHeigh, TheDirection direction) throws IOException {
        PDTextField textField = new PDTextField(acroForm);

        textField.setPartialName(name);
        String defaultAppearance = "/Helv 12 Tf 0 0 1 rg";

        float startX = page.getMediaBox().getUpperRightX() - textBoxWidth - 10 ;
        float startY = page.getMediaBox().getUpperRightY() - textBoxHeigh - 30 - maxHi ;



        textField.setDefaultAppearance(defaultAppearance);

        PDAnnotationWidget widgetOfTextField = textField.getWidgets().get(0);

        PDRectangle rect = new PDRectangle(startX, startY, textBoxWidth, textBoxHeigh);
        widgetOfTextField.setRectangle(rect);
        widgetOfTextField.setPage(page);

        PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
        fieldAppearance.setBorderColour(new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
        fieldAppearance.setBackground(new PDColor(new float[]{1,1,0}, PDDeviceRGB.INSTANCE));
        widgetOfTextField.setAppearanceCharacteristics(fieldAppearance);
        widgetOfTextField.setPrinted(true);

        page.getAnnotations().add(widgetOfTextField);

        textField.setQ(direction.dir);

        return textField;
    }

    private static PDTextField createPdTextFieldReadOnly(float maxHi, String name, PDPage page, PDAcroForm acroForm, float textBoxWidth, float textBoxHeigh, TheDirection direction) throws IOException {
        PDTextField textField = new PDTextField(acroForm);
        textField.setReadOnly(false);

        textField.setPartialName(name);
        String defaultAppearance = "/Helv 12 Tf 0 0 1 rg";

        float startX = page.getMediaBox().getUpperRightX() - textBoxWidth - 5 ;
        float startY = page.getMediaBox().getUpperRightY() - textBoxHeigh - 30 - maxHi ;



        textField.setDefaultAppearance(defaultAppearance);

        PDAnnotationWidget widgetOfTextField = textField.getWidgets().get(0);

        PDRectangle rect = new PDRectangle(startX, startY, textBoxWidth, textBoxHeigh);

        widgetOfTextField.setRectangle(rect);
        widgetOfTextField.setPage(page);

        PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
         widgetOfTextField.setAppearanceCharacteristics(fieldAppearance);
        widgetOfTextField.setPrinted(true);

        page.getAnnotations().add(widgetOfTextField);
        textField.setQ(direction.dir);

        textField.setValue("ffff");
        textField.setReadOnly(true);

//        textField.setDefaultAppearance("Sample Field");

        return textField;
    }
}
