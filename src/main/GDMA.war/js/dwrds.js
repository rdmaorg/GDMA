YAHOO.namespace("GDMA.dwrds");


/*
 * This code overrides the default connection handler so that it calls DWR
 */
YAHOO.util.DataSource.prototype.makeConnection = function(oRequest, oCallback, oCaller) {
    this.fireEvent("requestEvent", {request:oRequest,callback:oCallback,caller:oCaller});
    var oRawResponse = null;
    var tId = YAHOO.util.DataSource._nTransactionId++;
    // How to make the connection depends on the type of data
    switch(this.dataType) {
        // If the live data is a JavaScript Function
        // pass the request in as a parameter and
        // forward the return value to the handler
        case YAHOO.util.DataSource.TYPE_JSFUNCTION:
            YAHOO.GDMA.dwrds.oRequest = oRequest;
            scope = this;
            this.liveData(oRequest, function(oRawResponse) {
                YAHOO.GDMA.dwrds.oRawResponse = oRawResponse;
                scope.handleResponse(oRequest, oRawResponse, oCallback, oCaller, tId, scope);
            });
            break;
        // If the live data is over Connection Manager
        // set up the callback object and
        // pass the request in as a URL query and
        // forward the response to the handler
        case YAHOO.util.DataSource.TYPE_XHR:
            YAHOO.GDMA.dialog.showInfoDialog("Error!", "Datasource type TYPE_XHR has been disabled",
                    YAHOO.widget.SimpleDialog.ICON_BLOCK);
            break;
        // Simply forward the entire data object to the handler
        default:
            /* accounts for the following cases:
            YAHOO.util.DataSource.TYPE_UNKNOWN:
            YAHOO.util.DataSource.TYPE_JSARRAY:
            YAHOO.util.DataSource.TYPE_JSON:
            YAHOO.util.DataSource.TYPE_HTMLTABLE:
            YAHOO.util.DataSource.TYPE_XML:
            */
            oRawResponse = this.liveData;
            this.handleResponse(oRequest, oRawResponse, oCallback, oCaller, tId);
            break;
    }
    return tId;
};


/**
 * Overridable method parses JSON data into a response object.
 *
 * @method parseJSONData
 * @param oRequest {Object} Request object.
 * @param oFullResponse {Object} The full JSON from the live database.
 * @return {Object} Parsed response object with the following properties<br>
 *     - results (Array) Array of parsed data results<br>
 *     - error (Boolean) True if there was an error<br>
 *     - totalRecords (Number) Total number of records (if available)
 */
YAHOO.util.DataSource.prototype.parseJSONData = function(oRequest, oFullResponse) {
    var oParsedResponse = {results:[],meta:{}},
        schema          = this.responseSchema;

    if(YAHOO.lang.isObject(oFullResponse)) {        
        if(YAHOO.lang.isArray(schema.fields)) {
            var fields          = schema.fields,
                resultsList     = oFullResponse,
                results         = [],
                metaFields      = schema.metaFields || {},
                fieldParsers    = [],
                fieldPaths      = [],
                simpleFields    = [],
                bError          = false,
                i,len,j,v,key,parser,path;

            // Function to parse the schema's locator keys into walk paths
            var buildPath = function (needle) {
                var path = null, keys = [], i = 0;
                if (needle) {
                    // Strip the ["string keys"] and [1] array indexes
                    needle = needle.
                        replace(/\[(['"])(.*?)\1\]/g,
                        function (x,$1,$2) {keys[i]=$2;return '.@'+(i++);}).
                        replace(/\[(\d+)\]/g,
                        function (x,$1) {keys[i]=parseInt($1,10)|0;return '.@'+(i++);}).
                        replace(/^\./,''); // remove leading dot

                    // If the cleaned needle contains invalid characters, the
                    // path is invalid
                    if (!/[^\w\.\$@]/.test(needle)) {
                        path = needle.split('.');
                        for (i=path.length-1; i >= 0; --i) {
                            if (path[i].charAt(0) === '@') {
                                path[i] = keys[parseInt(path[i].substr(1),10)];
                            }
                        }
                    }
                }
                return path;
            };

            // build function to walk a path and return the pot of gold
            var walkPath = function (path, origin) {
                var v=origin,i=0,len=path.length;
                for (;i<len && v;++i) {
                    v = v[path[i]];
                }
                return v;
            };

            // Build the parser map and location paths
            for (i = fields.length - 1; i >= 0; --i) {
                key    = fields[i].key || fields[i];
                parser = fields[i].parser || fields[i].converter;
                path   = buildPath(key);

                if (parser) {
                    fieldParsers[fieldParsers.length] = {key:key,parser:parser};
                }

                if (path) {
                    if (path.length > 1) {
                        fieldPaths[fieldPaths.length] = {key:key,path:path};
                    } else {
                        //simpleFields[simpleFields.length] = key;
                        simpleFields[i] = key; //NEW
                    }
                } else {
                    YAHOO.log("Invalid key syntax: " + key,"warn",this.toString());
                }
            }

            // Parse the response
            // Step 1. Pull the resultsList from oFullResponse (default assumes
            // oFullResponse IS the resultsList)
            if (schema.resultsList) {
                path = buildPath(schema.resultsList);
                if (path) {
                    resultsList = walkPath(path, oFullResponse);
                    if (resultsList === undefined) {
                        bError = true;
                    }
                } else {
                    bError = true;
                }
            }
            if (!resultsList) {
                resultsList = [];
            }
            if (!YAHOO.lang.isArray(resultsList)) {
                resultsList = [resultsList];
            }

            if (!bError) {
                // Step 2. Process the results, flattening the records and/or
                // applying parsers if needed
                //if (fieldParsers.length || fieldPaths.length) {
                    var arrType = YAHOO.lang.isArray(resultsList[0]); //NEW
                    for (i = resultsList.length - 1; i >= 0; --i) {
                        var r = resultsList[i], rec = {};
                        for (j = simpleFields.length - 1; j >= 0; --j) {
                            //rec[simpleFields[j]] = r[simpleFields[j]];
                            rec[simpleFields[j]] = arrType ? r[j] : r[simpleFields[j]]; //NEW
                        }

                        for (j = fieldPaths.length - 1; j >= 0; --j) {
                            rec[fieldPaths[j].key] = walkPath(fieldPaths[j].path,r);
                        }

                        for (j = fieldParsers.length - 1; j >= 0; --j) {
                            var p = fieldParsers[j].key;
                            rec[p] = fieldParsers[j].parser(rec[p]);
                            if (rec[p] === undefined) {
                                rec[p] = null;
                            }
                        }
                        results[i] = rec;
                    }
                //}

                // Step 3. Pull meta fields from oFullResponse if identified
                if (schema.totalRecords && !metaFields.totalRecords) {
                    // for backward compatibility
                    metaFields.totalRecords = schema.totalRecords;
                }

                for (key in metaFields) {
                    if (YAHOO.lang.hasOwnProperty(metaFields,key)) {
                        path = buildPath(metaFields[key]);
                        if (path) {
                            v = walkPath(path, oFullResponse);
                            oParsedResponse.meta[key] = v;
                        }
                    }
                }

            } else {
                YAHOO.log("JSON data could not be parsed: " +
                        YAHOO.lang.dump(oFullResponse), "error", this.toString());

                oParsedResponse.error = true;
            }

            oParsedResponse.results = results;
        }
    }
    else {
        YAHOO.log("JSON data could not be parsed: " +
                YAHOO.lang.dump(oFullResponse), "error", this.toString());
        oParsedResponse.error = true;
    }

    return oParsedResponse;
};