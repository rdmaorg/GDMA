YAHOO.namespace("GDMA.admin.access");
YAHOO.GDMA.admin.access.loadList=function(B,A){YAHOO.GDMA.admin.access.tableId=B;
YAHOO.GDMA.admin.access.tableName=A;
YAHOO.GDMA.admin.access.initLayout()
};
YAHOO.GDMA.admin.access.displayLists=function(I){YAHOO.GDMA.admin.access.accessList=I;
var C=I.canAccess;
var D=I.canNotAccess;
var B=YAHOO.util.Dom.get("divDlgAccessRight");
var G=YAHOO.util.Dom.get("divDlgAccessLeft");
B.innerHTML+='<SELECT size="20" id="lstAccess" multiple="multiple" />';
G.innerHTML+='<SELECT size="20" id="lstNoAccess" multiple="multiple" />';
var F=YAHOO.util.Dom.get("lstAccess");
var H=YAHOO.util.Dom.get("lstNoAccess");
F.size=20;
H.size=20;
for(var E=0;
E<C.length;
E++){var A=document.createElement("option");
A.value=C[E]["id"];
A.text=C[E]["userName"];
F.options[E]=A
}for(var E=0;
E<D.length;
E++){var A=document.createElement("option");
A.value=D[E]["id"];
A.text=D[E]["userName"];
H.options[E]=A
}YAHOO.GDMA.dialog.loading.hide()
};
YAHOO.GDMA.admin.access.moveItemToLeft=function(){YAHOO.GDMA.admin.access.moveItem("lstAccess","lstNoAccess")
};
YAHOO.GDMA.admin.access.moveItemToRight=function(){YAHOO.GDMA.admin.access.moveItem("lstNoAccess","lstAccess")
};
YAHOO.GDMA.admin.access.moveAllItemToRight=function(){YAHOO.GDMA.admin.access.moveAllItems("lstNoAccess","lstAccess")
};
YAHOO.GDMA.admin.access.moveAllItemToLeft=function(){YAHOO.GDMA.admin.access.moveAllItems("lstAccess","lstNoAccess")
};
YAHOO.GDMA.admin.access.moveAllItems=function(C,B){var C=YAHOO.util.Dom.get(C);
var B=YAHOO.util.Dom.get(B);
while(C.options.length>0){var A=C.options[0];
var D=document.createElement("option");
D.value=A.value;
D.text=A.text;
B.options[B.options.length]=D;
C.options[0]=null
}};
YAHOO.GDMA.admin.access.moveItem=function(C,B){var C=YAHOO.util.Dom.get(C);
var B=YAHOO.util.Dom.get(B);
if(C.selectedIndex==-1){YAHOO.GDMA.dialog.showInfoDialog("No record selected ...","Please select a user")
}else{while(C.selectedIndex!=-1){var A=C.options[C.selectedIndex];
var D=document.createElement("option");
D.value=A.value;
D.text=A.text;
B.options[B.options.length]=D;
C.options[C.selectedIndex]=null
}}};
YAHOO.GDMA.admin.access.save=function(){var A=function(){var B=YAHOO.util.Dom.get("lstAccess");
var E={};
E.id=YAHOO.GDMA.admin.access.tableId;
E.name=YAHOO.GDMA.admin.access.tableName;
E.users=[];
for(var C=0;
C<B.options.length;
C++){var D=B.options[C];
E.users[C]={};
E.users[C]["id"]=D.value;
E.users[C]["userName"]=D.text
}GdmaAdmin.saveAccessList(E,function(){YAHOO.GDMA.dialog.showInfoDialog("Saved!","Record(s) saved!");
YAHOO.GDMA.admin.access.cancel()
})
};
YAHOO.GDMA.dialog.showYesNoDialog(A,"Please confirm save","Are you sure you wish to save the changes?")
};
YAHOO.GDMA.admin.access.cancel=function(){YAHOO.GDMA.admin.access.panel.destroy()
};
YAHOO.GDMA.admin.access.initLayout=function(){var A=YAHOO.util.Dom.get("divDlgAccess");
if(!A){A=document.createElement("div");
A.id="divDlgAccess";
document.body.insertBefore(A,document.body.firstChild)
}YAHOO.GDMA.admin.access.panel=new YAHOO.widget.Panel("divDlgAccess",{draggable:true,close:false,modal:true,width:"438px",xy:[140,100]});
YAHOO.GDMA.admin.access.panel.setHeader("User Access for table "+YAHOO.GDMA.admin.access.tableName);
YAHOO.GDMA.admin.access.panel.setBody('<div id="divDlgAccessLayout"></div>');
YAHOO.GDMA.admin.access.panel.beforeRenderEvent.subscribe(function(){try{YAHOO.util.Event.onAvailable("divDlgAccessLayout",function(){var J=YAHOO.util.Dom.get("divDlgAccessLayout");
var G=YAHOO.GDMA.utilities.createElement("div","divDlgAccessLeft",J);
var C=YAHOO.GDMA.utilities.createElement("div","divDlgAccessRight",J);
var F=YAHOO.GDMA.utilities.createElement("div","divDlgAccessCenter",J);
var E=YAHOO.GDMA.utilities.createElement("div","divDlgAccessBottom",J);
YAHOO.GDMA.admin.access.layout=new YAHOO.widget.Layout("divDlgAccessLayout",{height:300,width:430,units:[{position:"left",width:150,height:60,body:"divDlgAccessLeft",header:"User Without Access"},{position:"center",width:100,height:60,body:"divDlgAccessCenter"},{position:"right",width:150,height:60,body:"divDlgAccessRight",header:"User With Access"},{position:"bottom",height:40,body:"divDlgAccessBottom"}]});
YAHOO.GDMA.admin.access.layout.render();
var I=YAHOO.util.Dom.get("divDlgAccessCenter");
var K=YAHOO.GDMA.toolbar.createToolBarButton("Grant All","btnAllMoveRight",I,YAHOO.GDMA.admin.access.moveAllItemToRight,"Grant access to all users","show");
var K=YAHOO.GDMA.toolbar.createToolBarButton("Grant","btnMoveRight",I,YAHOO.GDMA.admin.access.moveItemToRight,"Grant access to selected user","show");
var L=YAHOO.GDMA.toolbar.createToolBarButton("Revoke","btnMoveLeft",I,YAHOO.GDMA.admin.access.moveItemToLeft,"Revoke access to selected user","show");
var K=YAHOO.GDMA.toolbar.createToolBarButton("Revoke All","btnAllMoveLeft",I,YAHOO.GDMA.admin.access.moveAllItemToLeft,"Revoke access to all users","show");
I=YAHOO.util.Dom.get("divDlgAccessBottom");
var H=YAHOO.GDMA.toolbar.createToolBarButton("Save","btnAccessListSave",I,YAHOO.GDMA.admin.access.save,"Save changes to the access list","show");
var D=YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnAccessListCancel",I,YAHOO.GDMA.admin.access.cancel,"Close this dialog without saving changes","show");
GdmaAdmin.getAccessListForTable(YAHOO.GDMA.admin.access.tableId,function(M){YAHOO.GDMA.admin.access.displayLists(M)
})
})
}catch(B){alert(B)
}});
YAHOO.GDMA.admin.access.panel.render()
};