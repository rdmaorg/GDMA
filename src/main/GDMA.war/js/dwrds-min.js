YAHOO.namespace("GDMA.dwrds");
YAHOO.util.DataSource.prototype.makeConnection=function(D,A,C){this.fireEvent("requestEvent",{request:D,callback:A,caller:C});
var B=null;
var E=YAHOO.util.DataSource._nTransactionId++;
switch(this.dataType){case YAHOO.util.DataSource.TYPE_JSFUNCTION:YAHOO.GDMA.dwrds.oRequest=D;
scope=this;
this.liveData(D,function(F){YAHOO.GDMA.dwrds.oRawResponse=F;
scope.handleResponse(D,F,A,C,E,scope)
});
break;
case YAHOO.util.DataSource.TYPE_XHR:YAHOO.GDMA.dialog.showInfoDialog("Error!","Datasource type TYPE_XHR has been disabled",YAHOO.widget.SimpleDialog.ICON_BLOCK);
break;
default:B=this.liveData;
this.handleResponse(D,B,A,C,E);
break
}return E
};
YAHOO.util.DataSource.prototype.parseJSONData=function(R,W){var V={results:[],meta:{}},O=this.responseSchema;
if(YAHOO.lang.isObject(W)){if(YAHOO.lang.isArray(O.fields)){var P=O.fields,C=W,Q=[],I=O.metaFields||{},E=[],H=[],G=[],X=false,T,U,S,J,Y,B,N;
var A=function(c){var b=null,a=[],Z=0;
if(c){c=c.replace(/\[(['"])(.*?)\1\]/g,function(e,d,f){a[Z]=f;
return".@"+(Z++)
}).replace(/\[(\d+)\]/g,function(e,d){a[Z]=parseInt(d,10)|0;
return".@"+(Z++)
}).replace(/^\./,"");
if(!/[^\w\.\$@]/.test(c)){b=c.split(".");
for(Z=b.length-1;
Z>=0;
--Z){if(b[Z].charAt(0)==="@"){b[Z]=a[parseInt(b[Z].substr(1),10)]
}}}}return b
};
var D=function(d,b){var a=b,c=0,Z=d.length;
for(;
c<Z&&a;
++c){a=a[d[c]]
}return a
};
for(T=P.length-1;
T>=0;
--T){Y=P[T].key||P[T];
B=P[T].parser||P[T].converter;
N=A(Y);
if(B){E[E.length]={key:Y,parser:B}
}if(N){if(N.length>1){H[H.length]={key:Y,path:N}
}else{G[T]=Y
}}else{YAHOO.log("Invalid key syntax: "+Y,"warn",this.toString())
}}if(O.resultsList){N=A(O.resultsList);
if(N){C=D(N,W);
if(C===undefined){X=true
}}else{X=true
}}if(!C){C=[]
}if(!YAHOO.lang.isArray(C)){C=[C]
}if(!X){var K=YAHOO.lang.isArray(C[0]);
for(T=C.length-1;
T>=0;
--T){var L=C[T],F={};
for(S=G.length-1;
S>=0;
--S){F[G[S]]=K?L[S]:L[G[S]]
}for(S=H.length-1;
S>=0;
--S){F[H[S].key]=D(H[S].path,L)
}for(S=E.length-1;
S>=0;
--S){var M=E[S].key;
F[M]=E[S].parser(F[M]);
if(F[M]===undefined){F[M]=null
}}Q[T]=F
}if(O.totalRecords&&!I.totalRecords){I.totalRecords=O.totalRecords
}for(Y in I){if(YAHOO.lang.hasOwnProperty(I,Y)){N=A(I[Y]);
if(N){J=D(N,W);
V.meta[Y]=J
}}}}else{YAHOO.log("JSON data could not be parsed: "+YAHOO.lang.dump(W),"error",this.toString());
V.error=true
}V.results=Q
}}else{YAHOO.log("JSON data could not be parsed: "+YAHOO.lang.dump(W),"error",this.toString());
V.error=true
}return V
};