package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringService {

    List<Order> getOrdersByDate(LocalDate date) throws DataPersistenceException;

    List<Product> getAllProducts() throws DataPersistenceException;

    List<Tax> getAllTaxes() throws DataPersistenceException;

    Order createOrder(LocalDate date, String customerName, String state,
                      String productType, BigDecimal area) throws DataPersistenceException;

    void addOrder(LocalDate date, Order order) throws DataPersistenceException;

    Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException;

    void updateOrder(LocalDate date, Order order) throws DataPersistenceException;

    void removeOrder(LocalDate date, int orderNumber) throws DataPersistenceException;

}

