package com.knoldus;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ExcelDataReader implements DataReader {

    private final ReaderConfig config;
    private final Logger logger = LoggerFactory.getLogger(ExcelDataReader.class);

    //get the instance of work book
    public ExcelDataReader(ReaderConfig config) {
        this.config = config;
    }

    //Get the sheet using the work book object
    private XSSFWorkbook getWorkBook() throws InvalidFormatException, IOException {
        return new XSSFWorkbook(new File(config.getFileLocation()));
    }

    private XSSFSheet getSheet(XSSFWorkbook workBook) {
        return workBook.getSheet(config.getSheetName());
    }

    public Cell getColumnIndex(Row row) {
        return row.getCell(config.getColumnName());
    }

    //To get the header from the excel file
    private List<String> getHeaders(XSSFSheet sheet) {
        List<String> headers = new ArrayList<String>();
        XSSFRow row = sheet.getRow(0);
        row.forEach((cell) -> {
            headers.add(cell.getStringCellValue());
        });
        return Collections.unmodifiableList(headers);
    }

    @Override
    public List<Map<String, String>> getAllRows() {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        try (XSSFWorkbook workBook = getWorkBook()) {
            XSSFSheet sheet = getSheet(workBook);
            data = getData(sheet);
        } catch (Exception exception) {
            logger.error(exception, () -> {
                return String.format("Not able to read the excel %s from location %s", config.getFileName(),
                        config.getFileLocation());
            });
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(data);

    }

    private List<Map<String, String>> getData(XSSFSheet sheet) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        List<String> headers = getHeaders(sheet);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Map<String, String> rowMap = new HashedMap<String, String>();
            XSSFRow row = sheet.getRow(i);
            forEachWithCounter(row, (index, cell) -> {
                rowMap.put(headers.get(index), cell.getStringCellValue());
            });
            data.add(rowMap);
        }
        return Collections.unmodifiableList(data);
    }

    private Map<String, String> getData(XSSFSheet sheet, int rowIndex) {
        List<String> headers = getHeaders(sheet);
        Map<String, String> rowMap = new HashedMap<String, String>();

        XSSFRow row = sheet.getRow(rowIndex);
        forEachWithCounter(row, (index, cell) -> {
            rowMap.put(headers.get(index), cell.getStringCellValue());
        });

        forEachWithCounter(row, (i, j) -> {

        });

        return Collections.unmodifiableMap(rowMap);
    }

    @Override
    public Map<String, String> getASingleRow() {
        Map<String, String> data = new HashedMap<String, String>();

        try (XSSFWorkbook workBook = getWorkBook()) {
            XSSFSheet sheet = getSheet(workBook);
            data = getData(sheet, config.getIndex());
        } catch (Exception exception) {
            logger.error(exception, () -> {
                return String.format("Not able to read the excel %s from location %s", config.getFileName(),
                        config.getFileLocation());
            });
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(data);
    }

    public List<String> getExcelDataWithRespectToColumn() {
        try (XSSFWorkbook workBook = getWorkBook()) {
            XSSFSheet sheet = getSheet(workBook);
            List<String> values = new ArrayList<String>();
            for (Row r : sheet) {
                Cell c = getColumnIndex(r);
                if (c != null) {
                    if (c.getCellType() == Cell.CELL_TYPE_STRING) {
                        values.add(c.getStringCellValue());
                    } else if (c.getCellType() == Cell.CELL_TYPE_FORMULA && c.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                        values.add(c.getStringCellValue());
                    }
                }
            }
            return values;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void forEachWithCounter(Iterable<Cell> source, BiConsumer<Integer, Cell> biConsumer) {
        int iterator = 0;
        for (Cell cell : source) {
            biConsumer.accept(iterator, cell);
            iterator++;
        }
    }
}
