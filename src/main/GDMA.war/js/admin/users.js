// Create a namespace for this code
YAHOO.namespace("GDMA.admin.users");

// define the datasource fields
YAHOO.GDMA.admin.users.fields = {
      fields : [ 
                 {key:"id", parser:YAHOO.util.DataSource.parseNumber},
                 {key:"firstName", parser:YAHOO.util.DataSource.parseString},
                 {key:"lastName", parser:YAHOO.util.DataSource.parseString},
                 {key:"userName", parser:YAHOO.util.DataSource.parseString},
                 {key:"admin"},
                 {key:"locked"},
                 {key:"active"}
                 ]
};

//Need a custom edit drop down to deal with referenced table
YAHOO.GDMA.admin.users.editActiveUserDropdown = function(oEditor, oSelf) {
    var elCell = oEditor.cell;
    var oRecord = oEditor.record;
    var oColumn = oEditor.column;
    var elContainer = oEditor.container;
    //var value = oEditor.value ? oEditor.value.id : -1;

    var elDropdown2 = elContainer.appendChild(document.createElement("select"));
    
    var activeId = new Array();
    var activeValue = new Array();
    activeId[0] = 1;
    activeId[1] = 0;
    activeValue[0] = "true";
    activeValue[1] = "false";
    YAHOO.GDMA.utilities.populateDropDown2(
            elDropdown2, 
            activeId, 
            activeValue, 
            1,
            2,
            -1, 
            true);
    
    // set up a listener to track the input value
    YAHOO.util.Event.addListener(elDropdown2, "change", function() {
        oSelf._oCellEditor.value = activeValue[elDropdown2.selectedIndex - 1];//elDropdown2.options[elDropdown2.selectedIndex].value;
        oSelf.fireEvent("editorUpdateEvent", {
            editor :oSelf._oCellEditor
        });
    });

    // Focus the dropdown
    oSelf._focusEl(elDropdown2);
};

// define the users columns
YAHOO.GDMA.admin.users.columnDefs = [ {
        label :"",
        formatter : YAHOO.GDMA.utilities.rownumFormatter,
        minWidth: 20
    },{
        key :"firstName",
        label :"First Name",
        sortable:true,
        resizeable:true,
        editor :"textbox",
        width:100
    }, {
        key :"lastName",
        label :"Last Name",
        sortable:true,
        resizeable:true,
        editor :"textbox",
        width:100
    },{
        key :"userName",
        label :"Username",
        sortable:true,
        resizeable:true, 
        editor :"textbox",
        width:100
    },{
        key :"admin",
        label :"Admin",
        formatter:"checkbox",
        resizeable:true,
        width:75
    }, {
        key :"locked",
        label :"Locked",
        formatter:"checkbox",
        resizeable:true,
        width:75
    }, {
        key :"active",
        label :"Active",
        formatter : YAHOO.GDMA.admin.users.activeFormatter,
        editor : YAHOO.GDMA.admin.users.editActiveUserDropdown,
        resizeable:true, 
        width:50
}];



// Load the list of users
YAHOO.GDMA.admin.users.loadUsers = function(fnCallback ) {
  GdmaAdmin.getUsers( function(users) {
      // setup the object backing the user
      var datasource = new YAHOO.util.DataSource(users);
      datasource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;        
      // field names
      datasource.responseSchema = YAHOO.GDMA.admin.users.fields;        
      datasource.columnDefs = YAHOO.GDMA.admin.users.columnDefs;
      fnCallback(datasource);
  });
  // setup the functions need
  YAHOO.GDMA.admin.saveFunction =  GdmaAdmin.saveUsers;
  YAHOO.GDMA.admin.deleteFunction = GdmaAdmin.deleteUser;
};
