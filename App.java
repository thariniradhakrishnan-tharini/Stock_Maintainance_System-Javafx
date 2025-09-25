package com.stock;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class App extends Application {
    private static ObservableList<Product> inventory = FXCollections.observableArrayList();
    private static int nextProductId = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stock Maintenance System");

        BorderPane borderPane = new BorderPane();
        VBox vbox = new VBox();
        borderPane.setLeft(vbox);

        populateMenu(vbox);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void populateMenu(VBox vbox) {
        vbox.setPadding(new Insets(10));

        Button addProductB = new Button("Add Product");
        addProductB.setOnAction(e -> addProduct());

        Button updateProductBtn = new Button("Update Product");
        updateProductBtn.setOnAction(e -> updateProduct());

        Button removeProductBtn = new Button("Remove Product");
        removeProductBtn.setOnAction(e -> removeProduct());

        Button checkStockLevelsBtn = new Button("Check Stock Levels");
        checkStockLevelsBtn.setOnAction(e -> checkStockLevels());

        Button searchAndViewProductBtn = new Button("Search and View Product");
        searchAndViewProductBtn.setOnAction(e -> searchAndViewProduct());

        Button displaySummaryBtn = new Button("Display Summary");
        displaySummaryBtn.setOnAction(e -> displaySummary());

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> System.exit(0));

        vbox.getChildren().addAll(
                addProductB, updateProductBtn, removeProductBtn,
                checkStockLevelsBtn, searchAndViewProductBtn,
                displaySummaryBtn, exitBtn
        );
    }

    private void addProduct() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Product");
        dialog.setHeaderText("Enter product details:");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField stockField = new TextField();

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Stock Level:"), 0, 1);
        grid.add(stockField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                try {
                    String name = nameField.getText();
                    int stock = Integer.parseInt(stockField.getText());

                    Product newProduct = new Product(nextProductId++, name, stock);
                    inventory.add(newProduct);
                    showInfoAlert("Product added successfully. Product ID: " + newProduct.getId());
                } catch (NumberFormatException e) {
                    showWarningAlert("Invalid stock level. Please enter a valid number.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Product");
        dialog.setHeaderText("Enter product ID to update:");
        dialog.setContentText("Product ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                int productId = Integer.parseInt(idStr);
                Product product = findProductById(productId);

                if (product != null) {
                    Dialog<Product> updateDialog = new Dialog<>();
                    updateDialog.setTitle("Update Product");
                    updateDialog.setHeaderText("Enter new stock level:");

                    ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                    updateDialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

                    GridPane updateGrid = new GridPane();
                    updateGrid.setHgap(10);
                    updateGrid.setVgap(10);

                    TextField stockField = new TextField();
                    stockField.setText(String.valueOf(product.getStock()));

                    updateGrid.add(new Label("Stock Level:"), 0, 0);
                    updateGrid.add(stockField, 1, 0);

                    updateDialog.getDialogPane().setContent(updateGrid);

                    updateDialog.setResultConverter(buttonType -> {
                        if (buttonType == updateButton) {
                            try {
                                int newStock = Integer.parseInt(stockField.getText());
                                product.setStock(newStock);
                                showInfoAlert("Stock level updated successfully for product ID " + productId);
                            } catch (NumberFormatException e) {
                                showWarningAlert("Invalid stock level. Please enter a valid number.");
                            }
                        }
                        return null;
                    });

                    updateDialog.showAndWait();
                } else {
                    showWarningAlert("Product not found with ID " + productId);
                }
            } catch (NumberFormatException e) {
                showWarningAlert("Invalid product ID. Please enter a valid number.");
            }
        });
    }

    private void removeProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Product");
        dialog.setHeaderText("Enter product ID to remove:");
        dialog.setContentText("Product ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                int productId = Integer.parseInt(idStr);
                Product product = findProductById(productId);

                if (product != null) {
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Confirm Removal");
                    confirmDialog.setHeaderText("Are you sure you want to remove the product?");
                    confirmDialog.setContentText("Product ID: " + productId);

                    Optional<ButtonType> confirmResult = confirmDialog.showAndWait();
                    if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                        inventory.remove(product);
                        showInfoAlert("Product removed successfully. Product ID: " + productId);
                    }
                } else {
                    showWarningAlert("Product not found with ID " + productId);
                }
            } catch (NumberFormatException e) {
                showWarningAlert("Invalid product ID. Please enter a valid number.");
            }
        });
    }

    private void searchAndViewProduct() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search and View Product");
        dialog.setHeaderText("Enter product name or ID to search/view:");
        dialog.setContentText("Product Name or ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(searchInput -> {
            Product product = findProductByNameOrId(searchInput);

            if (product != null) {
                Alert productDialog = new Alert(Alert.AlertType.INFORMATION);
                productDialog.setTitle("Product Found");
                productDialog.setHeaderText(null);
                productDialog.setContentText("Product ID: " + product.getId() + "\nName: " +
                        product.getName() + "\nStock: " + product.getStock());
                productDialog.showAndWait();
            } else {
                showWarningAlert("Product not found with name or ID: " + searchInput);
            }
        });
    }

    private void displaySummary() {
        if (inventory.isEmpty()) {
            showInfoAlert("Inventory is empty.");
        } else {
            TableView<Product> tableView = createSummaryTableView();

            Stage summaryStage = new Stage();
            summaryStage.setTitle("Display Summary");
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(tableView);

            Scene scene = new Scene(borderPane, 400, 300);
            summaryStage.setScene(scene);
            summaryStage.show();
        }
    }

    private void checkStockLevels() {
        if (inventory.isEmpty()) {
            showInfoAlert("Inventory is empty.");
        } else {
            StringBuilder stockInfo = new StringBuilder("Current Stock Levels:\n\n");
            for (Product product : inventory) {
                stockInfo.append("ID: ").append(product.getId())
                        .append(", Name: ").append(product.getName())
                        .append(", Stock: ").append(product.getStock()).append("\n");
            }
            showInfoAlert(stockInfo.toString());
        }
    }

    private TableView<Product> createSummaryTableView() {
        TableView<Product> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableView.getColumns().addAll(idColumn, nameColumn, stockColumn);

        tableView.setItems(inventory);

        return tableView;
    }

    private Product findProductByNameOrId(String searchInput) {
        for (Product product : inventory) {
            if (String.valueOf(product.getId()).equals(searchInput) || product.getName().equalsIgnoreCase(searchInput)) {
                return product;
            }
        }
        return null;
    }

    private Product findProductById(int productId) {
        for (Product product : inventory) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Product {
        private final IntegerProperty id;
        private final StringProperty name;
        private final IntegerProperty stock;

        public Product(int id, String name, int stock) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.stock = new SimpleIntegerProperty(stock);
        }

        public int getId() {
            return id.get();
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public int getStock() {
            return stock.get();
        }

        public IntegerProperty stockProperty() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock.set(stock);
        }
    }
}
