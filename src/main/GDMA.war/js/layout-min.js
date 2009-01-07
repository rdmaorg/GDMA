YAHOO.namespace("GDMA");
YAHOO.GDMA.init=function(){var A=[{position:"top",height:80,resize:false,body:"top1",gutter:"0px 0px 0px 0px"},{position:"bottom",height:30,body:"footer1",gutter:"0px 0px 0px 0px"}];
if(YAHOO.util.Dom.get("divServerBrowser")){A.push({position:"left",header:"Server Browser",width:200,resize:true,body:"left1",gutter:"2px 4px 2px 0px",collapse:true,collapseSize:15,scroll:true,animate:true});
A.push({position:"center",header:"Data Grid",body:"center1",scroll:true,height:"200px",gutter:"2px 0px 2px 0px"})
}else{A.push({position:"center",header:"Admin - Servers",body:"center1",scroll:true,height:"200px",gutter:"20px 20px 20px 20px"})
}YAHOO.GDMA.layout=new YAHOO.widget.Layout({units:A});
YAHOO.GDMA.layout.render();
YAHOO.GDMA.layout.on("render",function(){if(YAHOO.GDMA.layout.getUnitByPosition("left")){YAHOO.GDMA.layout.getUnitByPosition("left").on("close",function(){YAHOO.GDMA.closeLeft()
})
}});
YAHOO.util.Event.on("tLeft","click",function(B){YAHOO.util.Event.stopEvent(B);
YAHOO.GDMA.layout.getUnitByPosition("left").toggle()
});
YAHOO.GDMA.closeLeft=function(){var B=document.createElement("a");
B.href="#";
B.innerHTML="Add Left Unit";
YAHOO.util.Dom.get("closeLeft").parentNode.appendChild(B);
YAHOO.util.Dom.setStyle("tLeft","display","none");
YAHOO.util.Dom.setStyle("closeLeft","display","none");
YAHOO.util.Event.on(B,"click",function(C){YAHOO.util.Event.stopEvent(C);
YAHOO.util.Dom.setStyle("tLeft","display","inline");
YAHOO.util.Dom.setStyle("closeLeft","display","inline");
B.parentNode.removeChild(B);
YAHOO.GDMA.layout.addUnit(YAHOO.GDMA.layout.get("units")[3]);
YAHOO.GDMA.layout.getUnitByPosition("left").on("close",function(){YAHOO.GDMA.closeLeft()
})
})
}
};
YAHOO.util.Event.onDOMReady(YAHOO.GDMA.init);