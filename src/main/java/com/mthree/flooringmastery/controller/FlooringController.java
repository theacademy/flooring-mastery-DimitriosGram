package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.DataPersistenceException;
import com.mthree.flooringmastery.service.FlooringService;
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

        String userInput = view.getDateInput("Enter order date (MMddyyyy): ");
        LocalDate date = LocalDate.parse(userInput, DateTimeFormatter.ofPattern("MMddyyyy"));
        List<Order> orders = service.getOrdersByDate(date);

        if (orders.isEmpty()) {
            view.displayMessage("No orders found for " + date + ".");
        } else {
            view.displayOrders(orders);
        }
    }

    private void addOrder() throws DataPersistenceException {
        view.printDivider();

        String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
        LocalDate date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));

        String customerName = view.getCustomerName();
        String state = view.getStateInput();
        String productType = view.getProductTypeInput();
        BigDecimal area = view.getAreaInput();

        Order newOrder = service.createOrder(date, customerName, state, productType, area);

        view.displayOrderSummary(newOrder);
        view.printBlankLine();
        boolean confirm = view.getYesNoConfirmation("Place this order? (Y/N): ");

        if (confirm) {
            view.displayMessage("Order successfully added!");
        } else {
            view.displayMessage("Order cancelled.");
        }

    }

    private void editOrder() throws DataPersistenceException {
        view.printDivider();

        String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
        LocalDate date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));

        List<Order> orders = service.getOrdersByDate(date);
        if (orders.isEmpty()) {
            view.displayMessage("No orders found for that date.");
            return;
        }

        view.displayOrders(orders);
        view.printBlankLine();
        int orderNum = view.getOrderNumberInput("Enter order number to edit: ");

        Order orderToEdit = service.getOrder(date, orderNum);
        if (orderToEdit == null) {
            view.displayMessage("Order not found.");
            return;
        }

        Order editedOrder = view.getEditedOrderInfo(orderToEdit);
        service.updateOrder(date, editedOrder);

        view.displayMessage("Order updated successfully!");
    }

    private void removeOrder() throws DataPersistenceException {
        view.printDivider();

        String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
        LocalDate date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));

        List<Order> orders = service.getOrdersByDate(date);
        if (orders.isEmpty()) {
            view.displayMessage("No orders found for that date.");
            return;
        }

        view.displayOrders(orders);
        view.printBlankLine();
        int orderNum = view.getOrderNumberInput("Enter order number to remove: ");

        Order orderToRemove = service.getOrder(date, orderNum);
        if (orderToRemove == null) {
            view.displayMessage("Order not found.");
            return;
        }

        boolean confirm = view.getYesNoConfirmation("Are you sure you want to remove this order? (Y/N): ");
        if (confirm) {
            service.removeOrder(date, orderNum);
            view.displayMessage("Order successfully removed!");
        } else {
            view.displayMessage("Order removal cancelled.");
        }
    }



}
