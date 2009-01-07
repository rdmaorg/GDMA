package com.vodafone.gdma.web.view;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.vodafone.gdma.util.Formatter;

public class PdfDownloaderView extends AbstractView {

    private static final String CONTENT_TYPE = "text/csv";

    private static final String EXTENSION = ".csv";

    public PdfDownloaderView() {
        setContentType(CONTENT_TYPE);
    }

    /**
     * Renders the Excel view, given the specified model.
     */
    protected final void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        model = (Map<String, Object>) model.get("model");

        List<String> headers = (List<String>) model.get("headers");
        List<List> records = (List<List>) model.get("records");

        StringBuilder sbTemp = new StringBuilder();
        if (records != null && records.size() > 0) {
            int colIdx = 0;
            boolean addComma = false;
            for (String column : headers) {
                if (addComma) {
                    sbTemp.append(',');
                } else {
                    addComma = true;
                }
                sbTemp.append(column);
            }
            sbTemp.append("\r\n");

            addComma = false;
            for (List recordRow : records) {
                for (int i = 1; i < recordRow.size(); i++) {
                    if (addComma) {
                        sbTemp.append(',');
                    } else {
                        addComma = true;
                    }
                    sbTemp.append(recordRow.get(i) == null ? " " : getValue(recordRow.get(i)));
                }
                sbTemp.append("\r\n");
            }
        } else {
            sbTemp.append("No records found\r\n");
        }

        response.setContentType(getContentType());
        PrintWriter printWriter = response.getWriter();
        printWriter.write(sbTemp.toString());
        printWriter.flush();
    }

    private String getValue(Object object) {
        if (object instanceof Timestamp || object instanceof java.sql.Date || object instanceof Date) {
            return getDateValue(object);
        } else {
            return object == null ? "" : object.toString();
        }
    }

    private String getDateValue(Object object) {
        Date date;
        if (object instanceof Timestamp) {
            date = new Date(((Timestamp) object).getTime());
        } else if (object instanceof java.sql.Date) {
            date = new Date(((java.sql.Date) object).getTime());
        } else {
            date = (Date) object;
        }
        return Formatter.formatDate(date);
    }
}
