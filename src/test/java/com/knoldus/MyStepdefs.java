package com.knoldus;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

public class MyStepdefs {

    @Given("I gave excel file as an input")
    public void i_gave_excel_file_as_an_input(DataReader dataTable) {
//        System.out.println(dataTable.getAllRows());
//        System.out.println(dataTable.getASingleRow());
        System.out.println(dataTable.getAllRows());
//        System.out.println(dataTable.getAllRows().);
    }

    @DataTableType
    public DataReader excelToDataTable(Map<String, String> entry){
        ReaderConfig config = new ReaderConfig.ReaderConfigBuilder()
                .setFileName(entry.get("Excel"))
                .setFileLocation(entry.get("Location"))
                .setSheetName(entry.get("Sheet"))
                .setIndex(Integer.valueOf(entry.getOrDefault("Index","0")))
                .build();
        return new ExcelDataReader(config);
    }
}
