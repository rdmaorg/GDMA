YAHOO.namespace ("GDMA.server.browser");

YAHOO.GDMA.server.browser.startRequest = function() {
    GdmaAjax.getServerTableList(YAHOO.GDMA.server.browser.initTree);
};

YAHOO.GDMA.server.browser.initTree = function (serverList) {
    YAHOO.GDMA.server.browser.tree = new YAHOO.widget.TreeView("divServerBrowser");
    var root = YAHOO.GDMA.server.browser.tree.getRoot();
    for(i=0;i<serverList.length;i++){
        var tmpNode = new YAHOO.widget.TextNode(serverList[i].name, root, false);
        tmpNode.labelStyle = "icon-server"; 
        for(j=0;j<serverList[i].tables.length;j++){
            var leafNode = new YAHOO.widget.TextNode(serverList[i].tables[j].name, tmpNode, false);
            leafNode.labelStyle = "icon-table";
            leafNode.serverId = serverList[i].id
            leafNode.tableId = serverList[i].tables[j].id;

            
        }
    }

    YAHOO.GDMA.server.browser.tree.subscribe("labelClick", function(node) { 
        if(node.tableId){
            if(YAHOO.GDMA.datagrid.init)
                YAHOO.GDMA.datagrid.init(node.serverId, node.tableId);
            
        }
    }); 
    YAHOO.GDMA.server.browser.tree.draw();
};

YAHOO.util.Event.onDOMReady( function(){
    YAHOO.util.Event.onAvailable('divServerBrowser', YAHOO.GDMA.server.browser.startRequest);
});