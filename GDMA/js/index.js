function doHideLeftFrame(){
	var tblHidden = document.getElementById("tblHidden");
	var tblMain = document.getElementById("tblMain");
	if(tblMain.style.display=='none'){
		top.document.getElementById('frmContent').cols ="25%,75%";
		tblMain.style.display='block';
		tblHidden.style.display='none';		
			
	}else{
		top.document.getElementById('frmContent').cols ="1%,99%";
		tblMain.style.display='none';
		tblHidden.style.display='block';			
	}
}
