/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jameskoch.view_controller;

import jameskoch.model.InhousePart;
import jameskoch.model.Inventory;
import jameskoch.model.OutsourcedPart;
import jameskoch.model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author james
 */
public class AddPartController implements Initializable {

    @FXML private RadioButton InhouseRadioButton;
    @FXML private RadioButton OutsourcedRadioButton;
    @FXML private Label SwitchAddPartLabel;
    @FXML private TextField IdTextField;
    @FXML private TextField NameTextField;
    @FXML private TextField InventoryTextField;
    @FXML private TextField PriceTextField;
    @FXML private TextField MaxTextField;
    @FXML private TextField MinTextField;
    @FXML private TextField CoNameMachIdTextField;
    private ToggleGroup sourcedFromToggleGroup;
    

    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;

    @FXML void AddPartsInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        SwitchAddPartLabel.setText("Machine ID");
    }

    @FXML void AddPartsOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        SwitchAddPartLabel.setText("Company Name");
    }

    @FXML void AddPartsSaveClicked(ActionEvent event) throws IOException {
        String partName = NameTextField.getText();
        String partInv = InventoryTextField.getText();
        String partPrice = PriceTextField.getText();
        String partMin = MinTextField.getText();
        String partMax = MaxTextField.getText();
        String partDyn = CoNameMachIdTextField.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isOutsourced == false) {
                    InhousePart iPart = new InhousePart();

                    iPart.setPartID(partID);
                    iPart.setPartName(partName);
                    iPart.setPartPrice(Double.parseDouble(partPrice));
                    iPart.setPartInStock(Integer.parseInt(partInv));
                    iPart.setPartMin(Integer.parseInt(partMin));
                    iPart.setPartMax(Integer.parseInt(partMax));
                    iPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(iPart);
                } else {
                    OutsourcedPart oPart = new OutsourcedPart();

                    oPart.setPartID(partID);
                    oPart.setPartName(partName);
                    oPart.setPartPrice(Double.parseDouble(partPrice));
                    oPart.setPartInStock(Integer.parseInt(partInv));
                    oPart.setPartMin(Integer.parseInt(partMin));
                    oPart.setPartMax(Integer.parseInt(partMax));
                    oPart.setPartCompanyName(partDyn);
                    Inventory.addPart(oPart);
                }

                Parent partsSave = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                Scene scene = new Scene(partsSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML void AddPartsCancelClicked(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete");
        alert.setContentText("Are you sure you want to delete part " + NameTextField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent partsCancel = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("Cancel has been clicked.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        IdTextField.setText("AUTO GEN: " + partID);
        
        sourcedFromToggleGroup = new ToggleGroup();
        this.InhouseRadioButton.setToggleGroup(sourcedFromToggleGroup);
        this.OutsourcedRadioButton.setToggleGroup(sourcedFromToggleGroup);
    }    
    
}
