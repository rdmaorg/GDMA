var selectedRow = null;
var selectedID = null;

function mouseDblClicked(obj,id)
{
	mouseClicked(obj,id);
	doEdit();
}

function mouseClicked(obj,id)
{
  if(selectedRow) 
  {
    selectedRow.className = 'dataBody';
    selectedRow = null;
  }
  
  selectedRow = obj;
  selectedID = id;  
  selectedRow.className = 'dataBodySelected'; 
}

function mouseEntered(obj)
{
  if(selectedRow != obj)
    obj.className = 'dataBodyHover';
}

function mouseExited(obj)
{
  if(selectedRow != obj)
    obj.className = 'dataBody';
}