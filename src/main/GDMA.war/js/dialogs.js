YAHOO.namespace("GDMA.dialog");

YAHOO.util.Event.onDOMReady( function(){
    YAHOO.GDMA.dialog.loading = new YAHOO.widget.Panel("loading", {
        width: "220px",
        height: "32px",
        fixedcenter: true,
        close: false,
        draggable: false,
        modal: true,
        visible: false
    });

    //YAHOO.GDMA.dialog.loading.setHeader("Loading, please wait...");
    YAHOO.GDMA.dialog.loading.setBody('Loading<br/><img src="' + GDMA_CONTEXT_ROOT + 'images/loading_new.gif" />');
    YAHOO.GDMA.dialog.loading.render("body");
});

YAHOO.GDMA.dialog.showYesNoDialog = function(yesCallback, header, text) {
	YAHOO.GDMA.dialog.loading.hide();
    var handleNo = function() {
        this.hide();
    };

    var handleYes = function() {
        this.hide();
        yesCallback();
    };

    var yesnoButtons = [ {
        text :"Yes",
        handler :handleYes,
        isDefault :false
    }, {
        text :"No",
        handler :handleNo,
        isDefault :true
    } ];

    // Instantiate the Dialog
    if (!YAHOO.GDMA.dialog.yesNoDialog) {
        YAHOO.GDMA.dialog.yesNoDialog = new YAHOO.widget.SimpleDialog("yesNoDialog", {
            width :"400px",
            fixedcenter :true,
            visible :false,
            draggable :false,
            close :false,
            modal :true,
            zindex :40000,
            text :"Do you want to continue?",
            icon :YAHOO.widget.SimpleDialog.ICON_HELP,
            constraintoviewport :true
        });
        YAHOO.GDMA.dialog.yesNoDialog.setHeader(header);
        // YAHOO.GDMA.dialog.yesNoDialog.cfg.queueProperty("buttons",
        // yesnoButtons);
        YAHOO.GDMA.dialog.yesNoDialog.render("body");
    }
    YAHOO.GDMA.dialog.yesNoDialog.cfg.queueProperty("buttons", yesnoButtons);
    YAHOO.GDMA.dialog.yesNoDialog.cfg.refresh();
    YAHOO.GDMA.dialog.yesNoDialog.setBody(text);
    YAHOO.GDMA.dialog.yesNoDialog.setHeader(header);
    YAHOO.GDMA.dialog.yesNoDialog.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_ALARM);

    // Render the Dialog

    YAHOO.GDMA.dialog.yesNoDialog.show();
};

YAHOO.GDMA.dialog.showInfoDialog = function(header, text, icon) {
	YAHOO.GDMA.dialog.loading.hide();
    var handleOk = function() {
        this.hide();
    };

    if (!icon)
        icon = YAHOO.widget.SimpleDialog.ICON_INFO;

    // Instantiate the Dialog
    if (!YAHOO.GDMA.dialog.infoDialog) {
        YAHOO.GDMA.dialog.infoDialog = new YAHOO.widget.SimpleDialog("infoDialog", {
            fixedcenter :true,
            visible :false,
            draggable :false,
            close :false,
            modal :true,
            zindex :40000,
            text :"Click Ok?",
            constraintoviewport :true,
            buttons : [ {
                text :"Ok",
                handler :handleOk,
                isDefault :true
            } ]
        });
        YAHOO.GDMA.dialog.infoDialog.setHeader("Info");
        YAHOO.GDMA.dialog.infoDialog.render("body");
    }

    YAHOO.GDMA.dialog.infoDialog.setBody(text);
    YAHOO.GDMA.dialog.infoDialog.setHeader(header);
    YAHOO.GDMA.dialog.infoDialog.cfg.setProperty("icon", icon);

    // Render the Dialog

    YAHOO.GDMA.dialog.infoDialog.show();
};