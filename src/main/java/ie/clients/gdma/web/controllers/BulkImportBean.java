package ie.clients.gdma.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public class BulkImportBean {
	
	private MultipartFile fiDataFile;
	private String txtPaginatedRequest;

	public void setFiDataFile(MultipartFile file) {
		this.fiDataFile = file;
	}

	public MultipartFile getFiDataFile() {
		return fiDataFile;
	}
	
	public String getTxtPaginatedRequest() {
		return txtPaginatedRequest;
	}

	public void setTxtPaginatedRequest(String paginatedRequest) {
	    this.txtPaginatedRequest = paginatedRequest;
    }
}
