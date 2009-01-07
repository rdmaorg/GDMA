package com.vodafone.gdma.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.vodafone.gdma.GdmaFacade;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.web.command.Filter;
import com.vodafone.gdma.web.command.PaginatedRequest;
import com.vodafone.gdma.web.command.PaginatedResponse;

@SuppressWarnings("unchecked")
public class DownloadController extends AbstractController {

    protected final Log logger = LogFactory.getLog(getClass());

    protected GdmaFacade gdmaFacade;

    public GdmaFacade getGdmaFacade() {
        return gdmaFacade;
    }

    public void setGdmaFacade(GdmaFacade gdmaFacade) {
        this.gdmaFacade = gdmaFacade;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String txtPaginatedRequest = ServletRequestUtils.getRequiredStringParameter(request, "txtPaginatedRequest");
        Map classMap = new HashMap();
        PaginatedRequest paginatedRequest = new PaginatedRequest();
        classMap.put("filters", Filter.class);

        JSONObject jsonObject = JSONObject.fromObject(txtPaginatedRequest);
        paginatedRequest = (PaginatedRequest) JSONObject.toBean(jsonObject, PaginatedRequest.class, classMap);

        Map<String, Object> model = new HashMap<String, Object>();
        PaginatedResponse paginatedResponse = gdmaFacade.getDynamicDao().get(paginatedRequest);
        Table table = gdmaFacade.getTableDao().get(paginatedRequest.getTableId());

        List<List> records = paginatedResponse.getRecords();
        List<String> headers = new ArrayList<String>();

        for (Column column : table.getColumns()) {
            if (column.isDisplayed()) {
                headers.add(column.getName());
            }
        }

        model.put("records", records);
        model.put("headers", headers);

        String uri = request.getRequestURI();
        String extension = uri.substring(uri.length() - 3);

        return new ModelAndView(extension, "model", model);

    }
}
