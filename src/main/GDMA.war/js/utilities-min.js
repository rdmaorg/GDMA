YAHOO.namespace("GDMA.utilities");
YAHOO.namespace("GDMA.utilities.sqlType");
YAHOO.GDMA.utilities.createElement=function(D,E,C,B){var A=document.createElement(D);
if(E){A.id=E
}if(D=="input"&&B){A.type=B
}if(C){C.appendChild(A)
}return A
};
YAHOO.GDMA.utilities.sqlType.BIT=-7;
YAHOO.GDMA.utilities.sqlType.TINYINT=-6;
YAHOO.GDMA.utilities.sqlType.SMALLINT=5;
YAHOO.GDMA.utilities.sqlType.INTEGER=4;
YAHOO.GDMA.utilities.sqlType.BIGINT=-5;
YAHOO.GDMA.utilities.sqlType.FLOAT=6;
YAHOO.GDMA.utilities.sqlType.REAL=7;
YAHOO.GDMA.utilities.sqlType.DOUBLE=8;
YAHOO.GDMA.utilities.sqlType.NUMERIC=2;
YAHOO.GDMA.utilities.sqlType.DECIMAL=3;
YAHOO.GDMA.utilities.sqlType.CHAR=1;
YAHOO.GDMA.utilities.sqlType.VARCHAR=12;
YAHOO.GDMA.utilities.sqlType.LONGVARCHAR=-1;
YAHOO.GDMA.utilities.sqlType.DATE=91;
YAHOO.GDMA.utilities.sqlType.TIME=92;
YAHOO.GDMA.utilities.sqlType.TIMESTAMP=93;
YAHOO.GDMA.utilities.sqlType.BINARY=-2;
YAHOO.GDMA.utilities.sqlType.VARBINARY=-3;
YAHOO.GDMA.utilities.sqlType.LONGVARBINARY=-4;
YAHOO.GDMA.utilities.sqlType.NULL=0;
YAHOO.GDMA.utilities.sqlType.OTHER=1111;
YAHOO.GDMA.utilities.sqlType.JAVA_OBJECT=2000;
YAHOO.GDMA.utilities.sqlType.DISTINCT=2001;
YAHOO.GDMA.utilities.sqlType.STRUCT=2002;
YAHOO.GDMA.utilities.sqlType.ARRAY=2003;
YAHOO.GDMA.utilities.sqlType.BLOB=2004;
YAHOO.GDMA.utilities.sqlType.CLOB=2005;
YAHOO.GDMA.utilities.sqlType.REF=2006;
YAHOO.GDMA.utilities.sqlType.DATALINK=70;
YAHOO.GDMA.utilities.sqlType.BOOLEAN=16;
YAHOO.GDMA.utilities.isTypeNumber=function(A){if(A==YAHOO.GDMA.utilities.sqlType.BIT||A==YAHOO.GDMA.utilities.sqlType.TINYINT||A==YAHOO.GDMA.utilities.sqlType.SMALLINT||A==YAHOO.GDMA.utilities.sqlType.INTEGER||A==YAHOO.GDMA.utilities.sqlType.BIGINT||A==YAHOO.GDMA.utilities.sqlType.FLOAT||A==YAHOO.GDMA.utilities.sqlType.REAL||A==YAHOO.GDMA.utilities.sqlType.DOUBLE||A==YAHOO.GDMA.utilities.sqlType.NUMERIC||A==YAHOO.GDMA.utilities.sqlType.DECIMAL){return true
}else{return false
}};
YAHOO.GDMA.utilities.isTypeDate=function(A){if(A==YAHOO.GDMA.utilities.sqlType.DATE||A==YAHOO.GDMA.utilities.sqlType.TIME||A==YAHOO.GDMA.utilities.sqlType.TIMESTAMP){return true
}else{return false
}};
YAHOO.GDMA.utilities.isTypeString=function(A){if(A==YAHOO.GDMA.utilities.sqlType.CHAR||A==YAHOO.GDMA.utilities.sqlType.VARCHAR||A==YAHOO.GDMA.utilities.sqlType.LONGVARCHAR){return true
}else{return false
}};
YAHOO.GDMA.utilities.parseDate=function(A){return Date.parse(A)
};
YAHOO.GDMA.utilities.formatDate=function(C,B,D,G){if(G&&G instanceof Date){var E=G.getMonth()+1;
var A=G.getDate();
var F=G.getFullYear();
C.innerHTML=F+"-"+E+"-"+A
}else{C.innerHTML=""
}};
YAHOO.GDMA.utilities.getParser=function(A){if(YAHOO.GDMA.utilities.isTypeDate(A)){return YAHOO.util.DataSource.parseDate
}else{if(YAHOO.GDMA.utilities.isTypeNumber(A)){return YAHOO.util.DataSource.parseString
}else{if(YAHOO.GDMA.utilities.isTypeString(A)){return YAHOO.util.DataSource.parseString
}else{return YAHOO.util.DataSource.parseString
}}}};
YAHOO.GDMA.utilities.formatNumber=function(A,B,C,D){if(YAHOO.lang.isNumber(D)){A.innerHTML=D
}else{A.innerHTML=YAHOO.lang.isValue(D)?D:""
}};
YAHOO.GDMA.utilities.getFormatter=function(A){if(YAHOO.GDMA.utilities.isTypeDate(A)){return YAHOO.GDMA.utilities.formatDate
}else{if(YAHOO.GDMA.utilities.isTypeNumber(A)){return YAHOO.GDMA.utilities.formatNumber
}else{if(YAHOO.GDMA.utilities.isTypeString(A)){return YAHOO.widget.DataTable.formatText
}else{return YAHOO.widget.DataTable.formatText
}}}};
YAHOO.GDMA.utilities.editDate=function(I,F){var J=I.cell;
var M=I.record;
var E=I.column;
var A=I.container;
var K=I.value;
if(!(K instanceof Date)){K=I.defaultValue||new Date()
}var C={strings:{month:"Choose Month",year:"Enter Year",submit:"OK",cancel:"Cancel",invalidYear:"Please enter a valid year"},monthFormat:YAHOO.widget.Calendar.SHORT,initialFocus:"year"};
var D=(K.getMonth()+1)+"/"+K.getDate()+"/"+K.getFullYear();
var L=A.appendChild(document.createElement("div"));
var H=E.getColEl();
L.id=H+"-dateContainer";
var B=new YAHOO.widget.Calendar(H+"-date",L.id,{selected:D,pagedate:K,navigator:C});
B.render();
L.style.cssFloat="none";
if(YAHOO.env.ua.ie){var G=A.appendChild(document.createElement("br"));
G.style.clear="both"
}B.selectEvent.subscribe(function(O,N,P){F._oCellEditor.value=new Date(N[0][0][0],N[0][0][1]-1,N[0][0][2]);
F.fireEvent("editorUpdateEvent",{editor:F._oCellEditor})
})
};
YAHOO.GDMA.utilities.getEditor=function(A){if(YAHOO.GDMA.utilities.isTypeDate(A)){return YAHOO.GDMA.utilities.editDate
}else{if(YAHOO.GDMA.utilities.isTypeNumber(A)){return"textbox"
}else{if(YAHOO.GDMA.utilities.isTypeString(A)){return"textbox"
}else{return"textbox"
}}}};
YAHOO.GDMA.utilities.validateNumber=function(B){if(YAHOO.lang.trim(B)==""){return B
}var A=B*1;
if(YAHOO.lang.isNumber(A)){return A
}else{YAHOO.GDMA.dialog.showInfoDialog("Validation Error","Value entered is not numeric");
return null
}};
YAHOO.GDMA.utilities.validateString=function(B){var A=""+B;
if(YAHOO.lang.isString(A)){return A
}else{YAHOO.GDMA.dialog.showInfoDialog("Validation Error","Value entered is not text");
return null
}};
YAHOO.GDMA.utilities.validateDate=function(A){YAHOO.log("Not validating : type "+(typeof A)+", value "+A,"warn",this.toString());
return A
};
YAHOO.GDMA.utilities.getValidator=function(A){if(YAHOO.GDMA.utilities.isTypeDate(A)){return YAHOO.GDMA.utilities.validateDate
}else{if(YAHOO.GDMA.utilities.isTypeNumber(A)){return YAHOO.GDMA.utilities.validateNumber
}else{if(YAHOO.GDMA.utilities.isTypeString(A)){return YAHOO.GDMA.utilities.validateString
}else{return YAHOO.GDMA.utilities.validateString
}}}};
YAHOO.GDMA.utilities.rownumFormatter=function(B,A,C,D){B.innerHTML=(this.getRecordSet().getRecordIndex(A)+1)
};
YAHOO.GDMA.utilities.passwordFormatter=function(B,A,C,D){B.innerHTML="*****"
};
YAHOO.GDMA.utilities.createCheckAllHeader=function(A){var B='<div style="float:left">';
B+=A;
B+='</div><div style="float:right"> <input type="checkbox" class="chkCheckAll" /> </div>';
return B
};
YAHOO.GDMA.utilities.populateDropDown=function(H,F,G,A,D,B){var E=document.createElement("option");
if(B){E.value=-1;
E.text="Please Select";
E.selected=(D==-1);
H.options[0]=E
}for(var C=0;
C<F.length;
C++){E=document.createElement("option");
E.value=F[C][G];
E.text=F[C][A];
H.options[C+1]=E;
if(D==F[C][G]){E.selected=(D==F[C][G]);
H.selectedIndex=C+1
}}};