/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jameskoch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author james
 */
public class OutsourcedPart extends Part {
    private final StringProperty companyName;

    public OutsourcedPart() {
        super();
        companyName = new SimpleStringProperty();
    }

    public void setPartCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getPartCompanyName() {
        return this.companyName.get();
    }
}
