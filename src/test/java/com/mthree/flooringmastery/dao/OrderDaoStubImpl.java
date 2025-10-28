package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Stub implementation of OrderDao used for unit testing.
 *
 * Simulates in-memory order storage without file I/O.
 * Useful for verifying service layer behavior.
 */
public class OrderDaoStubImpl implements OrderDao {

    private final List<Order> orderList = new ArrayList<>();

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws DataPersistenceException {
        // Return all orders regardless of date for simplicity
        return new ArrayList<>(orderList);
    }

    @Override
    public void addOrder(LocalDate date, Order order) throws DataPersistenceException {
        // Simply adds the order to the in-memory list
        orderList.add(order);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        // Find an order by its number
        return orderList.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        // Simulate deletion by filtering out matching order
        orderList.removeIf(o -> o.getOrderNumber() == orderNumber);
    }

    @Override
    public void saveCurrentWork() throws DataPersistenceException {
        // No file saving — stub does nothing
    }

    @Override
    public int getNextOrderNumber(LocalDate date) throws DataPersistenceException {
        // Incrementally generate order numbers
        return orderList.size() + 1;
    }

    @Override
    public void updateOrder(LocalDate date, Order order) throws DataPersistenceException {
        // Remove old version and add updated version
        removeOrder(date, order.getOrderNumber());
        orderList.add(order);
    }
}
