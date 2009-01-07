YAHOO.namespace("GDMA.admin.users");
YAHOO.GDMA.admin.users.fields={fields:[{key:"id",parser:YAHOO.util.DataSource.parseNumber},{key:"firstName",parser:YAHOO.util.DataSource.parseString},{key:"lastName",parser:YAHOO.util.DataSource.parseString},{key:"userName",parser:YAHOO.util.DataSource.parseString},{key:"admin"},{key:"locked"}]};
YAHOO.GDMA.admin.users.columnDefs=[{label:"",formatter:YAHOO.GDMA.utilities.rownumFormatter,minWidth:20},{key:"firstName",label:"First Name",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"lastName",label:"Last Name",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"userName",label:"Username",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"admin",label:"Admin",formatter:"checkbox",resizeable:true,width:75},{key:"locked",label:"Locked",formatter:"checkbox",resizeable:true,width:75}];
YAHOO.GDMA.admin.users.loadUsers=function(A){GdmaAdmin.getUsers(function(C){var B=new YAHOO.util.DataSource(C);
B.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
B.responseSchema=YAHOO.GDMA.admin.users.fields;
B.columnDefs=YAHOO.GDMA.admin.users.columnDefs;
A(B)
});
YAHOO.GDMA.admin.saveFunction=GdmaAdmin.saveUsers;
YAHOO.GDMA.admin.deleteFunction=GdmaAdmin.deleteUser
};