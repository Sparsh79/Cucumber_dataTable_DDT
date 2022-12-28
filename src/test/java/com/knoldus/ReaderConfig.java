package com.knoldus;

import java.util.Objects;

public class ReaderConfig {

    private final String fileName;
    private final String fileLocation;
    private final String sheetName;
    private int index = -1;
    private int columnName = -1;

    private ReaderConfig(String fileName, String fileLocation, String sheetName, int index, int columnName) {
        this.fileName = fileName;
        this.fileLocation = fileLocation;
        this.sheetName = sheetName;
        this.index = index;
        this.columnName = columnName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getIndex() {
        return index;
    }

    public int getColumnName() {
        return columnName;
    }

    public static class ReaderConfigBuilder {
        private String fileName;
        private String fileLocation;
        private String sheetName;
        private int index = -1;
        private int columnName = -1;

        public ReaderConfigBuilder setFileName(String fileName) {
            this.fileName = fileName;
            return this;

        }

        public ReaderConfigBuilder setColumnName(int columnName) {
            this.columnName = columnName;
            return this;
        }

        public ReaderConfigBuilder setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
            return this;
        }

        public ReaderConfigBuilder setSheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ReaderConfigBuilder setIndex(int index) {
            this.index = index;
            return this;
        }

        public ReaderConfig build() {
            Objects.requireNonNull(fileName);
            Objects.requireNonNull(fileLocation);
            Objects.requireNonNull(sheetName);
            Objects.requireNonNull(columnName);
            return new ReaderConfig(fileName, fileLocation, sheetName, index, columnName);
        }
    }
}
