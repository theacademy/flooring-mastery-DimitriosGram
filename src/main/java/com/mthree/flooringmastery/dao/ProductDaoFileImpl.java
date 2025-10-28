package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDaoFileImpl implements ProductDao {

    private static final String DELIMITER = ",";
    private static final String PRODUCT_FILE = "Data/Products.txt";

    @Override
    public List<Product> getAllProducts() throws DataPersistenceException {
        List<Product> products = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PRODUCT_FILE);
        if (inputStream == null) {
            throw new DataPersistenceException("Product file not found: " + PRODUCT_FILE);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(DELIMITER);
                if (tokens.length < 3) continue;

                Product p = new Product();
                p.setProductType(tokens[0]);
                p.setCostPerSquareFoot(new BigDecimal(tokens[1]));
                p.setLaborCostPerSquareFoot(new BigDecimal(tokens[2]));
                products.add(p);
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Could not load product data: " + e.getMessage());
        }

        return products;
    }

    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        if (productType == null) {
            return null;
        }

        return getAllProducts().stream()
                .filter(p -> p.getProductType() != null
                        && p.getProductType().trim().equalsIgnoreCase(productType.trim()))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Tax getTax(String state) throws DataPersistenceException {
        return null;
    }
}
