package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringServiceImpl implements FlooringService {

    private final OrderDao orderDao;
    private final ProductDao productDao;
    private final TaxDao taxDao;

    public FlooringServiceImpl(OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws DataPersistenceException {
        return orderDao.getOrdersByDate(date);
    }

    @Override
    public List<Product> getAllProducts() throws DataPersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public List<Tax> getAllTaxes() throws DataPersistenceException {
        return taxDao.getAllTaxes();
    }

    @Override
    public Order createOrder(LocalDate date, String customerName, String state,
                             String productType, BigDecimal area) throws DataPersistenceException {

        // --- Load and validate tax ---
        Tax tax = taxDao.getTax(state);
        if (tax == null) {
            throw new DataPersistenceException("Tax data not found for state: " + state);
        }


        // --- Load and validate product ---
        Product product = productDao.getProduct(productType);
        if (product == null) {
            throw new DataPersistenceException("Product data not found for product type: " + productType);
        }


        // --- Create new order object ---
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setState(state);
        order.setTaxRate(tax.getTaxRate());
        order.setProductType(productType);
        order.setArea(area);
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        // --- Perform calculations ---
        BigDecimal materialCost = area.multiply(product.getCostPerSquareFoot());
        BigDecimal laborCost = area.multiply(product.getLaborCostPerSquareFoot());
        BigDecimal taxRate = tax.getTaxRate().divide(new BigDecimal("100"));
        BigDecimal taxTotal = (materialCost.add(laborCost)).multiply(taxRate);
        BigDecimal total = materialCost.add(laborCost).add(taxTotal);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxTotal);
        order.setTotal(total);

        // --- Generate and set order number ---
        int nextOrderNumber = orderDao.getNextOrderNumber(date);
        order.setOrderNumber(nextOrderNumber);

        // --- Save order to file ---
        orderDao.addOrder(date, order);

        return order;
    }

    @Override
    public void addOrder(LocalDate date, Order order) throws DataPersistenceException {
        orderDao.addOrder(date, order);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        return orderDao.getOrder(date, orderNumber);
    }

    @Override
    public void updateOrder(LocalDate date, Order order) throws DataPersistenceException {
        // Recalculate pricing before saving
        Product product = productDao.getProduct(order.getProductType());
        Tax tax = taxDao.getTax(order.getState());

        if (product != null && tax != null) {
            order.setCostPerSquareFoot(product.getCostPerSquareFoot());
            order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
            order.setTaxRate(tax.getTaxRate());

            BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());
            BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());
            BigDecimal taxTotal = (materialCost.add(laborCost))
                    .multiply(order.getTaxRate().divide(new BigDecimal("100")));
            BigDecimal total = materialCost.add(laborCost).add(taxTotal);

            order.setMaterialCost(materialCost);
            order.setLaborCost(laborCost);
            order.setTax(taxTotal);
            order.setTotal(total);
        }

        orderDao.updateOrder(date, order);
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        orderDao.removeOrder(date, orderNumber);
    }




}
