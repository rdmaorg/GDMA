YAHOO.namespace("GDMA.dialog");
YAHOO.util.Event.onDOMReady(function(){YAHOO.GDMA.dialog.loading=new YAHOO.widget.Panel("loading",{width:"220px",height:"32px",fixedcenter:true,close:false,draggable:false,modal:true,visible:false});
YAHOO.GDMA.dialog.loading.setBody('Loading<br/><img src="'+GDMA_CONTEXT_ROOT+'images/loading_new.gif" />');
YAHOO.GDMA.dialog.loading.render("body")
});
YAHOO.GDMA.dialog.showYesNoDialog=function(C,F,E){YAHOO.GDMA.dialog.loading.hide();
var B=function(){this.hide()
};
var D=function(){this.hide();
C()
};
var A=[{text:"Yes",handler:D,isDefault:false},{text:"No",handler:B,isDefault:true}];
if(!YAHOO.GDMA.dialog.yesNoDialog){YAHOO.GDMA.dialog.yesNoDialog=new YAHOO.widget.SimpleDialog("yesNoDialog",{width:"400px",fixedcenter:true,visible:false,draggable:false,close:false,modal:true,zindex:40000,text:"Do you want to continue?",icon:YAHOO.widget.SimpleDialog.ICON_HELP,constraintoviewport:true});
YAHOO.GDMA.dialog.yesNoDialog.setHeader(F);
YAHOO.GDMA.dialog.yesNoDialog.render("body")
}YAHOO.GDMA.dialog.yesNoDialog.cfg.queueProperty("buttons",A);
YAHOO.GDMA.dialog.yesNoDialog.cfg.refresh();
YAHOO.GDMA.dialog.yesNoDialog.setBody(E);
YAHOO.GDMA.dialog.yesNoDialog.setHeader(F);
YAHOO.GDMA.dialog.yesNoDialog.cfg.setProperty("icon",YAHOO.widget.SimpleDialog.ICON_ALARM);
YAHOO.GDMA.dialog.yesNoDialog.show()
};
YAHOO.GDMA.dialog.showInfoDialog=function(D,C,A){YAHOO.GDMA.dialog.loading.hide();
var B=function(){this.hide()
};
if(!A){A=YAHOO.widget.SimpleDialog.ICON_INFO
}if(!YAHOO.GDMA.dialog.infoDialog){YAHOO.GDMA.dialog.infoDialog=new YAHOO.widget.SimpleDialog("infoDialog",{fixedcenter:true,visible:false,draggable:false,close:false,modal:true,zindex:40000,text:"Click Ok?",constraintoviewport:true,buttons:[{text:"Ok",handler:B,isDefault:true}]});
YAHOO.GDMA.dialog.infoDialog.setHeader("Info");
YAHOO.GDMA.dialog.infoDialog.render("body")
}YAHOO.GDMA.dialog.infoDialog.setBody(C);
YAHOO.GDMA.dialog.infoDialog.setHeader(D);
YAHOO.GDMA.dialog.infoDialog.cfg.setProperty("icon",A);
YAHOO.GDMA.dialog.infoDialog.show()
};