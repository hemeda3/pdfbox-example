package com.mapledoum.com;

import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

public enum TheDirection {
    Center(PDTextField.QUADDING_CENTERED),
    RTL(PDTextField.QUADDING_RIGHT),
    LTR(PDTextField.QUADDING_LEFT);

    int dir = 0;
    TheDirection(int d){
        this.dir = d;
    }
}
