<?xml version="1.0" encoding="UTF-8"?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
          xmlns:sec="http://www.springframework.org/security/tags"
		version="2.0">       

		<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
				
				
<div class="footer" height="100%">
<div style="float:left"><b>Logged in User : </b><sec:authentication property="principal.firstName"/>&nbsp;&nbsp;<sec:authentication property="principal.lastName"/></div>
<div style="float:right">© 2011 AVNET Client Solutions Ltd. 14 Joyce Way, Park West Business Park, Nangor Road, Dublin 12, Ireland</div>

</div>
</jsp:root>
