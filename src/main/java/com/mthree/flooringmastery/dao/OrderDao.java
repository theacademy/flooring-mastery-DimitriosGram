package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.DataPersistenceException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getOrdersByDate(LocalDate date) throws DataPersistenceException;
    void addOrder(LocalDate date, Order order) throws DataPersistenceException;
    void removeOrder(LocalDate date, int orderNumber) throws DataPersistenceException;
    void saveCurrentWork() throws DataPersistenceException;
    int getNextOrderNumber(LocalDate date) throws DataPersistenceException;

}
