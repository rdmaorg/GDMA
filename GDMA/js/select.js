var selectedRow;

function mouseDblClicked(obj)
{
 alert('double clicked');
}

function mouseClicked(obj)
{
  if(selectedRow) 
  {
    selectedRow.className = 'dataBody';
    selectedRow = null;
  }
  
  selectedRow = obj;
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


