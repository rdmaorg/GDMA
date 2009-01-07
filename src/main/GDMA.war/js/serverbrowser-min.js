YAHOO.namespace("GDMA.server.browser");
YAHOO.GDMA.server.browser.startRequest=function(){GdmaAjax.getServerTableList(YAHOO.GDMA.server.browser.initTree)
};
YAHOO.GDMA.server.browser.initTree=function(B){YAHOO.GDMA.server.browser.tree=new YAHOO.widget.TreeView("divServerBrowser");
var A=YAHOO.GDMA.server.browser.tree.getRoot();
for(i=0;
i<B.length;
i++){var D=new YAHOO.widget.TextNode(B[i].name,A,false);
D.labelStyle="icon-server";
for(j=0;
j<B[i].tables.length;
j++){var C=new YAHOO.widget.TextNode(B[i].tables[j].name,D,false);
C.labelStyle="icon-table";
C.serverId=B[i].id;
C.tableId=B[i].tables[j].id
}}YAHOO.GDMA.server.browser.tree.subscribe("labelClick",function(E){if(E.tableId){if(YAHOO.GDMA.datagrid.init){YAHOO.GDMA.datagrid.init(E.serverId,E.tableId)
}}});
YAHOO.GDMA.server.browser.tree.draw()
};
YAHOO.util.Event.onDOMReady(function(){YAHOO.util.Event.onAvailable("divServerBrowser",YAHOO.GDMA.server.browser.startRequest)
});