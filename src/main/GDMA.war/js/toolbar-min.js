YAHOO.namespace("GDMA.toolbar");
YAHOO.GDMA.toolbar.buttons={};
YAHOO.GDMA.toolbar.currentMode="defaultMode";
YAHOO.GDMA.toolbar.changeState=function(B){if(!B||B==null||B==""){B="defaultMode"
}YAHOO.GDMA.toolbar.currentMode=B;
for(var C in YAHOO.GDMA.toolbar.buttons){var A=YAHOO.GDMA.toolbar.buttons[C];
if(A.config[B]=="show"){A.button.show()
}else{A.button.hide()
}}};
YAHOO.GDMA.toolbar.createToolbar=function(C){for(var B=0;
B<C.length;
B++){var A=C[B].name;
var D="btn"+A;
if(YAHOO.GDMA.toolbar.buttons[D]){YAHOO.GDMA.toolbar.buttons[D].config=C[B];
YAHOO.GDMA.toolbar.updateToolBarButton(D,C[B])
}else{YAHOO.GDMA.toolbar.buttons[D]={};
YAHOO.GDMA.toolbar.buttons[D].name=A;
YAHOO.GDMA.toolbar.buttons[D].config=C[B];
YAHOO.GDMA.toolbar.buttons[D].button=YAHOO.GDMA.toolbar.createToolBarButton(A,D,"divToolbar",C[B].fn,C[B].tooltip,C[B].defaultMode)
}}};
YAHOO.GDMA.toolbar.initTooltip=function(){YAHOO.GDMA.toolbar.tooltip=new YAHOO.widget.Tooltip("ttToolbar",{context:[]});
YAHOO.GDMA.toolbar.tooltipMessages={};
YAHOO.GDMA.toolbar.tooltip.contextTriggerEvent.subscribe(function(C,A){var B=A[0];
YAHOO.log("tt context id "+B.id,"warn","YAHOO.GDMA.toolbar");
YAHOO.log("Setting tt text to "+YAHOO.GDMA.toolbar.tooltipMessages[B.id],"warn","YAHOO.GDMA.toolbar");
YAHOO.GDMA.toolbar.tooltip.cfg.setProperty("text",YAHOO.GDMA.toolbar.tooltipMessages[B.id])
})
};
YAHOO.GDMA.toolbar.addToToolTipContext=function(C){var B=YAHOO.GDMA.toolbar.tooltip.cfg.getProperty("context");
for(var A=0;
A<B.length;
A++){if(B[A]==C){return 
}}B.push(C)
};
YAHOO.GDMA.toolbar.createToolBarButton=function(C,G,B,E,F,A){YAHOO.log("Creating button "+G+" buttons","warn","YAHOO.GDMA.toolbar");
var D=new YAHOO.widget.Button({id:G,type:"push",label:C,container:B,onclick:{fn:E}});
if(A!="show"){D.hide()
}if(!YAHOO.GDMA.toolbar.tooltip){YAHOO.GDMA.toolbar.initTooltip()
}YAHOO.GDMA.toolbar.addToToolTipContext(G);
YAHOO.GDMA.toolbar.tooltipMessages[G]=F;
return D
};
YAHOO.GDMA.toolbar.updateToolBarButton=function(C,A){var B=YAHOO.GDMA.toolbar.buttons[C];
B.config=A;
B.button.set("onclick",{fn:A.fn});
B.button.set("name",A.name);
if(B.config[YAHOO.GDMA.toolbar.currentMode]=="show"){B.button.show()
}else{B.button.hide()
}YAHOO.GDMA.toolbar.tooltipMessages[C]=A.tooltip;
return B
};
if(YAHOO.widget.Button){if(!YAHOO.widget.Button.hide){YAHOO.widget.Button.prototype.hide=function(){this.setStyle("display","none")
}
}else{alert("ERROR: Trying to create the prototype 'hide' in the unloaded library 'YAHOO.widget.Button' but it already exists!")
}if(!YAHOO.widget.Button.show){YAHOO.widget.Button.prototype.show=function(){try{this.setStyle("display","-moz-inline-box")
}catch(A){}this.setStyle("display","inline-block")
}
}else{alert("ERROR: Trying to create the prototype 'show' in the unloaded library 'YAHOO.widget.Button' but it already exists!")
}}else{alert("ERROR: Trying to create a prototype in the unloaded library 'YAHOO.widget.Button'")
};