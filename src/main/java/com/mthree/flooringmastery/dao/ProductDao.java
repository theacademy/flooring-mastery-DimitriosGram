package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts() throws DataPersistenceException;
    Product getProduct(String productType) throws DataPersistenceException;
    Tax getTax(String state) throws DataPersistenceException;

}
