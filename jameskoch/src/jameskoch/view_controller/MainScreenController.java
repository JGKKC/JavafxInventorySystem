/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jameskoch.view_controller;

import jameskoch.model.Part;
import jameskoch.model.Product;
import jameskoch.model.Inventory;
import static jameskoch.model.Inventory.getPartInventory;
import static jameskoch.model.Inventory.getProductInventory;
import static jameskoch.model.Inventory.removeProduct;
import static jameskoch.model.Inventory.removePart;
import static jameskoch.model.Inventory.validatePartDelete;


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
public class MainScreenController implements Initializable {
    
    @FXML private TextField PartsSearchTextField;
    @FXML private TextField ProductsSearchTextField;
    @FXML private TableView<Part> PartsTableView;
    @FXML private TableColumn<Part, Integer> PartsPartIdTableColumn;
    @FXML private TableColumn<Part, String> PartsPartNameTableColumn;
    @FXML private TableColumn<Part,  Integer> PartsInventoryLevelTableColumn;
    @FXML private TableColumn<Part, Double> PartsPriceTableColumn;
    @FXML private TableView<Product> ProductsTableView;
    @FXML private TableColumn<Product, Integer> ProductsPartIdTableColumn;
    @FXML private TableColumn<Product, String> ProductsPartNameTableColumn;
    @FXML private TableColumn<Product, Integer> ProductsInventoryLevelTableColumn;
    @FXML private TableColumn<Product, Double> ProductsPriceTableColumn;
    

    /**
     * Initializes the controller class.
     */
    
    
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    public MainScreenController() {
    }

    @FXML void ClearSearchParts(ActionEvent event) throws IOException {
        updatePartTableView();
        PartsSearchTextField.setText("");
    }

    @FXML void ClearSearchProducts(ActionEvent event) throws IOException {
        updateProductTableView();
        ProductsSearchTextField.setText("");
    }

    @FXML void MainExitClick(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation ");
        alert.setHeaderText("Confirm Exit!");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {
            System.out.println("You clicked cancel. Please complete form.");
        }
    }

    @FXML void MainAddPartsClick(ActionEvent event) throws IOException {

        Parent addParts = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void MainAddProductsClick(ActionEvent event) throws IOException {

        Parent addProducts = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        Scene scene = new Scene(addProducts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void MainModifyPartsClick(ActionEvent event) throws IOException {

        modifyPart = PartsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent modifyParts = FXMLLoader.load(getClass().getResource("modifyPart.fxml"));
        Scene scene = new Scene(modifyParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void MainModifyProductsClick(ActionEvent event) throws IOException {

        modifyProduct = ProductsTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent modifyProducts = FXMLLoader.load(getClass().getResource("modifyProduct.fxml"));
        Scene scene = new Scene(modifyProducts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void MainSearchProductsBtn(ActionEvent event) throws IOException {
        String searchProd = ProductsSearchTextField.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProd) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error!");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any Product!");
            alert.showAndWait();
        } else {
            prodIndex = Inventory.lookupProduct(searchProd);
            Product tempProd = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempProd);
            ProductsTableView.setItems(tempProdList);
        }
    }

    @FXML void MainDeleteProductsClick(ActionEvent event) throws IOException {
        Product product = ProductsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Confirm?");
        alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            removeProduct(product);
            updateProductTableView();
            System.out.println("Product " + product.getProductName() + " was removed.");
        } else {
            System.out.println("Product " + product.getProductName() + " was not removed.");
        }
    }

    @FXML void MainPartsSearchBtn(ActionEvent event) throws IOException {
        String searchPart = PartsSearchTextField.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any Part!");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempProdList = FXCollections.observableArrayList();
            tempProdList.add(tempPart);
            PartsTableView.setItems(tempProdList);
        }
    }

    @FXML void MainDeletePartsClick(ActionEvent event) throws IOException {
        Part part = PartsTableView.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Delete Error!");
            alert.setHeaderText("Part cannot be removed!");
            alert.setContentText("This part is used in a product.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                removePart(part);
                updatePartTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            } else {
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartsPartIdTableColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        PartsPartNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        PartsInventoryLevelTableColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        PartsPriceTableColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        ProductsPartIdTableColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        ProductsPartNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        ProductsInventoryLevelTableColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        ProductsPriceTableColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartTableView();
        updateProductTableView();
    }

    public void updatePartTableView() {
        PartsTableView.setItems(getPartInventory());
    }

    public void updateProductTableView() {
        ProductsTableView.setItems(getProductInventory());
    }

//    public void setMainApp(jameskoch mainApp) {
//        updatePartTableView();
//        updateProductTableView();
//    }
//    
}
