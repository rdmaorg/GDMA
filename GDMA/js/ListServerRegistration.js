function doNew(){
	window.location.href='ServerRegistration.jsp?action=NEW';
}

function doEdit(){
	if(selectedID)
		window.location.href='ServerRegistration.jsp?action=EDIT&id='+selectedID;
	else
		alert('Please select a server to edit');
}  

function doDelete(){
	if(selectedID)
		window.location.href='ServerRegistration.jsp?action=DELETE&id='+selectedID;
	else
		alert('Please select a server to delete');
} 

function doTables(){
	if(selectedID)
		window.location.href='ListTables.jsp?server_id='+selectedID;
	else
		alert('Please select a server');
} 
  