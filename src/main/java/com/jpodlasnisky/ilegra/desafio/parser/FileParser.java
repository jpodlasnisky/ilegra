package com.jpodlasnisky.ilegra.desafio.parser;

import com.jpodlasnisky.ilegra.desafio.model.*;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.NumberUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private CSVReader csvReader;
    private FileReader fileReader;
    private File file;

    public FileParser(File file) {
        try {
            this.file = file;
        } catch (Exception e) {
            throw new IllegalArgumentException("File not found");
        }
    }

    public BaseEntity readLine() {
        BaseEntity baseEntity = null;
       try {
           if (csvReader == null) initReader();

           String[] line = csvReader.readNext();

           if (!ArrayUtils.isEmpty(line)) {
               baseEntity = createEntity(baseEntity, line);
               return baseEntity;
           } else {
               return null;
           }


       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
    }

    public List<Item> parseItems(String itemsString){
        List<Item> items = new ArrayList<>();

        itemsString = StringUtils.remove(itemsString, "[");
        itemsString = StringUtils.remove(itemsString, "]");

        String[] itemsArray = StringUtils.split(itemsString, ",");

        for (int i = 0; i < itemsArray.length; i++) {

            String itemString = itemsArray[i];

            String[] itemPart = StringUtils.split(itemString, "-");

            Item item = Item.builder()
                    .itemId(NumberUtils.parseNumber(itemPart[0], Integer.class))
                    .quantity(NumberUtils.parseNumber(itemPart[1], Integer.class))
                    .price(NumberUtils.parseNumber(itemPart[2], Double.class))
                    .build();

            items.add(item);
        }

        return items;
    }

    private BaseEntity createSalesman(String[] line) {
        BaseEntity entity;
        entity = Salesman.builder()
                .cpf(line[1])
                .name(line[2])
                .build();
        return entity;
    }

    private BaseEntity createCustomer(String[] line) {
        BaseEntity entity;
        entity = Customer.builder()
                .cnpj(line[1])
                .name(line[2])
                .businessArea(line[3])
                .build();
        return entity;
    }

    private BaseEntity createSale(String[] line) {
        BaseEntity entity;
        entity = Sale.builder()
                .saleId(NumberUtils.parseNumber(line[1], Integer.class))
                .itemList(parseItems(line[2]))
                .salesman(line[3])
                .build();
        return entity;
    }

    private BaseEntity createEntity(BaseEntity entity, String[] line) {
        if("001".equals(line[0])) {
            entity = createSalesman(line);

        } else if("002".equals(line[0])) {
            entity = createCustomer(line);

        } else if("003".equals(line[0])) {
            entity = createSale(line);
        }
        return entity;
    }

    private void initReader() throws Exception {
        if (fileReader == null) fileReader = new FileReader(file);

        if (csvReader == null) {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator('ç')
                    .build();

            csvReader = new CSVReaderBuilder(fileReader)
                    .withCSVParser(parser)
                    .build();
        }
    }

    public void closeReader() throws Exception {
        try {
            csvReader.close();
            fileReader.close();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }


}
