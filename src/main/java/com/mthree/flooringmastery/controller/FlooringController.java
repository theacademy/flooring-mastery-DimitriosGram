package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.DataPersistenceException;
import com.mthree.flooringmastery.service.FlooringService;
import com.mthree.flooringmastery.service.OrderValidationException;
import com.mthree.flooringmastery.view.FlooringView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlooringController {

    private final FlooringService service;
    private final FlooringView view;

    public FlooringController(FlooringService service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        boolean keepGoing = true;
        while (keepGoing) {
            int menuSelection = view.printMenuAndGetSelection();

            try {
                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 6:
                        keepGoing = false;
                        view.displayMessage("Goodbye!");
                        break;
                    default:
                        view.displayMessage("Feature not yet implemented.");
                        break;
                }

            } catch (DataPersistenceException e) {
                view.displayMessage("ERROR: " + e.getMessage());
            }
        }
    }

    private void displayOrders() throws DataPersistenceException {
        view.printDivider();

        LocalDate date = null;
        boolean validDate = false;

        // Keep prompting until user enters a valid date
        while (!validDate) {
            try {
                String userInput = view.getDateInput("Enter order date (MMddyyyy): ");
                date = LocalDate.parse(userInput, DateTimeFormatter.ofPattern("MMddyyyy"));
                validDate = true;
            } catch (Exception e) {
                view.displayMessage("❌ Invalid date format. Please use MMddyyyy (e.g., 02222025).");
            }
        }

        List<Order> orders = service.getOrdersByDate(date);

        if (orders.isEmpty()) {
            view.displayMessage("No orders found for " + date + ".");
        } else {
            view.displayOrders(orders);
        }
    }


    private void addOrder() throws DataPersistenceException {
        view.printDivider();

        // --- Get valid date ---
        LocalDate date = null;
        while (date == null) {
            try {
                String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
                date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));
            } catch (Exception e) {
                view.displayMessage("Invalid date format. Please use MMddyyyy (e.g., 02222025).");
            }
        }

        // --- Get customer name ---
        String customerName = view.getCustomerName();

        // --- Get valid state ---
        String state = "";
        boolean validState = false;

        while (!validState) {
            state = view.getStateInput().trim().toUpperCase();
            final String inputState = state;

            try {
                List<com.mthree.flooringmastery.model.Tax> taxes = service.getAllTaxes();

                if (taxes == null || taxes.isEmpty()) {
                    view.displayMessage("⚠️ No tax data available to validate. Please check your tax file.");
                    break;
                }

                boolean matchFound = taxes.stream()
                        .anyMatch(t -> t.getStateAbbreviation() != null &&
                                t.getStateAbbreviation().equalsIgnoreCase(inputState));

                if (matchFound) {
                    validState = true;
                } else {
                    view.displayMessage("❌ Invalid state abbreviation. Try again (e.g., TX, CA).");
                }

            } catch (DataPersistenceException e) {
                view.displayMessage("⚠️ Could not load tax data. Please try again or check your file.");
                break;
            }
        }

        // --- Get valid product type ---
        String productType = "";
        boolean validProduct = false;

        while (!validProduct) {
            productType = view.getProductTypeInput().trim();
            final String inputProduct = productType.toLowerCase(); // normalize

            try {
                List<com.mthree.flooringmastery.model.Product> products = service.getAllProducts();

                // ✅ If DAO returned null or empty, stop loop to avoid infinite tries
                if (products == null || products.isEmpty()) {
                    view.displayMessage("⚠️ No products available to validate. Please check your data files.");
                    break;
                }

                boolean matchFound = products.stream()
                        .anyMatch(p -> p.getProductType() != null &&
                                p.getProductType().equalsIgnoreCase(inputProduct));

                if (matchFound) {
                    validProduct = true;
                } else {
                    view.displayMessage("❌ Invalid product type. Try again (e.g., Tile, Wood).");
                }

            } catch (DataPersistenceException e) {
                view.displayMessage("⚠️ Could not load product data. Please try again or check your file.");
                // ✅ Break instead of looping infinitely on repeated load failure
                break;
            }
        }

        // --- Get valid area ---
        BigDecimal area = null;
        while (area == null) {
            try {
                area = view.getAreaInput();
                if (area.compareTo(new BigDecimal("100")) < 0) {
                    view.displayMessage("Area must be at least 100 square feet.");
                    area = null;
                }
            } catch (Exception e) {
                view.displayMessage("Please enter a valid numeric value for area.");
            }
        }

        // --- Create and confirm order ---
        try {
            Order newOrder = service.createOrder(date, customerName, state, productType, area);
            view.displayOrderSummary(newOrder);
            view.printBlankLine();

            boolean confirm = view.getYesNoConfirmation("Place this order? (Y/N): ");
            if (confirm) {
                service.addOrder(date, newOrder);
                view.displayMessage("Order successfully added!");
            } else {
                view.displayMessage("Order cancelled.");
            }

        } catch (OrderValidationException e) {
            view.displayMessage("Invalid order: " + e.getMessage());

        } catch (DataPersistenceException e) {
            view.displayMessage("Error: Could not create order. " + e.getMessage());
        }
    }

        private void editOrder() throws DataPersistenceException {
        view.printDivider();

        LocalDate date = null;
        boolean validDate = false;

        // Validate date format
        while (!validDate) {
            try {
                String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
                date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));
                validDate = true;
            } catch (Exception e) {
                view.displayMessage("❌ Invalid date format. Please use MMddyyyy (e.g., 02222025).");
            }
        }

        List<Order> orders = service.getOrdersByDate(date);
        if (orders.isEmpty()) {
            view.displayMessage("No orders found for that date.");
            return;
        }

        view.displayOrders(orders);
        view.printBlankLine();

        // Validate order number input
        int orderNum = -1;
        boolean validOrderNum = false;

        while (!validOrderNum) {
            try {
                orderNum = view.getOrderNumberInput("Enter order number to edit: ");
                validOrderNum = true;
            } catch (Exception e) {
                view.displayMessage("❌ Please enter a valid order number (e.g., 1, 2, 3).");
            }
        }

        Order orderToEdit = service.getOrder(date, orderNum);
        if (orderToEdit == null) {
            view.displayMessage("Order not found.");
            return;
        }

        Order editedOrder = view.getEditedOrderInfo(orderToEdit);
        service.updateOrder(date, editedOrder);

        view.displayMessage("✅ Order updated successfully!");
    }


    private void removeOrder() throws DataPersistenceException {
        view.printDivider();

        LocalDate date = null;
        boolean validDate = false;

        // Validate date format
        while (!validDate) {
            try {
                String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
                date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));
                validDate = true;
            } catch (Exception e) {
                view.displayMessage("❌ Invalid date format. Please use MMddyyyy (e.g., 02222025).");
            }
        }

        // Load existing orders for the date
        List<Order> orders = service.getOrdersByDate(date);
        if (orders.isEmpty()) {
            view.displayMessage("No orders found for that date.");
            return;
        }

        view.displayOrders(orders);
        view.printBlankLine();

        // Validate order number input
        int orderNum = -1;
        boolean validOrderNum = false;

        while (!validOrderNum) {
            try {
                orderNum = view.getOrderNumberInput("Enter order number to remove: ");
                validOrderNum = true;
            } catch (Exception e) {
                view.displayMessage("❌ Please enter a valid numeric order number (e.g., 1, 2, 3).");
            }
        }

        // Check if order exists
        Order orderToRemove = service.getOrder(date, orderNum);
        if (orderToRemove == null) {
            view.displayMessage("Order not found.");
            return;
        }

        // Confirm removal
        boolean confirm = view.getYesNoConfirmation("Are you sure you want to remove this order? (Y/N): ");
        if (confirm) {
            service.removeOrder(date, orderNum);
            view.displayMessage("✅ Order successfully removed!");
        } else {
            view.displayMessage("Order removal cancelled.");
        }
    }




}
