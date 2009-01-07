YAHOO.namespace("GDMA.admin.columnconfig");
YAHOO.GDMA.admin.columnconfig.loadList=function(A,C,B){YAHOO.GDMA.admin.columnconfig.serverid=A;
YAHOO.GDMA.admin.columnconfig.tableId=C;
YAHOO.GDMA.admin.columnconfig.tableName=B;
YAHOO.GDMA.admin.columnconfig.initLayout()
};
YAHOO.GDMA.admin.columnconfig.displayList=function(C){YAHOO.GDMA.admin.columnconfig.columns=C;
var E=YAHOO.util.Dom.get("divDlgPopupFormLeft");
E.innerHTML+='<SELECT size="20" id="lstColumns" />';
var A=YAHOO.util.Dom.get("lstColumns");
for(var B=0;
B<C.length;
B++){var D=document.createElement("option");
D.value=C[B]["id"];
D.text=C[B]["name"];
A.options[B]=D
}};
YAHOO.GDMA.admin.columnconfig.moveItemUp=function(){var A=YAHOO.util.Dom.get("lstColumns");
if(A.selectedIndex==-1){YAHOO.GDMA.dialog.showInfoDialog("No column selected ...","Please select the a column")
}else{YAHOO.GDMA.admin.columnconfig.moveItem(A,A.selectedIndex,A.selectedIndex-1)
}};
YAHOO.GDMA.admin.columnconfig.moveItemDown=function(){var A=YAHOO.util.Dom.get("lstColumns");
if(A.selectedIndex==-1){YAHOO.GDMA.dialog.showInfoDialog("No column selected ...","Please select the a column")
}else{YAHOO.GDMA.admin.columnconfig.moveItem(A,A.selectedIndex,A.selectedIndex+1)
}};
YAHOO.GDMA.admin.columnconfig.moveItemTop=function(){var A=YAHOO.util.Dom.get("lstColumns");
if(A.selectedIndex==-1){YAHOO.GDMA.dialog.showInfoDialog("No column selected ...","Please select the a column")
}else{while(A.selectedIndex>0){YAHOO.GDMA.admin.columnconfig.moveItem(A,A.selectedIndex,A.selectedIndex-1)
}}};
YAHOO.GDMA.admin.columnconfig.moveItemBottom=function(){var A=YAHOO.util.Dom.get("lstColumns");
if(A.selectedIndex==-1){YAHOO.GDMA.dialog.showInfoDialog("No column selected ...","Please select the a column")
}else{while(A.selectedIndex<(A.options.length-1)){YAHOO.GDMA.admin.columnconfig.moveItem(A,A.selectedIndex,A.selectedIndex+1)
}}};
YAHOO.GDMA.admin.columnconfig.moveItem=function(A,C,D){try{if(D<A.options.length&&D>=0){var B=A.options[C].value;
var F=A.options[C].text;
A.options[C].value=A.options[D].value;
A.options[C].text=A.options[D].text;
A.options[D].value=B;
A.options[D].text=F;
A.selectedIndex=D
}}catch(E){alert(E)
}};
YAHOO.GDMA.admin.columnconfig.save=function(){var A=function(){var B=YAHOO.util.Dom.get("lstColumns");
var E=YAHOO.GDMA.admin.columnconfig.columns;
for(var D=0;
D<B.options.length;
D++){var F=B.options[D].value;
for(var C=0;
C<E.length;
C++){if(E[C].id==F){E[C].orderby=D;
break
}}}GdmaAdmin.saveColumns(E,function(){YAHOO.GDMA.dialog.showInfoDialog("Saved!","Record(s) saved!");
YAHOO.GDMA.admin.refreshColumns();
YAHOO.GDMA.admin.columnconfig.cancel()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(A,"Please confirm save","Are you sure you wish to save the changes?")
};
YAHOO.GDMA.admin.columnconfig.cancel=function(){YAHOO.GDMA.admin.columnconfig.panel.destroy()
};
YAHOO.GDMA.admin.columnconfig.initLayout=function(){var A=YAHOO.util.Dom.get("divDlgPopupForm");
if(!A){A=document.createElement("div");
A.id="divDlgPopupForm";
document.body.insertBefore(A,document.body.firstChild)
}YAHOO.GDMA.admin.columnconfig.panel=new YAHOO.widget.Panel("divDlgPopupForm",{draggable:true,close:false,modal:true,width:"308px",xy:[140,100]});
YAHOO.GDMA.admin.columnconfig.panel.setHeader("Column Order for table "+YAHOO.GDMA.admin.columnconfig.tableName);
YAHOO.GDMA.admin.columnconfig.panel.setBody('<div id="divDlgPopupFormLayout"></div>');
YAHOO.GDMA.admin.columnconfig.panel.beforeRenderEvent.subscribe(function(){try{YAHOO.util.Event.onAvailable("divDlgPopupFormLayout",function(){var I=YAHOO.util.Dom.get("divDlgPopupFormLayout");
var C=YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormLeft",I);
var J=YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormCenterButtons",I);
var D=YAHOO.GDMA.utilities.createElement("div","divDlgPopupFormBottom",I);
YAHOO.GDMA.admin.columnconfig.layout=new YAHOO.widget.Layout("divDlgPopupFormLayout",{height:400,width:300,units:[{position:"left",width:150,height:60,body:"divDlgPopupFormLeft",header:"User Without Access"},{position:"center",width:100,height:60,body:"divDlgPopupFormCenterButtons"},{position:"bottom",height:40,body:"divDlgPopupFormBottom"}]});
YAHOO.GDMA.admin.columnconfig.layout.render();
var G=YAHOO.util.Dom.get("divDlgPopupFormCenterButtons");
var K=YAHOO.GDMA.toolbar.createToolBarButton("Top","btnMoveTop",G,YAHOO.GDMA.admin.columnconfig.moveItemTop,"Move column to top","show");
var M=YAHOO.GDMA.toolbar.createToolBarButton("Up","btnMoveUp",G,YAHOO.GDMA.admin.columnconfig.moveItemUp,"Move column up","show");
var F=YAHOO.GDMA.toolbar.createToolBarButton("Down","btnMoveDown",G,YAHOO.GDMA.admin.columnconfig.moveItemDown,"Move column down","show");
var H=YAHOO.GDMA.toolbar.createToolBarButton("Bottom","btnMoveBottom",G,YAHOO.GDMA.admin.columnconfig.moveItemBottom,"Move column to bottom","show");
G=YAHOO.util.Dom.get("divDlgPopupFormBottom");
var E=YAHOO.GDMA.toolbar.createToolBarButton("Save","btnPopupFormSave",G,YAHOO.GDMA.admin.columnconfig.save,"Save changes to the column order","show");
var L=YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnPopupFormCancel",G,YAHOO.GDMA.admin.columnconfig.cancel,"Close this dialog without saving changes to the column order","show");
GdmaAdmin.getColumnsForTable(YAHOO.GDMA.admin.columnconfig.serverid,YAHOO.GDMA.admin.columnconfig.tableId,function(N){YAHOO.GDMA.admin.columnconfig.displayList(N)
})
})
}catch(B){alert(B)
}});
YAHOO.GDMA.admin.columnconfig.panel.render()
};