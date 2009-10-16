package ie.clients.gdma.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.clients.gdma.GdmaFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class BulkImportController extends AbstractController {

	protected final Log logger = LogFactory.getLog(getClass());

	protected GdmaFacade gdmaFacade;

	public GdmaFacade getGdmaFacade() {
		return gdmaFacade;
	}

	public void setGdmaFacade(GdmaFacade gdmaFacade) {
		this.gdmaFacade = gdmaFacade;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		logger.info("multipart: " + isMultipart);

		final Map<String, String> results = new HashMap<String, String>();
		results.put("error", "");
		results.put("numRecords", "0");

		if (isMultipart) {
			// Create a factory for disk-based file items
			final FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			final ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			List<FileItem> items = (List<FileItem>) upload.parseRequest(request);
			for (FileItem fi : items) {
				logger.info("file: " + fi.getName());
				if (!fi.getName().endsWith(".csv")) {
					results.put("error", "Invalid file type");
					break;
				}
				logger.info("type: " + fi.getContentType());
				logger.info("stream: " + fi.getInputStream());
				logger.info("size: " + fi.getSize());

				results.put("numRecords", "123");
				break;
			}
		}
		return new ModelAndView("bulkImportResults", "results", results);

	}
}
