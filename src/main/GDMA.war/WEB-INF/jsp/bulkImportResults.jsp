<?xml version="1.0" encoding="UTF-8"?>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>

if (window.parent.YAHOO.GDMA.datagrid.doBulkImportDone) {
   window.parent.YAHOO.GDMA.datagrid.doBulkImportDone('<c:out value="${results.error}"/>',
                                                      <c:out value="${results.numRecords}"/>);
}

</script>
