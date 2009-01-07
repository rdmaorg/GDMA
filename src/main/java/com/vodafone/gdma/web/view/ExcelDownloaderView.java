package com.vodafone.gdma.web.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.vodafone.gdma.util.ExcelCreator;

@SuppressWarnings("unchecked")
public class ExcelDownloaderView extends AbstractExcelView {

    protected final Log logger = LogFactory.getLog(getClass());

    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) {

        model = (Map<String, Object>) model.get("model");

        List<String> headers = (List<String>) model.get("headers");
        List<List> records = (List<List>) model.get("records");
        ExcelCreator.buildWorkbook(workbook, headers, records);
    }

}