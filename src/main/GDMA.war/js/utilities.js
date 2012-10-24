YAHOO.namespace("GDMA.utilities");
YAHOO.namespace("GDMA.utilities.sqlType");

YAHOO.GDMA.utilities.createElement = function(type, id, parent, inputType){
    var el = document.createElement(type);
    if(id){
        el.id = id;
    }
    if(type == "input" && inputType){
        el.type = inputType;
    }
    if(parent){
        parent.appendChild(el);
    }
    
    
    
    
    
    return el;
}

YAHOO.GDMA.utilities.sqlType.BIT         =  -7;
YAHOO.GDMA.utilities.sqlType.TINYINT     =  -6;
YAHOO.GDMA.utilities.sqlType.SMALLINT    =   5;
YAHOO.GDMA.utilities.sqlType.INTEGER     =   4;
YAHOO.GDMA.utilities.sqlType.BIGINT      =  -5;
YAHOO.GDMA.utilities.sqlType.FLOAT       =   6;
YAHOO.GDMA.utilities.sqlType.REAL        =   7;
YAHOO.GDMA.utilities.sqlType.DOUBLE      =   8;
YAHOO.GDMA.utilities.sqlType.NUMERIC     =   2;
YAHOO.GDMA.utilities.sqlType.DECIMAL     =   3;
YAHOO.GDMA.utilities.sqlType.CHAR        =   1;
YAHOO.GDMA.utilities.sqlType.VARCHAR     =  12;
YAHOO.GDMA.utilities.sqlType.LONGVARCHAR     =  -1;
YAHOO.GDMA.utilities.sqlType.DATE        =  91;
YAHOO.GDMA.utilities.sqlType.TIME        =  92;
YAHOO.GDMA.utilities.sqlType.TIMESTAMP   =  93;
YAHOO.GDMA.utilities.sqlType.BINARY      =  -2;
YAHOO.GDMA.utilities.sqlType.VARBINARY   =  -3;
YAHOO.GDMA.utilities.sqlType.LONGVARBINARY   =  -4;
YAHOO.GDMA.utilities.sqlType.NULL        =   0;
YAHOO.GDMA.utilities.sqlType.OTHER       = 1111;
YAHOO.GDMA.utilities.sqlType.JAVA_OBJECT = 2000;
YAHOO.GDMA.utilities.sqlType.DISTINCT    = 2001;
YAHOO.GDMA.utilities.sqlType.STRUCT      = 2002;
YAHOO.GDMA.utilities.sqlType.ARRAY       = 2003;
YAHOO.GDMA.utilities.sqlType.BLOB        = 2004;
YAHOO.GDMA.utilities.sqlType.CLOB        = 2005;
YAHOO.GDMA.utilities.sqlType.REF         = 2006;
YAHOO.GDMA.utilities.sqlType.DATALINK    = 70;
YAHOO.GDMA.utilities.sqlType.BOOLEAN     = 16;

YAHOO.GDMA.utilities.sqlType.TEXT     = 16;


YAHOO.GDMA.utilities.isTypeNumber = function(columnType){
    if( columnType == YAHOO.GDMA.utilities.sqlType.BIT ||
        columnType == YAHOO.GDMA.utilities.sqlType.TINYINT ||
        columnType == YAHOO.GDMA.utilities.sqlType.SMALLINT ||
        columnType == YAHOO.GDMA.utilities.sqlType.INTEGER ||
        columnType == YAHOO.GDMA.utilities.sqlType.BIGINT ||
        columnType == YAHOO.GDMA.utilities.sqlType.FLOAT ||
        columnType == YAHOO.GDMA.utilities.sqlType.REAL ||
        columnType == YAHOO.GDMA.utilities.sqlType.DOUBLE ||
        columnType == YAHOO.GDMA.utilities.sqlType.NUMERIC ||
        columnType == YAHOO.GDMA.utilities.sqlType.DECIMAL){
        return true;
    }else{
        return false;
    }
};

YAHOO.GDMA.utilities.isTypeDate = function(columnType){
    if( columnType == YAHOO.GDMA.utilities.sqlType.DATE ||
        columnType == YAHOO.GDMA.utilities.sqlType.TIMESTAMP){
        return true;
    }else{
        return false;
    }
};

YAHOO.GDMA.utilities.isTypeTime = function(columnType){
    if( columnType == YAHOO.GDMA.utilities.sqlType.TIME){
        return true;
    }else{
        return false;
    }
};

YAHOO.GDMA.utilities.isTypeString = function(columnType){
    if( columnType == YAHOO.GDMA.utilities.sqlType.CHAR ||
        columnType == YAHOO.GDMA.utilities.sqlType.VARCHAR ||
        columnType == YAHOO.GDMA.utilities.sqlType.LONGVARCHAR){
        return true;
    }else{
        return false;
    }
};

/**
 * Converts data to type Date.
 *
 * @method DataSource.parseDate
 * @param oData {Date | String | Number} Data to convert.
 * @return {Date} A Date instance.
 * @static
 */
YAHOO.GDMA.utilities.parseDate = function(oData) {
    
    // Validate
    if(oData instanceof Date) {
        
    	var mm = oData.getMonth()+1;
        var dd = oData.getDate();
        var yyyy = oData.getFullYear();
                
        if (mm < 10) mm = "0" + mm;
        if (dd < 10) dd = "0" + dd
        
        //return oData;
        //return yyyy + "-" + mm + "-" + dd;
       //return yyyy + "-" + mm + "-" + dd;
         return yyyy + "-" + mm + "-" + dd;
    }
    else {
        YAHOO.log("Could not convert data " + YAHOO.lang.dump(oData) + " to type Date", "warn", this.toString());
        return null;
    }
};

/**
 * Converts data to type Date.
 *
 * @method DataSource.parseDate
 * @param oData {Date | String | Number} Data to convert.
 * @return {Date} A Date instance.
 * @static
 */
YAHOO.GDMA.utilities.parseTime = function(oData) {
    
    // Validate
    if(oData instanceof Date) {
//    	return oData;
        var hh = oData.getHours()+1;
        var mm = oData.getMinutes();
        var ss = oData.getSeconds();
        
        if (hh < 10) hh = "0" + hh;
        if (hh == 24) hh = "00";
        if (mm < 10) mm = "0" + mm
        if (ss < 10) ss = "0" + ss

        return hh + ":" + mm + ":" + ss;
    }
    else {
        YAHOO.log("Could not convert data " + YAHOO.lang.dump(oData) + " to type Date", "warn", this.toString());
        return null;
    }
};

YAHOO.GDMA.utilities.formatDate = function(elCell, oRecord, oColumn, oData){
    if(oData && oData instanceof Date){
        var mm = oData.getMonth()+1;
        var dd = oData.getDate();
        var yyyy = oData.getFullYear(); 
        
        if (mm < 10) mm = "0" + mm;
        if (dd < 10) dd = "0" + dd;
        
        elCell.innerHTML = yyyy + "-" + mm + "-" + dd;
    }else {
        //elCell.innerHTML = "";
    	elCell.innerHTML = oData;
    }
};

YAHOO.GDMA.utilities.formatTime = function(elCell, oRecord, oColumn, oData){
    if(oData && oData instanceof Date){
        var hh = oData.getHours()+1;
        var mm = oData.getMinutes();
        var ss = oData.getSeconds();
        if (hh < 10) hh = "0" + hh;
        if (hh == 24) hh = "00";
        if (mm < 10) mm = "0" + mm
        if (ss < 10) ss = "0" + ss

        elCell.innerHTML = hh + ":" + mm + ":" + ss;
    }else {
        elCell.innerHTML = oData;
    }
};

//utility stuff- need to move out of here
YAHOO.GDMA.utilities.getParser = function (columnType){
    if(YAHOO.GDMA.utilities.isTypeDate(columnType)){
        //return YAHOO.GDMA.utilities.parseDatee;
        return YAHOO.GDMA.utilities.parseDate;
    }else if(YAHOO.GDMA.utilities.isTypeTime(columnType)){
        return YAHOO.GDMA.utilities.parseTime;
    }else if(YAHOO.GDMA.utilities.isTypeNumber(columnType)){
        return YAHOO.util.DataSource.parseString;
    }else if(YAHOO.GDMA.utilities.isTypeString(columnType)){
        return YAHOO.util.DataSource.parseString;
    }else{
        return YAHOO.util.DataSource.parseString;
    }
};


YAHOO.GDMA.utilities.formatNumber =  function(el, oRecord, oColumn, oData) {
    if(YAHOO.lang.isNumber(oData)) {
        el.innerHTML = oData ;
    }
    else {
        el.innerHTML = YAHOO.lang.isValue(oData) ? oData : "";
    }
};

YAHOO.GDMA.utilities.getFormatter = function (columnType){
    if(YAHOO.GDMA.utilities.isTypeDate(columnType)){
        return YAHOO.GDMA.utilities.formatDate;
    }else if(YAHOO.GDMA.utilities.isTypeTime(columnType)){
        return YAHOO.GDMA.utilities.formatTime;
    }else if(YAHOO.GDMA.utilities.isTypeNumber(columnType)){
        return YAHOO.GDMA.utilities.formatNumber;
    }else if(YAHOO.GDMA.utilities.isTypeString(columnType)){
        return YAHOO.widget.DataTable.formatText;
    }else{
        return YAHOO.widget.DataTable.formatText;
    }
};


YAHOO.GDMA.utilities.editDate = function(oEditor, oSelf) {
    var elCell = oEditor.cell;
    var oRecord = oEditor.record;
    var oColumn = oEditor.column;
    var elContainer = oEditor.container;
    var value = oEditor.value;
    
    // Set a default
    if(!(value instanceof Date)) {
        value = oEditor.defaultValue || new Date();
    }

    var navConfig = {
        strings : {
            month: "Choose Month",
            year: "Enter Year",
            submit: "OK",
            cancel: "Cancel",
            invalidYear: "Please enter a valid year"
        },
        monthFormat: YAHOO.widget.Calendar.SHORT,
        initialFocus: "year"
    };

    var selectedValue = (value.getMonth()+1)+"/"+value.getDate()+"/"+value.getFullYear();
    var calContainer = elContainer.appendChild(document.createElement("div"));
    var calPrefix = oColumn.getColEl();
    calContainer.id = calPrefix + "-dateContainer";
    var calendar =
            new YAHOO.widget.Calendar(calPrefix + "-date",
            calContainer.id,
            {
                    selected:selectedValue, 
                    pagedate:value,
                    navigator: navConfig
            });
    calendar.render();
    calContainer.style.cssFloat = "none";

    if(YAHOO.env.ua.ie) {
        var calFloatClearer = elContainer.appendChild(document.createElement("br"));
        calFloatClearer.style.clear = "both";
    }

    calendar.selectEvent.subscribe(function(type, args, obj) {
        oSelf._oCellEditor.value = new Date(args[0][0][0], args[0][0][1]-1, args[0][0][2]);
        oSelf.fireEvent("editorUpdateEvent",{editor:oSelf._oCellEditor});
    });
};


YAHOO.GDMA.utilities.getEditor = function (columnType){
    if(YAHOO.GDMA.utilities.isTypeDate(columnType)){
        return YAHOO.GDMA.utilities.editDate ;
    }else if(YAHOO.GDMA.utilities.isTypeTime(columnType)){
        return "textbox";
    }else if(YAHOO.GDMA.utilities.isTypeNumber(columnType)){
        return "textbox";
    }else if(YAHOO.GDMA.utilities.isTypeString(columnType)){
        return "textbox";
    }else{
        // todo unknown type
        return "textbox";
    }
};


YAHOO.GDMA.utilities.validateNumber = function(oData) {
    if(YAHOO.lang.trim(oData) == ""){
        return oData;
    }
    var number = oData * 1;
    if(YAHOO.lang.isNumber(number)) {
        return number;
    }
    else {
        YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "Value entered is not numeric");
        return null;
    }
};

YAHOO.GDMA.utilities.validateString = function(oData, oColumnIndex) {
    var string = "" + oData;
    var columnIndex;
    //if the editor(update) cell is defined, i.e. an update is occurring
    //then get the columnIndex from the cell editor pop-up object
    if(typeof(this._oCellEditor)!="undefined")
    {
    	columnIndex = this._oCellEditor.column._nTreeIndex-1
    }
    //otherwise set the columnIndex value to the value passed in the parameters 
    else 
    {
    	columnIndex = oColumnIndex;	
    }
    
    if(YAHOO.lang.isString(string)) {
        
    	if(null != columnIndex)
    	{	
    		if (null != YAHOO.GDMA.datagrid.currentDataDescription)
    		{
    			if (YAHOO.GDMA.datagrid.currentDataDescription.tables.length >= 1 && 
    			    null != YAHOO.GDMA.datagrid.currentDataDescription.tables[0].name)
    	    	{
    				if (YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns.length >= 1 )
    				{    				
    					if(oData.length <= YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns[columnIndex].columnSize)
    			        {
    			        	return string;
    			        }
    			        else
    			        {
    			        	YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "The maximum length allowed for this field is " + YAHOO.GDMA.datagrid.currentDataDescription.tables[0].columns[columnIndex].columnSize);
    			            return null;
    			        }
    				}
    	    	}    
    		}
    	}
    }
    else {
        YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "Value entered is not text");
        return null;
    }
    
};










YAHOO.GDMA.utilities.validateDate = function(oData) {
    YAHOO.log("Not validating : type " + (typeof oData) + ", value " + oData, "warn", this.toString());
    return oData;
};

YAHOO.GDMA.utilities.validateTime = function(oData) {
	
    var regex= /^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9]):([0-5]?[0-9])$/;
    var time = "" + oData;
    
    if (null == time || time == "" || time.match(regex)) 
    {
        return oData;
    }

    YAHOO.GDMA.dialog.showInfoDialog("Validation Error", "Time must be in the form hh:mm:ss (24 hour clock)");
    return null;
};

YAHOO.GDMA.utilities.getValidator = function (columnType){
    if(YAHOO.GDMA.utilities.isTypeDate(columnType)){
        return YAHOO.GDMA.utilities.validateDate;
    }else if(YAHOO.GDMA.utilities.isTypeTime(columnType)){
        return YAHOO.GDMA.utilities.validateTime;
    }else if(YAHOO.GDMA.utilities.isTypeNumber(columnType)){
        return YAHOO.GDMA.utilities.validateNumber;
    }else if(YAHOO.GDMA.utilities.isTypeString(columnType)){
        return YAHOO.GDMA.utilities.validateString;
    }else{
        // todo unknown type
        return YAHOO.GDMA.utilities.validateString;
    }
};

YAHOO.GDMA.utilities.rownumFormatter = function(elCell, oRecord, oColumn, oData) {
    elCell.innerHTML = (this.getRecordSet().getRecordIndex(oRecord) + 1);
};

YAHOO.GDMA.utilities.passwordFormatter = function(elCell, oRecord, oColumn, oData) {
    elCell.innerHTML = "*****";
};

YAHOO.GDMA.utilities.createCheckAllHeader = function(label){
    var header = "<div style=\"float:left\">";
    header += label;
    header += "</div><div style=\"float:right\"> <input type=\"checkbox\" class=\"chkCheckAll\" /> </div>";
    
    return header;
};


YAHOO.GDMA.utilities.populateDropDown = function(elDropdown, data, keyValue, keyText, currentValue, addPleaseSelect){

    var elOption = document.createElement("option");

    if(addPleaseSelect){
        // create the first option i.e. please select
        elOption.value = -1;
        elOption.text = "Please Select";
        elOption.selected = (currentValue == -1);
        elDropdown.options[0] = elOption;
    }

    // add the rest of the options
    for ( var i = 0; i < data.length; i++) {
        elOption = document.createElement("option");
        elOption.value = data[i][keyValue];
        elOption.text = data[i][keyText];
        elDropdown.options[i+1] = elOption;
        if(currentValue == data[i][keyValue]){
            elOption.selected = (currentValue == data[i][keyValue]);    
            elDropdown.selectedIndex = i + 1;
        }

    }
};

YAHOO.GDMA.utilities.populateDropDown2 = function(elDropdown, dataID, dataName, keyValue, keyText, currentValue, addPleaseSelect){

    var elOption = document.createElement("option");

    if(addPleaseSelect){
        // create the first option i.e. please select
        elOption.value = -1;
        elOption.text = "Please Select";
        elOption.selected = (currentValue == -1);
        elDropdown.options[0] = elOption;
    }

    // add the rest of the options
    for ( var i = 0; i < dataID.length; i++) {
        elOption = document.createElement("option");
        elOption.value = dataID[i];
        elOption.text = dataName[i];
        elDropdown.options[i+1] = elOption;
        if(currentValue == dataID[i]){
            elOption.selected = (currentValue == dataID[i]);    
            elDropdown.selectedIndex = i + 1;
        }

    }
};

YAHOO.GDMA.utilities.isDataTypeString = function(oDataType) {
	switch(oDataType) {
	case (1): case (12): case (-1): case (2005):
		return true;
	default:
		return false;
	}
};


