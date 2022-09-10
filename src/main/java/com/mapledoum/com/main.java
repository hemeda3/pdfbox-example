package com.mapledoum.com;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
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
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import static com.mapledoum.com.utils.bidiReorder;


public class main {



    public static void main1(String[] args) throws IOException {

        try (PDDocument document = new PDDocument())
        {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDAcroForm acroForm = new PDAcroForm(document);

            // if you want to see what Adobe does, activate this, open with Adobe
            // save the file, and then open it with PDFDebugger

            //acroForm.setNeedAppearances(true)

            document.getDocumentCatalog().setAcroForm(acroForm);
            List<String> options = Arrays.asList("a", "b", "c");
            PDComboBox comboBox = new PDComboBox(acroForm);
            comboBox.setPartialName("myCombo");
            comboBox.setRichTextValue("ricch");

            PDChoice choiceField = new PDComboBox(acroForm);
            COSArray choiceFieldOptions = new COSArray();
            choiceFieldOptions.add(new COSString(" "));
            choiceFieldOptions.add(new COSString("A"));
            choiceFieldOptions.add(new COSString("B"));

            PDAppearanceCharacteristicsDictionary appearanceCharacteristics = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
            appearanceCharacteristics.setBorderColour(new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE));
            appearanceCharacteristics.setBackground(new PDColor(new float[]{0, 1, 0.3f}, PDDeviceRGB.INSTANCE));
            // no caption => round
            // with caption => see checkbox example

            List<PDAnnotationWidget> widgets = new ArrayList<>();
            for (int i = 0; i < options.size(); i++)
            {
                PDAnnotationWidget widget = new PDAnnotationWidget();
                widget.setRectangle(new PDRectangle(30, PDRectangle.A4.getHeight() - 40 - i * 35, 30, 30));
                widget.setAppearanceCharacteristics(appearanceCharacteristics);
                PDBorderStyleDictionary borderStyleDictionary = new PDBorderStyleDictionary();
                borderStyleDictionary.setWidth(2);
                borderStyleDictionary.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
                widget.setBorderStyle(borderStyleDictionary);
                widget.setPage(page);

                COSDictionary apNDict = new COSDictionary();
                apNDict.setItem(COSName.Off, createAppearanceStream(document, widget, false));
                apNDict.setItem(options.get(i), createAppearanceStream(document, widget, true));

                PDAppearanceDictionary appearance = new PDAppearanceDictionary();
                PDAppearanceEntry appearanceNEntry = new PDAppearanceEntry(apNDict);
                appearance.setNormalAppearance(appearanceNEntry);
                widget.setAppearance(appearance);
                widget.setAppearanceState("Off"); // don't forget this, or button will be invisible
                widgets.add(widget);
                page.getAnnotations().add(widget);
            }
            comboBox.setWidgets(widgets);

            acroForm.getFields().add(comboBox);
            PDFont font = PDType1Font.TIMES_BOLD;

            // Set the texts
//            PDType1Font helvetica = new PDType1Font(FontName.HELVETICA);
            try (PDPageContentStream contents = new PDPageContentStream(document, page))
            {
                for (int i = 0; i < options.size(); i++)
                {
                    contents.beginText();
                    contents.setFont(font, 15);
                    contents.newLineAtOffset(70, PDRectangle.A4.getHeight() - 30 - i * 35);
                    contents.showText(options.get(i));
//                    contents.moveTextPositionByAmount(x, y);
//                    contents.dra("Some text.");
                    contents.newLine();
                    contents.drawString("Some more text.");
                    contents.newLine();
                    contents.drawString("Still some more text.");
                    contents.endText();
                }
            }

//            comboBox.setValue("c");

            document.save("target/RadioButtonsSample2.pdf");
        }
    }


    private static PDAppearanceStream createAppearanceStream(
            final PDDocument document, PDAnnotationWidget widget, boolean on) throws IOException
    {
        PDRectangle rect = widget.getRectangle();
        PDAppearanceStream onAP = new PDAppearanceStream(document);
        onAP.setBBox(new PDRectangle(rect.getWidth(), rect.getHeight()));
        try (PDAppearanceContentStream onAPCS = new PDAppearanceContentStream(onAP))
        {
            PDAppearanceCharacteristicsDictionary appearanceCharacteristics = widget.getAppearanceCharacteristics();
            PDColor backgroundColor = appearanceCharacteristics.getBackground();
            PDColor borderColor = appearanceCharacteristics.getBorderColour();
            float lineWidth = getLineWidth(widget);
            onAPCS.setBorderLine(lineWidth, widget.getBorderStyle(), widget.getBorder());
            onAPCS.setNonStrokingColor(backgroundColor);
            float radius = Math.min(rect.getWidth() / 2, rect.getHeight() / 2);
            drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, radius);
            onAPCS.fill();
            onAPCS.setStrokingColor(borderColor);
            drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, radius - lineWidth / 2);
            onAPCS.stroke();
            if (on)
            {
                onAPCS.setNonStrokingColor(0f);
                drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, (radius - lineWidth) / 2);
                onAPCS.fill();
            }
        }
        return onAP;
    }

    static float getLineWidth(PDAnnotationWidget widget)
    {
        PDBorderStyleDictionary bs = widget.getBorderStyle();
        if (bs != null)
        {
            return bs.getWidth();
        }
        return 1;
    }

    static void drawCircle(PDAppearanceContentStream cs, float x, float y, float r) throws IOException
    {
        // http://stackoverflow.com/a/2007782/535646
        float magic = r * 0.551784f;
        cs.moveTo(x, y + r);
        cs.curveTo(x + magic, y + r, x + r, y + magic, x + r, y);
        cs.curveTo(x + r, y - magic, x + magic, y - r, x, y - r);
        cs.curveTo(x - magic, y - r, x - r, y - magic, x - r, y);
        cs.curveTo(x - r, y + magic, x - magic, y + r, x, y + r);
        cs.closePath();
    }




    public static void maingood(String[] args) {

        PDFont font = PDType1Font.HELVETICA;
        Color color = Color.BLACK;
        float fontSize = 12;

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);

        document.addPage(page);

        PDAcroForm acroForm = new PDAcroForm(document);
        PDComboBox comboBox = createPdComboBox(fontSize, acroForm, page);


        try
        {
            utils.delete("target/test.pdf");
            FileOutputStream output = new FileOutputStream("target/test.pdf");
            document.save(output);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
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
            utils.delete("target/test.pdf");
            FileOutputStream output = new FileOutputStream("target/test.pdf");
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

    private static PDType0Font createPDType0Font(PDDocument pdDocument) throws IOException {
        File f = new File("/Users/ahmedyousri/Downloads/pdfcalc/src/main/java/com/mapledoum/com/NotoNaskhArabic-VariableFont_wght.ttf");

        return PDType0Font.load(pdDocument, f);


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
