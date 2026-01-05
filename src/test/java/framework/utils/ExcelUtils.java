package framework.utils;

import framework.logger.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {

    private static final Logger logger;

    static {
        try {
            logger = Logger.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ExcelUtils() {
    }

    public static void writeToExcelFile(String pathToFile, String sheetName, String[][] data) {
        if (data.length == 0) return;

        try (Workbook workbook = getWorkbook(pathToFile)) {
            Sheet sheet = isSheetExists(workbook, sheetName) ? workbook.getSheet(sheetName) : workbook.createSheet(sheetName);

            CellStyle headerCellStyle = setAndReturnSheetStyle(workbook, true);
            CellStyle cellStyle = setAndReturnSheetStyle(workbook, false);

            int currentRow = 0;
            int amountOfColumns = 0;

            for (String[] rowToCreate : data) {
                Row row = sheet.createRow(++currentRow);
                int columnCount = 0;
                for (String cellToCreate : rowToCreate) {
                    Cell cell = row.createCell(++columnCount);
                    cell.setCellValue(cellToCreate);
                    cell.setCellStyle(currentRow == 1 ? headerCellStyle : cellStyle);
                    amountOfColumns = Math.max(columnCount, amountOfColumns);
                }
            }
            for (int i = 1; i < amountOfColumns + 1; i++) {
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 15);
            }

            writeToWorkbook(pathToFile, workbook);
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    public static void addSheetToXlsxFile(String pathToFile, String sheetName, int sheetOrder) {
        try (Workbook workbook = getWorkbook(pathToFile)) {
            Sheet sheet = workbook.createSheet(sheetName);
            workbook.setSheetOrder(sheet.getSheetName(), sheetOrder);
            workbook.setActiveSheet(sheetOrder);
            writeToWorkbook(pathToFile, workbook);
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    private static boolean isSheetExists(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName) != null;
    }

    private static void writeToWorkbook(String pathToFile, Workbook workbook) {
        try (FileOutputStream fos = new FileOutputStream(pathToFile)) {
            workbook.write(fos);
        } catch (IOException exc) {
            logger.fatal(exc.getMessage());
        }
    }

    private static Workbook getWorkbook(String pathToFile) {
        File xlsxFile = new File(pathToFile);
        Workbook workbook = null;

        if (xlsxFile.exists() && xlsxFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(xlsxFile)) {
                workbook = new XSSFWorkbook(fis);
            } catch (IOException exc) {
                logger.fatal(exc.getMessage());
            }
        } else {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    private static CellStyle setAndReturnSheetStyle(Workbook workbook, boolean isHeader) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        if (isHeader) {
            font.setBold(true);
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
        } else {
            style.setWrapText(true);
            font.setFontHeightInPoints((short) 11);
        }
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}
