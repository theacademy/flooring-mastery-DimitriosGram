package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;
import java.util.List;

public interface TaxDao {
    List<Tax> getAllTaxes() throws DataPersistenceException;
    Tax getTax(String stateAbbreviation) throws DataPersistenceException;
}
