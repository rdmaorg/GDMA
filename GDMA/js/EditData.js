function doSave(){
	document.getElementById("action").value="SAVE";
	document.forms[0].submit();			
} 

function doKeyPress(event)
{
  var n;
  (window.Event) ? n = window.Event.which : n= event.keyCode
  if( n == 13)
    doSave();
}
