package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stub implementation of TaxDao for testing.
 *
 * Provides one sample tax entry for Texas (TX).
 */
public class TaxDaoStubImpl implements TaxDao {

    @Override
    public List<Tax> getAllTaxes() throws DataPersistenceException {
        // Return a single TX tax object
        return List.of(getTax("TX"));
    }

    @Override
    public Tax getTax(String stateAbbreviation) throws DataPersistenceException {
        // Only recognizes TX as a valid state
        if (!stateAbbreviation.equalsIgnoreCase("TX")) {
            return null;
        }

        Tax tax = new Tax();
        tax.setStateAbbreviation("TX");
        tax.setStateName("Texas");
        tax.setTaxRate(new BigDecimal("4.45"));
        return tax;
    }
}
