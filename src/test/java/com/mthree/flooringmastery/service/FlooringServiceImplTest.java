package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.*;
import com.mthree.flooringmastery.model.*;
import com.mthree.flooringmastery.model.Order;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FlooringServiceImpl class.
 *
 * This test suite verifies key business logic behaviors, including:
 *  - Correct order calculations
 *  - Proper exception handling for invalid product input
 */
public class FlooringServiceImplTest {

    private FlooringServiceImpl service;

    /**
     * Initializes the service layer with stub DAO implementations
     * before each test case.
     */
    @BeforeEach
    void setUp() {
        OrderDao orderDao = new OrderDaoStubImpl();
        ProductDao productDao = new ProductDaoStubImpl();
        TaxDao taxDao = new TaxDaoStubImpl();
        service = new FlooringServiceImpl(orderDao, productDao, taxDao);
    }

    /**
     * Verifies that order totals are correctly calculated
     * when a valid product and tax are provided.
     */
    @Test
    void testCreateOrderCalculations() throws DataPersistenceException, OrderValidationException  {
        LocalDate date = LocalDate.of(2025, 1, 1);

        // Create a new valid order
        com.mthree.flooringmastery.model.Order order =
                service.createOrder(date, "John", "TX", "Tile", new BigDecimal("100"));

        // Display for debugging
        System.out.println("\n----- TEST: Create Order Calculations -----");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("State: " + order.getState());
        System.out.println("Product: " + order.getProductType());
        System.out.println("Area: " + order.getArea());
        System.out.println("Total: " + order.getTotal());
        System.out.println("-------------------------------------------");

        // Assertions to confirm expected behavior
        assertEquals("TX", order.getState());
        assertEquals("Tile", order.getProductType());
        assertNotNull(order.getTotal(), "Total should not be null");
        assertTrue(order.getTotal().compareTo(BigDecimal.ZERO) > 0, "Total should be positive");
    }

    /**
     * Ensures that a DataPersistenceException is thrown
     * when the service is given an invalid product type.
     */
    @Test
    void testThrowsExceptionForInvalidProduct() {
        LocalDate date = LocalDate.of(2025, 1, 1);

        // Expect an exception for non-existent product type
        assertThrows(DataPersistenceException.class, () -> {
            service.createOrder(date, "John", "TX", "InvalidProduct", new BigDecimal("100"));
        });

        System.out.println("✅ Exception correctly thrown for invalid product type.");
    }

    @Test
    void testThrowsExceptionForInvalidState() {
        LocalDate date = LocalDate.of(2025, 1, 1);

        assertThrows(DataPersistenceException.class, () -> {
            service.createOrder(date, "John", "InvalidState", "Tile", new BigDecimal("100"));
        });

        System.out.println("✅ Exception correctly thrown for invalid state abbreviation.");
    }

    @Test
    void testGetNextOrderNumberIncrementsProperly() throws DataPersistenceException, OrderValidationException {
        LocalDate date = LocalDate.of(2025, 1, 1);

        Order order1 = service.createOrder(date, "Alice", "TX", "Tile", new BigDecimal("120"));
        service.getOrderDao().addOrder(date, order1);

        Order order2 = service.createOrder(date, "Bob", "TX", "Tile", new BigDecimal("150"));
        service.getOrderDao().addOrder(date, order2);

        int nextOrderNumber = service.getOrderDao().getNextOrderNumber(date);

        assertEquals(3, nextOrderNumber, "Next order number should increment correctly.");
    }


    @Test
    void testThrowsExceptionForInvalidArea() {
        LocalDate date = LocalDate.of(2025, 1, 1);

        assertThrows(OrderValidationException.class, () -> {
            service.createOrder(date, "John", "TX", "Tile", new BigDecimal("50"));
        });

        System.out.println("✅ Exception correctly thrown for area below minimum limit.");
    }

    @Test
    void testAddAndRetrieveOrder() throws DataPersistenceException, OrderValidationException {
        LocalDate date = LocalDate.of(2025, 1, 1);

        Order newOrder = service.createOrder(date, "Eve", "TX", "Tile", new BigDecimal("150"));
        service.getOrderDao().addOrder(date, newOrder);

        Order retrieved = service.getOrderDao().getOrder(date, newOrder.getOrderNumber());

        assertNotNull(retrieved, "Order should be retrievable after being added.");
        assertEquals("Eve", retrieved.getCustomerName());
    }

}
