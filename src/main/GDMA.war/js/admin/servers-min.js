YAHOO.namespace("GDMA.admin.servers");
YAHOO.GDMA.admin.servers.loadConnectionTypes=function(){GdmaAdmin.getConnectionTypes(function(A){YAHOO.GDMA.admin.servers.connectionTypes=A
})
};
YAHOO.GDMA.admin.servers.fields={fields:[{key:"id",parser:YAHOO.util.DataSource.parseNumber},{key:"name",parser:YAHOO.util.DataSource.parseString},{key:"username",parser:YAHOO.util.DataSource.parseString},{key:"password",parser:YAHOO.util.DataSource.parseString},{key:"connectionUrl",parser:YAHOO.util.DataSource.parseString},{key:"connectionType"},{key:"prefix",parser:YAHOO.util.DataSource.parseString},{key:"active"}]};
YAHOO.GDMA.admin.servers.doubleClick=function(B){var A=YAHOO.GDMA.admin.dataTable.getTrEl(B.target);
YAHOO.GDMA.admin.dataTable.selectRow(A);
YAHOO.GDMA.admin.dataTable.focus();
YAHOO.GDMA.admin.viewTables()
};
YAHOO.GDMA.admin.servers.loadServers=function(A){GdmaAdmin.getServers(function(C){YAHOO.GDMA.admin.servers.servers=C;
var B=new YAHOO.util.DataSource(C);
B.responseType=YAHOO.util.DataSource.TYPE_JSARRAY;
B.responseSchema=YAHOO.GDMA.admin.servers.fields;
B.columnDefs=YAHOO.GDMA.admin.servers.columnDefs;
A(B)
});
YAHOO.GDMA.admin.saveFunction=GdmaAdmin.saveServers;
YAHOO.GDMA.admin.deleteFunction=GdmaAdmin.deleteServer;
YAHOO.GDMA.admin.doubleClick=YAHOO.GDMA.admin.servers.doubleClick
};
YAHOO.GDMA.admin.servers.connectionTypeFormatter=function(B,A,C,D){YAHOO.GDMA.admin.servers.oRecord=A;
YAHOO.GDMA.admin.servers.elCell=B;
if(D){B.innerHTML=D.name
}else{B.innerHTML="NOT SELECTED"
}};
YAHOO.GDMA.admin.servers.editDropdown=function(G,A){var D=G.cell;
var B=G.record;
var F=G.column;
var E=G.container;
var C=G.value?G.value.id:-1;
var H=E.appendChild(document.createElement("select"));
YAHOO.GDMA.utilities.populateDropDown(H,YAHOO.GDMA.admin.servers.connectionTypes,"id","name",C,true);
YAHOO.util.Event.addListener(H,"change",function(){A._oCellEditor.value=YAHOO.GDMA.admin.servers.connectionTypes[H.selectedIndex-1];
A.fireEvent("editorUpdateEvent",{editor:A._oCellEditor})
});
A._focusEl(H)
};
YAHOO.GDMA.admin.servers.editActiveServerDropdown=function(G,F){var H=G.cell;
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
YAHOO.GDMA.admin.servers.columnDefs=[{label:"",formatter:YAHOO.GDMA.utilities.rownumFormatter,minWidth:20},{key:"name",label:"Name",editor:"textbox",sortable:true,resizeable:true,width:75},{key:"username",label:"Username",editor:"textbox",sortable:true,resizeable:true,width:75},{key:"password",label:"Password",editor:"textbox",formatter:YAHOO.GDMA.utilities.passwordFormatter,resizeable:true,width:75},{key:"connectionUrl",label:"Connection URL",editor:"textbox",resizeable:true,width:200},{key:"connectionType",label:"Connection Type",formatter:YAHOO.GDMA.admin.servers.connectionTypeFormatter,editor:YAHOO.GDMA.admin.servers.editDropdown,resizeable:true,width:100},{key:"prefix",label:"Prefix",editor:"textbox",resizeable:true,width:50},{key:"active",label:"Active",formatter:YAHOO.GDMA.admin.servers.activeFormatter,editor:YAHOO.GDMA.admin.servers.editActiveServerDropdown,resizeable:true,width:50}];