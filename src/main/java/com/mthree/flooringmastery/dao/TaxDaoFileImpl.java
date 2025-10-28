package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;
import com.mthree.flooringmastery.service.DataPersistenceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TaxDaoFileImpl implements TaxDao {

    private static final String TAX_FILE = "/Data/Taxes.txt";
    private static final String DELIMITER = ",";

    @Override
    public List<Tax> getAllTaxes() throws DataPersistenceException {
        List<Tax> taxes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(TaxDaoFileImpl.class.getResourceAsStream(TAX_FILE)))) {
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(DELIMITER);
                Tax t = new Tax();
                t.setStateAbbreviation(tokens[0]);
                t.setStateName(tokens[1]);
                t.setTaxRate(new BigDecimal(tokens[2]));
                taxes.add(t);
            }

        } catch (IOException e) {
            throw new DataPersistenceException("Could not load tax data.", e);
        }

        return taxes;
    }

    @Override
    public Tax getTax(String stateAbbreviation) throws DataPersistenceException {
        return getAllTaxes().stream()
                .filter(t -> t.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation))
                .findFirst()
                .orElse(null);
    }
}
