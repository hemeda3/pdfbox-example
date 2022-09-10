package com.mapledoum.com.examples;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.XmpSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class AddMetaData {

    public static void main(String[] args) throws Exception{

        try (final PDDocument document = new PDDocument()){

            PDDocumentInformation info = new PDDocumentInformation();
            info.setTitle("Apache PDFBox");
            info.setSubject("Apache PDFBox adding meta-data to PDF document");
            info.setAuthor("Memorynotfound.com");
            info.setCreator("Memorynotfound.com");
            info.setProducer("Memorynotfound.com");
            info.setKeywords("Apache, PdfBox, XMP, PDF");
            info.setCreationDate(Calendar.getInstance());
            info.setModificationDate(Calendar.getInstance());
            info.setTrapped("Unknown");
            info.setCustomMetadataValue("swag", "yes");

            XMPMetadata metadata = XMPMetadata.createXMPMetadata();

            AdobePDFSchema pdfSchema = metadata.createAndAddAdobePDFSchema();
            pdfSchema.setKeywords(info.getKeywords());
            pdfSchema.setProducer(info.getProducer());

            XMPBasicSchema basicSchema = metadata.createAndAddXMPBasicSchema();
            basicSchema.setModifyDate(info.getModificationDate());
            basicSchema.setCreateDate(info.getCreationDate());
            basicSchema.setCreatorTool(info.getCreator());
            basicSchema.setMetadataDate(info.getCreationDate());

            DublinCoreSchema dcSchema = metadata.createAndAddDublinCoreSchema();
            dcSchema.setTitle(info.getTitle());
            dcSchema.addCreator(info.getCreator());
            dcSchema.setDescription(info.getSubject());

            PDMetadata metadataStream = new PDMetadata(document);
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            catalog.setMetadata(metadataStream);

            XmpSerializer serializer = new XmpSerializer();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.serialize(metadata, out, false);
            metadataStream.importXMPMetadata(out.toByteArray());

            PDPage page = new PDPage();
            document.addPage(page);

            document.setDocumentInformation(info);
            document.setVersion(1.5f);
            document.save(new File("/tmp/meta-data.pdf"));
        } catch (IOException e){
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }

}
