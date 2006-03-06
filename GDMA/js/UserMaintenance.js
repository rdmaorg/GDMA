/*
 * SOCO 03/03/2006
 * Submit this form
 */
function submitThisForm(button)
{ 
   document.getElementById("userAction").value=button;	
   
   	if(button=="Delete")
   	{       
        populateUserForm();
        
   		if(validateUserForm())
    	if(confirm("Are you sure you wish to delete GDMA User: "+document.getElementById("newUserName").value))
    	{
    	   document.forms[0].submit();
       	}
       	return;       	
    }
    else if(button=="Save")
    {
       if(validateUserForm())
    	   document.forms[0].submit();
    }    
}
/*
 * SOCO 25/01/2006
 * Populates the values on the User Form with the radio selected user
 */
function populateUserForm()
{  
    var selectedUser = getCheckedValue(document.forms[0].elements['selectedUserRadio']);
    document.getElementById("oldUserId").value=document.getElementById("userId"+selectedUser).value;	
    document.getElementById("newUserFirstName").value=document.getElementById("userFirstName"+selectedUser).value;	
    document.getElementById("newUserLastName").value=document.getElementById("userLastName"+selectedUser).value;	
    document.getElementById("newUserName").value=document.getElementById("userName"+selectedUser).value;	
   
}

/*
 * SOCO 25/01/2006
 * Clears the values from the User Form
 */
function clearUserForm()
{
   document.getElementById("oldUserId").value="";	
   document.getElementById("newUserFirstName").value="";	
   document.getElementById("newUserLastName").value="";	
   document.getElementById("newUserName").value="";	
}

/*
 * SOCO 03/03/2006
 * Validates the values from the User Form
 */
function validateUserForm()
{
  
   if(document.getElementById("newUserFirstName").value==""	
      || document.getElementById("newUserLastName").value==""	
      || document.getElementById("newUserName").value=="")
   {
       alert("Please complete all fields before saving.");   
       return false;
   }	
   return true;
}

// return the value of the radio button that is checked
// return an empty string if none are checked, or
// there are no radio buttons
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
		
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}
