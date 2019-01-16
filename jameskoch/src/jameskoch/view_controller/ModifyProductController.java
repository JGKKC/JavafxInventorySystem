/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jameskoch.view_controller;

import jameskoch.model.Inventory;
import jameskoch.model.Part;
import jameskoch.model.Product;
import static jameskoch.view_controller.MainScreenController.productToModifyIndex;
import static jameskoch.model.Inventory.getPartInventory;
import static jameskoch.model.Inventory.getProductInventory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author james
 */
public class ModifyProductController implements Initializable {
    
    @FXML private TextField SearchPartTextField;
    @FXML private TextField SearchProductTextField;
    @FXML private TextField IdTextField;
    @FXML private TextField NameTextField;
    @FXML private TextField InventoryTextField;
    @FXML private TextField PriceTextField;
    @FXML private TextField MaxTextField;
    @FXML private TextField MinTextField;
    @FXML private TableView<Part> AddTableView;
    @FXML private TableColumn<Part, Integer> AddPartIdTableColumn;
    @FXML private TableColumn<Part, String> AddPartNameTableColumn;
    @FXML private TableColumn<Part, Integer> AddInventoryLevelTableColumm;
    @FXML private TableColumn<Part, Double> AddPriceTableColumn;
    @FXML private TableView<Part> DeleteTableView;
    @FXML private TableColumn<Part, Integer> DeletePartIdTableColumn;
    @FXML private TableColumn<Part, String> DeletePartNameTableColumn;
    @FXML private TableColumn<Part, Integer> DeleteInventoryLevelTableColumn;
    @FXML private TableColumn<Part, Double> DeletePriceTableColumn;
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;
    

    @FXML void ClearSearchAdd(ActionEvent event) {
        updatePartTableView();
        SearchPartTextField.setText("");
    }

    @FXML void ClearSearchRemove(ActionEvent event) {
        updateCurrentPartTableView();
        SearchProductTextField.setText("");
    }

    @FXML void ModifyProductsSearchPartAddBtn(ActionEvent event) {
        String searchPart = SearchPartTextField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("Search term not found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            AddTableView.setItems(tempProdList);
        }
    }

    @FXML void ModifyProductsAddButton(ActionEvent event) {
        Part part = AddTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateCurrentPartTableView();
    }

    @FXML void ModifyProductsSearchPartDeleteBtn(ActionEvent event) {
        String searchPart = SearchProductTextField.getText();
        int partIndex = -1;

        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search");
            alert.setHeaderText("Part not found");
            alert.setContentText("Search term not found.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);

            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            DeleteTableView.setItems(tempProdList);
        }
    }

    @FXML void ModifyProductsDeleteButton(ActionEvent event) {
        Part part = DeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Delete");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        } else {
            System.out.println("Cancel was clicked.");
        }
    }

    @FXML void ModifyProductsSaveButtonClicked(ActionEvent event) throws IOException {
        String productName = NameTextField.getText();
        String productInv = InventoryTextField.getText();
        String productPrice = PriceTextField.getText();
        String productMin = MinTextField.getText();
        String productMax = MaxTextField.getText();
        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Product!");
                alert.setHeaderText("Error!");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
            } else {
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);

                Parent productsSave = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
                Scene scene = new Scene(productsSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            System.out.println("Blank Field");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be left blank!");
            alert.showAndWait();
        }
    }

    @FXML void ModifyProductsCancelClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Delete!");
        alert.setContentText("Are you sure you want to cancel update of product " + NameTextField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent partsCancel = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene scene = new Scene(partsCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel. Please complete form.");
        }
    }
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        System.out.println("Product ID " + productID + " is available.");
        IdTextField.setText("AUTO GEN: " + productID);
        NameTextField.setText(product.getProductName());
        InventoryTextField.setText(Integer.toString(product.getProductInStock()));
        PriceTextField.setText(Double.toString(product.getProductPrice()));
        MinTextField.setText(Integer.toString(product.getProductMin()));
        MaxTextField.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        AddPartIdTableColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        AddPartNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        AddInventoryLevelTableColumm.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        AddPriceTableColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        DeletePartIdTableColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        DeletePartNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        DeleteInventoryLevelTableColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        DeletePriceTableColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updatePartTableView();
        updateCurrentPartTableView();
    }

    public void updatePartTableView() {
        AddTableView.setItems(getPartInventory());
    }

    public void updateCurrentPartTableView() {
        DeleteTableView.setItems(currentParts);
    }

    
}
