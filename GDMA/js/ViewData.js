function doEdit(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		document.getElementById("trSearch").style.display = "none";
		clearSearch();
		fillFields();
		document.getElementById("mode").value="UPDATE";
    doSubmit('EditData.jsp','');
	}
}

function doInsert(){
	document.getElementById("mode").value="INSERT";
  doSubmit('EditData.jsp','');
}   

function doDelete(){
	if(selectedID == null){
		alert("Please select a row");
	}else{
		if(confirm("Are you sure you wish to delete this row")){			
			fillFields();
			document.getElementById("mode").value="DELETE";
			doSubmit('EditData.jsp','');
		}
	}

}


function fillFields(){
	//get a pointer to the row which contains the INPUTS for posting
	var trRow = document.getElementById("trRow"+selectedID);
  
	//get a pointer to the header row we are going to post
  var trHeader = document.getElementById("trHeader");
  
  var fldRow = trRow.getElementsByTagName("INPUT");
	var fldHeader = trHeader.getElementsByTagName("INPUT");
	if(fldRow && fldRow.length > 0 &&
	  fldHeader && fldHeader.length > 0){
		for( i = 0; i < fldRow.length ; i++){
  		fldHeader[i].value = fldRow[i].value;
		}
	}
}

function doDownload(file){
  doSubmit(file,'frmHidden');
}

function doToggleSearch(){
	var oSearchPanel = document.getElementById("trSearch");
  var oSearchButton = document.getElementById("tdSearchButton");  
	if(oSearchPanel){
		if(oSearchPanel.style.display == "none"){
			oSearchPanel.style.display = "block";
      oSearchButton.style.display = "none";
    }
		else{
			oSearchPanel.style.display = "none";
      oSearchButton.style.display = "block";
    }
	}
}

function doSearch(){
  doDisplayLoading()
	document.getElementById("mode").value='SEARCH';
  doSubmit('ViewData.jsp','');  
}

function doAllRecords(){
  doDisplayLoading()
	clearSearch();
	document.getElementById("mode").value='ALL';
	document.getElementById("frmMain").action='ViewData.jsp';
  doSubmit('ViewData.jsp','');
}

function clearSearch(){
	//first clear out all old search values
	var flds = document.getElementById("frmMain").getElementsByTagName("INPUT");
	if(flds && flds.length > 0){
		for( i = 0; i < flds.length ; i++){
			if(flds[i].name && flds[i].name.length > 7 && flds[i].name.substr(0,7) == "search_")
				flds[i].value = "";
		}
	}	
  
  var flds = document.getElementById("frmMain").getElementsByTagName("SELECT");
  if(flds && flds.length > 0){
    for( i = 0; i < flds.length ; i++){
      if(flds[i].name && flds[i].name.length > 7 && flds[i].name.substr(0,7) == "search_")
        flds[i].selectedIndex  = 0;
    }
  }   
}


function doReset(){

  var tr = document.getElementById("trSearch");
  
  var flds = tr.getElementsByTagName("INPUT");
  if(flds && flds.length > 0 ){
    for( i = 0; i < flds.length ; i++){
      flds[i].value = '';
    }
  }
  
  var flds = tr.getElementsByTagName("SELECT");
  if(flds && flds.length > 0 ){
    for( i = 0; i < flds.length ; i++){
      flds[i].selectedIndex = 0;
    }
  }
}


function doSubmit(strAction, strTarget){
  document.getElementById("frmMain").action= strAction;
  document.getElementById("frmMain").target= strTarget;     
  document.getElementById("frmMain").submit();  
}

function doDisplayLoading(){

  var frmMenu = window.top.frames[0];
  var divLoading;
  if(frmMenu){  
    divLoading = frmMenu.document.getElementById('divLoading');
    divLoading.style.display='block';
  }
}

function doHideLoading(){
  var frmMenu = window.top.frames[0];
  var divLoading;
  if(frmMenu){  
    divLoading = frmMenu.document.getElementById('divLoading');
    divLoading.style.display='none';
  }
}

function doKeyPress(event)
{
  var n;
  (window.Event) ? n = window.Event.which : n= event.keyCode
  if( n == 13)
    doSearch();
}


