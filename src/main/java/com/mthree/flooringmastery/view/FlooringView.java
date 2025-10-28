package com.mthree.flooringmastery.view;

import com.mthree.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class FlooringView {
    private final UserIO io;

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("<<FLOORING PROGRAM>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public void displayMessage(String msg) {
        io.print(msg);
    }

    public String getDateInput(String prompt) {
        return io.readString(prompt);
    }

    public void displayOrders(List<Order> orders) {
        for (Order order : orders) {
            io.print(order.getOrderNumber() + ": " + order.getCustomerName() +
                    " - " + order.getProductType() + " (£" + order.getTotal() + ")");
        }
    }

    public String getCustomerName() {
        return io.readString("Enter customer name: ");
    }

    public String getStateInput() {
        return io.readString("Enter state abbreviation (e.g., CA, TX): ").toUpperCase();
    }

    public String getProductTypeInput() {
        return io.readString("Enter product type (e.g., Tile, Wood): ");
    }

    public BigDecimal getAreaInput() {
        return io.readBigDecimal("Enter area in square feet (minimum 100): ");
    }



    public void displayOrderSummary(Order order) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.UK);

        System.out.println("----- Order Summary -----");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("State: " + order.getState() + " (" + order.getTaxRate() + "%)");
        System.out.println("Product: " + order.getProductType());
        System.out.println("Area: " + order.getArea());
        System.out.println("Material Cost: " + currency.format(order.getMaterialCost()));
        System.out.println("Labor Cost: " + currency.format(order.getLaborCost()));
        System.out.println("Tax: " + currency.format(order.getTax()));
        System.out.println("Total: " + currency.format(order.getTotal()));
    }


    public boolean getYesNoConfirmation(String prompt) {
        String answer = io.readString(prompt);
        return answer.equalsIgnoreCase("Y");
    }

}
