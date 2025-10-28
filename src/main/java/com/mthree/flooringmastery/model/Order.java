package com.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {

    private int orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    // === Getters and Setters ===

    public int getOrderNumber(){return orderNumber;}
    public void setOrderNumber(int orderNumber){this.orderNumber = orderNumber;}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }

    public BigDecimal getCostPerSquareFoot() { return costPerSquareFoot; }
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) { this.costPerSquareFoot = costPerSquareFoot; }

    public BigDecimal getLaborCostPerSquareFoot() { return laborCostPerSquareFoot; }
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) { this.laborCostPerSquareFoot = laborCostPerSquareFoot; }

    public BigDecimal getMaterialCost() { return materialCost; }
    public void setMaterialCost(BigDecimal materialCost) { this.materialCost = materialCost; }

    public BigDecimal getLaborCost() { return laborCost; }
    public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }


    // === Utility ===
    @Override
    public String toString(){

        return orderNumber + "," + customerName + "," + state + "," + taxRate + "," + productType + "," +
                area + "," + costPerSquareFoot + "," + laborCostPerSquareFoot + "," + materialCost + "," +
                laborCost + "," + tax + "," + total;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber &&
                Objects.equals(customerName, order.customerName) &&
                Objects.equals(state,order.state);
    }

    @Override
    public int hashCode(){
        return Objects.hash(orderNumber, customerName, state);
    }
}
