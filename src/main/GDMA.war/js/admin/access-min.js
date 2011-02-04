YAHOO.namespace("GDMA.admin.access");
YAHOO.GDMA.admin.access.loadList=function(B,A){YAHOO.GDMA.admin.access.tableId=B;
YAHOO.GDMA.admin.access.tableName=A;
YAHOO.GDMA.admin.access.initLayout()
};
YAHOO.GDMA.admin.access.displayLists=function(H){YAHOO.GDMA.admin.access.userAccessList=H;
var E=YAHOO.util.Dom.get("divDlgAccessCenter");
E.innerHTML+="<BR>&nbsp;<BR><BR>&nbsp;<BR>&nbsp;<BR>";
var D='<div align="center" id="userAccessDiv" class="height: 100px; overflow-y: scroll;  padding: 30px; width: 100%;"><table id="userAccessTable" overflow-y="scroll"  cellpadding="10" width="90%">';
D+='<tr bgcolor="#C0C0C0"><th><b>User</b></th><th><b>Display</b></th><th><b>Update</b></th><th><b>Insert</b></th><th><b>Delete</b></th></tr>';
YAHOO.GDMA.admin.access.userIdList=new Array();
for(var F=0;
F<H.length;
F++){YAHOO.GDMA.admin.access.userIdList[F]=H[F]["user"]["id"];
var I=H[F]["user"]["userName"];
var G="chkDisplay"+H[F]["user"]["id"];
var C="chkUpdate"+H[F]["user"]["id"];
var A="chkInsert"+H[F]["user"]["id"];
var B="chkDelete"+H[F]["user"]["id"];
if((F%2)==0){D+='<tr bgcolor="#EDF5FF"><td align="left" >'+I+"</td>";
if(H[F]["allowDisplay"]){D+='<td align="left" ><INPUT type="checkbox"  id="'+G+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox"  id="'+G+'" /></td>'
}if(H[F]["allowUpdate"]){D+='<td align="left" ><INPUT type="checkbox" id="'+C+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+C+'" /></td>'
}if(H[F]["allowInsert"]){D+='<td align="left" ><INPUT type="checkbox" id="'+A+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+A+'" /></td>'
}if(H[F]["allowDelete"]){D+='<td align="left" ><INPUT type="checkbox" id="'+B+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+B+'" /></td>'
}}else{D+='<tr bgcolor="#FFFFFF"><td align="left" >'+I+"</td>";
if(H[F]["allowDisplay"]){D+='<td align="left" ><INPUT type="checkbox"  id="'+G+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox"  id="'+G+'" /></td>'
}if(H[F]["allowUpdate"]){D+='<td align="left" ><INPUT type="checkbox" id="'+C+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+C+'" /></td>'
}if(H[F]["allowInsert"]){D+='<td align="left" ><INPUT type="checkbox" id="'+A+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+A+'" /></td>'
}if(H[F]["allowDelete"]){D+='<td align="left" ><INPUT type="checkbox" id="'+B+'" checked /></td>'
}else{D+='<td align="left" ><INPUT type="checkbox" id="'+B+'" /></td>'
}}}D+="</table></div>";
E.innerHTML+=D;
E.innerHTML+="<BR>&nbsp;<BR><BR>&nbsp;<BR>&nbsp;<BR>";
YAHOO.GDMA.dialog.loading.hide()
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
YAHOO.GDMA.admin.access.save=function(){var A=function(){var E=GdmaAdmin.getUserList();
var B=YAHOO.GDMA.admin.access.userIdList.length;
var C={};
for(var D=0;
D<B;
D++){C.tableId=YAHOO.GDMA.admin.access.tableId;
C.userId=YAHOO.GDMA.admin.access.userIdList[D];
C.allowDisplay=document.getElementById("chkDisplay"+YAHOO.GDMA.admin.access.userIdList[D]).checked;
C.allowUpdate=document.getElementById("chkUpdate"+YAHOO.GDMA.admin.access.userIdList[D]).checked;
C.allowInsert=document.getElementById("chkInsert"+YAHOO.GDMA.admin.access.userIdList[D]).checked;
C.allowDelete=document.getElementById("chkDelete"+YAHOO.GDMA.admin.access.userIdList[D]).checked;
GdmaAdmin.saveAccessList(C,function(){})
}YAHOO.GDMA.dialog.showInfoDialog("Saved!","Record(s) saved!");
YAHOO.GDMA.admin.access.cancel()
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
YAHOO.GDMA.admin.access.panel.beforeRenderEvent.subscribe(function(){try{YAHOO.util.Event.onAvailable("divDlgAccessLayout",function(){var G=YAHOO.util.Dom.get("divDlgAccessLayout");
var C=YAHOO.GDMA.utilities.createElement("div","divDlgAccessCenter",G);
var D=YAHOO.GDMA.utilities.createElement("div","divDlgAccessBottom",G);
YAHOO.GDMA.admin.access.layout=new YAHOO.widget.Layout("divDlgAccessLayout",{height:300,width:430,units:[{position:"center",width:100,height:60,body:"divDlgAccessCenter"},{position:"bottom",height:40,body:"divDlgAccessBottom"}]});
YAHOO.GDMA.admin.access.layout.render();
divButtons=YAHOO.util.Dom.get("divDlgAccessBottom");
var F=YAHOO.GDMA.toolbar.createToolBarButton("Save","btnAccessListSave",divButtons,YAHOO.GDMA.admin.access.save,"Save changes to the access list","show");
var E=YAHOO.GDMA.toolbar.createToolBarButton("Cancel","btnAccessListCancel",divButtons,YAHOO.GDMA.admin.access.cancel,"Close this dialog without saving changes","show");
GdmaAdmin.getAccessListForTable(YAHOO.GDMA.admin.access.tableId,function(H){YAHOO.GDMA.admin.access.displayLists(H)
})
})
}catch(B){alert(B)
}});
YAHOO.GDMA.admin.access.panel.render()
};