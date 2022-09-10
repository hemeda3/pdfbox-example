package com.mapledoum.com;


import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.File;

public class utils {

    public static boolean isInt(String Input)
    {
        try{Integer.parseInt(Input);return true;}
        catch(NumberFormatException e){return false;}
    }
    public static String reverseNumbersInString(String Input)
    {
        char[] Separated = Input.toCharArray();int i = 0;
        String Result = "",Hold = "";
        for(;i<Separated.length;i++ )
        {
            if(isInt(Separated[i]+"") == true)
            {
                while(i < Separated.length && (isInt(Separated[i]+"") == true ||  Separated[i] == '.' ||  Separated[i] == '-'))
                {
                    Hold += Separated[i];
                    i++;
                }
                Result+=Hold;
                Hold="";
            }
            else{Result+=Separated[i];}
        }
        return Result;
    }

    public static String bidiReorder(String text)
    {
        try {
            Bidi bidi = new Bidi((new ArabicShaping(ArabicShaping.LETTERS_SHAPE)).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (Exception ase3) {
            ase3.printStackTrace();
            return text;
        }
    }


    public static void delete(String f) {

        File file = new File("target/test.pdf");
        boolean res =  file.delete();

    }


}
