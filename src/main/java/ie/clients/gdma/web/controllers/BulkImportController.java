package ie.clients.gdma.web.controllers;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.web.command.PaginatedRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class BulkImportController extends SimpleFormController {

	protected final Log logger = LogFactory.getLog(getClass());

	protected GdmaFacade gdmaFacade;

	public GdmaFacade getGdmaFacade() {
		return gdmaFacade;
	}

	public void setGdmaFacade(GdmaFacade gdmaFacade) {
		this.gdmaFacade = gdmaFacade;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws ServletException,
	        IOException {

		final BulkImportBean bean = (BulkImportBean) command;

		final Map<String, String> results = new HashMap<String, String>();
		results.put("error", "");
		results.put("numRecords", "0");

		try {
			// Parse the request
			final MultipartFile file = bean.getFiDataFile();

			logger.info("file: " + file.getOriginalFilename());

			if (!file.getOriginalFilename().endsWith(".csv")) {
				results.put("error", "Invalid file type");
			} else {
				logger.info("type: " + file.getContentType());
				logger.info("stream: " + file.getInputStream());
				logger.info("size: " + file.getSize());

				final JSONObject jsonObject = JSONObject.fromObject(bean.getTxtPaginatedRequest());
				final PaginatedRequest paginatedRequest = (PaginatedRequest) JSONObject.toBean(jsonObject, PaginatedRequest.class);
				final Server server = gdmaFacade.getServerDao().get(paginatedRequest.getServerId());
				final Table table = gdmaFacade.getTableDao().get(paginatedRequest.getTableId());

				final int numRows = gdmaFacade.getDynamicDao().bulkImport(server, table, file.getInputStream());
				results.put("numRecords", "" + numRows);
			}
		} catch (IOException ex) {
			results.put("error", "Could not read the data: " + ex);
			logger.error("Could not read the data", ex);
		}

		return new ModelAndView(getSuccessView(), "results", results);

	}
}
