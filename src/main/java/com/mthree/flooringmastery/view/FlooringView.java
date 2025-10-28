package com.mthree.flooringmastery.view;

import com.mthree.flooringmastery.model.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class FlooringView {
    private final UserIO io;

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        printDivider();
        io.print("<< FLOORING PROGRAM >>");
        io.print("");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");
        printDivider();
        io.print("");
        return io.readInt("Please select from the above choices: ", 1, 6);
    }


    public void displayMessage(String msg) {
        io.print("");
        io.print(msg);
        io.print("");
    }


    public String getDateInput(String prompt) {
        return io.readString(prompt);
    }

    public void displayOrders(List<Order> orders) {
        printBlankLine();
        io.print("\n==============================");
        io.print("        ORDER SUMMARY");
        io.print("==============================\n");
        for (Order o : orders) {
            io.print(String.format(
                    "Order #%d | %s | %s (%s%%) | %s | Area: %s | Total: £%s",
                    o.getOrderNumber(),
                    o.getCustomerName(),
                    o.getState(),
                    o.getTaxRate(),
                    o.getProductType(),
                    o.getArea(),
                    o.getTotal().setScale(2, RoundingMode.HALF_UP)
            ));
        }
        io.print("------------------");
        printBlankLine();
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

        io.print("\n----- Order Summary -----");
        io.print("Customer: " + order.getCustomerName());
        io.print("State: " + order.getState() + " (" + order.getTaxRate() + "%)");
        io.print("Product: " + order.getProductType());
        io.print("Area: " + order.getArea());
        io.print("Material Cost: £" + order.getMaterialCost().setScale(2, RoundingMode.HALF_UP));
        io.print("Labor Cost: £" + order.getLaborCost().setScale(2, RoundingMode.HALF_UP));
        io.print("Tax: £" + order.getTax().setScale(2, RoundingMode.HALF_UP));
        io.print("Total: £" + order.getTotal().setScale(2, RoundingMode.HALF_UP));
        io.print("-------------------------\n");
    }


    public boolean getYesNoConfirmation(String prompt) {
        String response;
        do {
            response = io.readString(prompt).trim().toLowerCase();
        } while (!response.equals("y") && !response.equals("n"));
        io.print(""); // add spacing
        return response.equals("y");
    }


    public int getOrderNumberInput(String prompt) {
        return io.readInt(prompt);
    }

    public Order getEditedOrderInfo(Order order) {
        String newName = io.readString("Enter customer name (" + order.getCustomerName() + "): ");
        if (!newName.trim().isEmpty()) order.setCustomerName(newName);

        String newState = io.readString("Enter state (" + order.getState() + "): ");
        if (!newState.trim().isEmpty()) order.setState(newState);

        String newProduct = io.readString("Enter product (" + order.getProductType() + "): ");
        if (!newProduct.trim().isEmpty()) order.setProductType(newProduct);

        String areaStr = io.readString("Enter area (" + order.getArea() + "): ");
        if (!areaStr.trim().isEmpty()) order.setArea(new BigDecimal(areaStr));

        return order;
    }

    public void printDivider() {
        io.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
    }

    public void printBlankLine() {
        io.print("");
    }



}
