function doDelete(){
	if(confirm("Are you sure you wish to delete this record?")){
		document.getElementById("action").value="SAVE";
		document.forms[0].submit();			
	}
} 

function doSave(){
	document.getElementById("action").value="SAVE";
	document.forms[0].submit();			
} 
