package ie.clients.gdma.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateEditor extends PropertyEditorSupport {

	protected SimpleDateFormat format;
	
	public void setFormat(String format) {
		this.format = new SimpleDateFormat(format);
	}
	
	public void setAsText(String arg) throws IllegalArgumentException {
		try {
			this.setValue(format.parse(arg));
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Could not parse the date", ex);
		}
	}
	
}
