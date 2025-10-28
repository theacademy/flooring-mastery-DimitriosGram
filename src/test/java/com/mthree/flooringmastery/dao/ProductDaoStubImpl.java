package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stub implementation of ProductDao for testing.
 *
 * Returns a hardcoded product (Tile) to verify calculation logic.
 */
public class ProductDaoStubImpl implements ProductDao {

    @Override
    public List<Product> getAllProducts() throws DataPersistenceException {
        // Always return a single "Tile" product for consistency
        return List.of(getProduct("Tile"));
    }

    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        // Only return valid product if it matches "Tile"
        if (!productType.equalsIgnoreCase("Tile")) {
            return null; // simulate missing product
        }

        Product product = new Product();
        product.setProductType("Tile");
        product.setCostPerSquareFoot(new BigDecimal("3.50"));
        product.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        return product;
    }

    @Override
    public Tax getTax(String state) throws DataPersistenceException {
        // Not used here, included to satisfy interface
        return null;
    }
}
