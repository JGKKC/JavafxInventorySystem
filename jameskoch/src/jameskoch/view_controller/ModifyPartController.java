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
import static jameskoch.view_controller.MainScreenController.partToModifyIndex;
import static jameskoch.model.Inventory.getPartInventory;

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
public class ModifyPartController implements Initializable {
    
    @FXML private TextField IdTextField;
    @FXML private TextField NameTextField;
    @FXML private TextField InventoryTextField;
    @FXML private TextField PriceTextField;
    @FXML private TextField MaxTextField;
    @FXML private TextField MinTextField;
    @FXML private TextField CoNameMachIdTextField;
    @FXML private RadioButton InHouseRadioButton;
    @FXML private RadioButton OutSourcedRadioButton;
    @FXML private Label CoNameMachIdLabel;
    private ToggleGroup sourcedFromToggleGroup;
    
    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;
    
    
    @FXML void ModifyPartsOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        CoNameMachIdLabel.setText("Company Name.");
    }

    @FXML void ModifyPartsInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        CoNameMachIdLabel.setText("Machine ID");
    }

    @FXML void ModifyPartsCancelClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to cancel update of part " + NameTextField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part add has been cancelled.");
            Parent partsCancel = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete part info.");
        }
    }

    @FXML void ModifyPartsSaveClicked(ActionEvent event) throws IOException {
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
                alert.setHeaderText("Error!");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
            } else {
                if (InHouseRadioButton.isArmed()) {
                    InhousePart inhousePart = new InhousePart();
                    inhousePart.setPartID(partID);
                    inhousePart.setPartName(partName);
                    inhousePart.setPartPrice(Double.parseDouble(partPrice));
                    inhousePart.setPartInStock(Integer.parseInt(partInv));
                    inhousePart.setPartMin(Integer.parseInt(partMin));
                    inhousePart.setPartMax(Integer.parseInt(partMax));
                    inhousePart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inhousePart);
                } else {
                    OutsourcedPart outsourcedPart = new OutsourcedPart();
                    outsourcedPart.setPartID(partID);
                    outsourcedPart.setPartName(partName);
                    outsourcedPart.setPartPrice(Double.parseDouble(partPrice));
                    outsourcedPart.setPartInStock(Integer.parseInt(partInv));
                    outsourcedPart.setPartMin(Integer.parseInt(partMin));
                    outsourcedPart.setPartMax(Integer.parseInt(partMax));
                    outsourcedPart.setPartCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outsourcedPart);;
                }

                Parent modifyProductCancel = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                Scene scene = new Scene(modifyProductCancel);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
             Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        IdTextField.setText("Part ID autoset to: " + partID);
        NameTextField.setText(part.getPartName());
        InventoryTextField.setText(Integer.toString(part.getPartInStock()));
        PriceTextField.setText(Double.toString(part.getPartPrice()));
        MinTextField.setText(Integer.toString(part.getPartMin()));
        MaxTextField.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InhousePart) {
            CoNameMachIdTextField.setText(Integer.toString(((InhousePart) getPartInventory().get(partIndex)).getPartMachineID()));
            CoNameMachIdLabel.setText("Machine ID");
            InHouseRadioButton.setSelected(true);

        } else {
            CoNameMachIdTextField.setText(((OutsourcedPart) getPartInventory().get(partIndex)).getPartCompanyName());
            CoNameMachIdLabel.setText("Company Name");
            OutSourcedRadioButton.setSelected(true);
        }
        
        sourcedFromToggleGroup = new ToggleGroup();
        this.InHouseRadioButton.setToggleGroup(sourcedFromToggleGroup);
        this.OutSourcedRadioButton.setToggleGroup(sourcedFromToggleGroup);
    }    
    
}
