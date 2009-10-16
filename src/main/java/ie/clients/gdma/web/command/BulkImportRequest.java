package ie.clients.gdma.web.command;

public class BulkImportRequest extends UpdateRequest {

	private String file;

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

}
