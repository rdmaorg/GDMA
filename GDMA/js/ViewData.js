function doEdit(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		//get a pointer to the header row which contains the INPUTS for posting
		var trRow = document.getElementById("trRow"+selectedID);
		//get a pointer to the row we are going to post
	   var trHeader = document.getElementById("trHeader");
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
			document.getElementById("mode").value="UPDATE";
			document.forms[0].submit();
		}
	}
}

function doInsert(){
	document.getElementById("mode").value="INSERT";
	document.forms[0].submit();
}   

function doDelete(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		if(confirm("Are you sure you wish to delete this row")){
			var trRow = document.getElementById("trRow"+selectedID);
		    var trHeader = document.getElementById("trHeader");
		    //make sure we have TDs in the row and the header
		    //this codes fails if the data contains valid HTML
			if(trRow.getElementsByTagName("TD") && 
			   trHeader.getElementsByTagName("INPUT")){
				var tdRow = trRow.getElementsByTagName("TD");
				var fldHeader = trHeader.getElementsByTagName("INPUT");
				//start at 1 because first col is ID
				for( i = 1; i < tdRow.length ; i++){
					fldHeader[i - 1].value = tdRow[i].innerHTML;
				}
				document.getElementById("mode").value="DELETE";
				document.forms[0].submit();
			}
		}
	}
}

