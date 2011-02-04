YAHOO.namespace("GDMA.admin.users");
YAHOO.GDMA.admin.users.fields={fields:[{key:"id",parser:YAHOO.util.DataSource.parseNumber},{key:"firstName",parser:YAHOO.util.DataSource.parseString},{key:"lastName",parser:YAHOO.util.DataSource.parseString},{key:"userName",parser:YAHOO.util.DataSource.parseString},{key:"admin"},{key:"locked"},{key:"active"}]};
YAHOO.GDMA.admin.users.editActiveUserDropdown=function(G,F){var H=G.cell;
var I=G.record;
var E=G.column;
var C=G.container;
var D=C.appendChild(document.createElement("select"));
var B=new Array();
var A=new Array();
B[0]=1;
B[1]=0;
A[0]="true";
A[1]="false";
YAHOO.GDMA.utilities.populateDropDown2(D,B,A,1,2,-1,true);
YAHOO.util.Event.addListener(D,"change",function(){F._oCellEditor.value=A[D.selectedIndex-1];
F.fireEvent("editorUpdateEvent",{editor:F._oCellEditor})
});
F._focusEl(D)
};
YAHOO.GDMA.admin.users.columnDefs=[{label:"",formatter:YAHOO.GDMA.utilities.rownumFormatter,minWidth:20},{key:"firstName",label:"First Name",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"lastName",label:"Last Name",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"userName",label:"Username",sortable:true,resizeable:true,editor:"textbox",width:100},{key:"admin",label:"Admin",formatter:"checkbox",resizeable:true,width:75},{key:"locked",label:"Locked",formatter:"checkbox",resizeable:true,width:75},{key:"active",label:"Active",formatter:YAHOO.GDMA.admin.users.activeFormatter,editor:YAHOO.GDMA.admin.users.editActiveUserDropdown,resizeable:true,width:50}];
YAHOO.GDMA.admin.users.loadUsers=function(A){GdmaAdmin.getUsers(function(C){var B=new YAHOO.util.DataSource(C);
B.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
B.responseSchema=YAHOO.GDMA.admin.users.fields;
B.columnDefs=YAHOO.GDMA.admin.users.columnDefs;
A(B)
});
YAHOO.GDMA.admin.saveFunction=GdmaAdmin.saveUsers;
YAHOO.GDMA.admin.deleteFunction=GdmaAdmin.deleteUser
};