function doEdit(serverID){
	if(selectedID == -1 ){
		alert('Please save the list of tables first');
	}else{
		if(selectedID){
			var server_id = document.forms['frmMain'].elements['server_id'].value;
			window.location.href='ListColumns.jsp?server_id='+server_id+'&table_id='+selectedID;
		}else{
			alert('Please select a table');
	 	}
	 }
} 

// SOCO 24/01/2006
// open the access permissions screen
// user must have checked on saved a table before it can be opened in access screen
// this is the same functionality as doEdit(serverID) above
function accessPermissions(serverID)
{

// GO TO USER MAINTENANCE FOR NOW......
if(false)
{ window.location.href='UserMaintenance.jsp';
}
else
{
	if(selectedID == -1 )
	{
		alert('Please save the list of tables first');
	}else
	{
		if(selectedID)
		{
			var server_id = document.forms['frmMain'].elements['server_id'].value;
			window.location.href='TableAccessPermissions.jsp?server_id='+server_id+'&table_id='+selectedID;
		}else{
			alert('Please select a table');
	 	}
	 }
}
} 

