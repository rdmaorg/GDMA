function doEdit(){
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
		document.getElementById("mode").value="UPDATE";
		document.forms[0].submit();
	}
}  
