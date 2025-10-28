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
        String userInputDate = view.getDateInput("Enter order date (MMddyyyy): ");
        LocalDate date = LocalDate.parse(userInputDate, DateTimeFormatter.ofPattern("MMddyyyy"));

        String customerName = view.getCustomerName();
        String state = view.getStateInput();
        String productType = view.getProductTypeInput();
        BigDecimal area = view.getAreaInput();

        Order newOrder = service.createOrder(date, customerName, state, productType, area);

        view.displayOrderSummary(newOrder);
        boolean confirm = view.getYesNoConfirmation("Place this order? (Y/N): ");

        if (confirm) {
            view.displayMessage("Order successfully added!");
        } else {
            view.displayMessage("Order cancelled.");
        }

    }

}
