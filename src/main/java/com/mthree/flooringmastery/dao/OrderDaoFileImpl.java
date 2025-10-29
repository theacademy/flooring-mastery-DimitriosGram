package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao {

    private static final String ORDER_FOLDER = "Orders/";
    private static final String FILE_PREFIX = "Orders_";
    private static final String FILE_SUFFIX = ".txt";
    private static final String DELIMITER = ",";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("MMddyyyy");

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws DataPersistenceException {
        String fileName = ORDER_FOLDER + FILE_PREFIX + date.format(FILE_DATE_FORMAT) + FILE_SUFFIX;
        List<Order> orders = new ArrayList<>();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            scanner.nextLine(); // Skip header
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                if (tokens.length == 12) {
                    Order order = new Order();
                    order.setOrderNumber(Integer.parseInt(tokens[0]));
                    order.setCustomerName(tokens[1]);
                    order.setState(tokens[2]);
                    order.setTaxRate(new BigDecimal(tokens[3]));
                    order.setProductType(tokens[4]);
                    order.setArea(new BigDecimal(tokens[5]));
                    order.setCostPerSquareFoot(new BigDecimal(tokens[6]));
                    order.setLaborCostPerSquareFoot(new BigDecimal(tokens[7]));
                    order.setMaterialCost(new BigDecimal(tokens[8]));
                    order.setLaborCost(new BigDecimal(tokens[9]));
                    order.setTax(new BigDecimal(tokens[10]));
                    order.setTotal(new BigDecimal(tokens[11]));
                    orders.add(order);
                }
            }
        } catch (FileNotFoundException e) {
            // No file for that date = no orders
            return new ArrayList<>();
        } catch (IOException e) {
            throw new DataPersistenceException("Error reading order file: " + fileName, e);
        }

        return orders;
    }

    @Override
    public void addOrder(LocalDate date, Order order) throws DataPersistenceException {
        String fileName = ORDER_FOLDER + FILE_PREFIX + date.format(FILE_DATE_FORMAT) + FILE_SUFFIX;
        File file = new File(fileName);
        List<Order> existing = new ArrayList<>();

        // Ensure directory exists
        new File(ORDER_FOLDER).mkdirs();

        // Load existing orders if file exists
        if (file.exists()) existing = getOrdersByDate(date);

        // Assign next order number
        int nextOrderNum = existing.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;
        order.setOrderNumber(nextOrderNum);
        existing.add(order);

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                    + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                    + "LaborCost,Tax,Total");
            for (Order o : existing) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        o.getOrderNumber(), o.getCustomerName(), o.getState(), o.getTaxRate(),
                        o.getProductType(), o.getArea(), o.getCostPerSquareFoot(),
                        o.getLaborCostPerSquareFoot(), o.getMaterialCost(), o.getLaborCost(),
                        o.getTax(), o.getTotal());
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Could not save order data.", e);
        }
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        List<Order> orders = getOrdersByDate(date);
        orders.removeIf(o -> o.getOrderNumber() == orderNumber);

        // Rewrite file
        String fileName = ORDER_FOLDER + FILE_PREFIX + date.format(FILE_DATE_FORMAT) + FILE_SUFFIX;
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                    + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            for (Order o : orders) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        o.getOrderNumber(), o.getCustomerName(), o.getState(), o.getTaxRate(),
                        o.getProductType(), o.getArea(), o.getCostPerSquareFoot(),
                        o.getLaborCostPerSquareFoot(), o.getMaterialCost(), o.getLaborCost(),
                        o.getTax(), o.getTotal());
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Could not save updated order data.", e);
        }
    }


    @Override
    public void saveCurrentWork() throws DataPersistenceException {
        // Optional stretch goal
    }

    @Override
    public int getNextOrderNumber(LocalDate date) throws DataPersistenceException {
        return 0;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws DataPersistenceException {
        return getOrdersByDate(date).stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateOrder(LocalDate date, Order updatedOrder) throws DataPersistenceException {
        List<Order> orders = getOrdersByDate(date);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == updatedOrder.getOrderNumber()) {
                orders.set(i, updatedOrder);
                break;
            }
        }
        // rewrite file
        String fileName = ORDER_FOLDER + FILE_PREFIX + date.format(FILE_DATE_FORMAT) + FILE_SUFFIX;
        new File(ORDER_FOLDER).mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            for (Order o : orders) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        o.getOrderNumber(), o.getCustomerName(), o.getState(), o.getTaxRate(),
                        o.getProductType(), o.getArea(), o.getCostPerSquareFoot(),
                        o.getLaborCostPerSquareFoot(), o.getMaterialCost(), o.getLaborCost(),
                        o.getTax(), o.getTotal());
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Could not save updated order data.", e);
        }
    }



}
