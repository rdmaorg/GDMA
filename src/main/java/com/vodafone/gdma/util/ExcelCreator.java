package com.vodafone.gdma.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@SuppressWarnings("unchecked")
public class ExcelCreator {

    protected final Log logger = LogFactory.getLog(getClass());

    private static String SHEET_NAME = "Sheet 1";

    private static String DATE_FORMAT = "yy-m-d h:mm";

    private HSSFWorkbook workbook;

    private HSSFCellStyle cellDateStyle;

    private HSSFCellStyle headerStyle;

    private List<String> headers;

    private List<List> records;

    public ExcelCreator(HSSFWorkbook workbook, List<String> headers, List<List> records) {
        System.setProperty("java.awt.headless", "true");
        this.workbook = workbook;
        this.headers = headers;
        this.records = records;

        cellDateStyle = workbook.createCellStyle();
        cellDateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(DATE_FORMAT));

        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setFillPattern(HSSFCellStyle.SPARSE_DOTS);
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
    }

    public static void buildWorkbook(HSSFWorkbook workbook, List headers, List records) {
        ExcelCreator excelCreator = new ExcelCreator(workbook, headers, records);
        excelCreator.buildWorkbook();
    }

    protected void buildWorkbook() {

        HSSFSheet sheet;
        HSSFCell cell;

        sheet = workbook.createSheet(SHEET_NAME);

        int colIdx = 0;
        int rowIdx = 0;
        for (String column : headers) {
            cell = getCell(sheet, rowIdx, colIdx);
            cell.setCellStyle(headerStyle);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(column));
            sheet.autoSizeColumn((short) colIdx);
            colIdx++;
        }

        colIdx = 0;
        rowIdx = 1;
        if (records != null && records.size() > 0) {
            for (List recordRow : records) {
                for (int i = 1; i < recordRow.size(); i++) {
                    if (recordRow.get(i) != null) {
                        cell = getCell(sheet, rowIdx, colIdx);
                        setValue(cell, recordRow.get(i));
                    }
                    colIdx++;
                }
                colIdx = 0;
                rowIdx++;
            }
        } else {
            cell = getCell(sheet, rowIdx, colIdx);
            setValue(cell, "No records found");
        }

    }

    private void setValue(HSSFCell cell, Object object) {
        if (object instanceof Timestamp || object instanceof java.sql.Date || object instanceof Date) {
            setDateValue(cell, object);
        } else if (object instanceof Number) {
            setNumberValue(cell, object);
        } else {
            setTextValue(cell, object);
        }
    }

    private void setTextValue(HSSFCell cell, Object object) {
        if (object != null) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(object.toString()));
        }
    }

    private void setDateValue(HSSFCell cell, Object object) {
        Date date;
        if (object instanceof Timestamp) {
            date = new Date(((Timestamp) object).getTime());
        } else if (object instanceof java.sql.Date) {
            date = new Date(((java.sql.Date) object).getTime());
        } else {
            date = (Date) object;
        }

        // FIXME
        // date format we need is not in HSSFCellStyle - so we need format as a string

        // HSSFCellStyle cellStyle = workbook.createCellStyle();
        // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm") ); //works
        // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yy/m/d h:mm")); // doesn't work
        // cell.setCellValue(date);
        // cell.setCellStyle(cellStyle);

        setTextValue(cell, Formatter.formatDate(date));

    }

    private void setNumberValue(HSSFCell cell, Object object) {
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(((Number) object).doubleValue());

    }

    private HSSFCell getCell(HSSFSheet sheet, int row, int col) {
        HSSFRow sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        HSSFCell cell = sheetRow.getCell((short) col);
        if (cell == null) {
            cell = sheetRow.createCell((short) col);
        }
        return cell;
    }

}