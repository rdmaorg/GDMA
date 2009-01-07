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
                 {key:"locked"}
                 ]
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
