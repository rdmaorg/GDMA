function doEdit(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		document.getElementById("trSearch").style.display == "none";
		clearSearch();
		fillFields();
		document.getElementById("mode").value="UPDATE";
		document.getElementById("frmMain").action='EditData.jsp';
		document.getElementById("frmMain").target='';				
		document.getElementById("frmMain").submit();
	}
}

function doInsert(){
	document.getElementById("mode").value="INSERT";
	document.getElementById("frmMain").action='EditData.jsp';
	document.getElementById("frmMain").target='';	
	document.getElementById("frmMain").submit();
}   

function doDelete(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		if(confirm("Are you sure you wish to delete this row")){			
			fillFields();
			document.getElementById("mode").value="DELETE";
			document.getElementById("frmMain").action='EditData.jsp';
			document.getElementById("frmMain").target='';
			document.getElementById("frmMain").submit();
		}
	}

}

function fillFields(){
	//get a pointer to the header row which contains the INPUTS for posting
	var trRow = document.getElementById("trRow"+selectedID);
	//get a pointer to the row we are going to post
   var trHeader = document.getElementById("trSearch");
   //make sure we have TDs in the row and inputs in the header
   //this codes fails if the data contains valid HTML
   var tdRow = trRow.getElementsByTagName("TD");
	var fldHeader = trHeader.getElementsByTagName("INPUT");
	if(tdRow && tdRow.length > 0 &&
	   fldHeader && fldHeader.length > 0){
		//start at 1 because first col is ID
		for( i = 1; i < tdRow.length ; i++){
			//This will only occur in the case of a date
			//An input box will contain a timestamp
			var fldTime = tdRow[i].getElementsByTagName("INPUT");
			if(fldTime && fldTime.length > 0){
				var fldTime = tdRow[i].getElementsByTagName("INPUT");
				fldHeader[i - 1].value = fldTime[0].value;
			}else{
				fldHeader[i - 1].value = tdRow[i].innerHTML;
			}
		}
	}
}
function doDownload(file){
	document.getElementById("frmMain").action=file;
	document.getElementById("frmMain").target='frmHidden';
	document.getElementById("frmMain").submit();
}

function doToggleSearch(){
	var o = document.getElementById("trSearch");
	if(o){
		if(o.style.display == "none")
			o.style.display = "block";
		else
			o.style.display = "none";
	}
}

function doSearch(){
	document.getElementById("mode").value='SEARCH';
	document.getElementById("frmMain").action='ViewData.jsp';
	document.getElementById("frmMain").submit();	
}

function doAllRecords(){
	clearSearch();
	document.getElementById("mode").value='ALL';
	document.getElementById("frmMain").action='ViewData.jsp';
	document.getElementById("frmMain").submit();	
}

function clearSearch(){
	//first clear out all old search values
	var flds = document.getElementById("frmMain").getElementsByTagName("INPUT");
	if(flds && flds.length > 0){
		for( i = 0; i < flds.length ; i++){
			if(flds[i].name && flds[i].name.length > 4 && flds[i].name.substr(0,4) == "old_")
				flds[i].value = "";
		}
	}	
}

