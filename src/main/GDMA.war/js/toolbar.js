// Create a namespace for this code
YAHOO.namespace("GDMA.toolbar");

YAHOO.GDMA.toolbar.buttons = {};
YAHOO.GDMA.toolbar.currentMode = "defaultMode";

// Methods to hide/display buttons
YAHOO.GDMA.toolbar.changeState = function(mode) {
    if(!mode || mode == null || mode == "")
        mode = "defaultMode";
    YAHOO.GDMA.toolbar.currentMode = mode;
    
    for(var id in YAHOO.GDMA.toolbar.buttons){
        var button = YAHOO.GDMA.toolbar.buttons[id];
        if(button.config[mode] == "show"){
            button.button.show();
        }else{
            button.button.hide();
        }
    }
};

// Create the toolbar
YAHOO.GDMA.toolbar.createToolbar = function(buttons) {
    for(var i = 0; i < buttons.length; i++){
        var name = buttons[i].name;
        var id = "btn" + name;
            
        // do we have a button with that id already
        if(YAHOO.GDMA.toolbar.buttons[id]){
            YAHOO.GDMA.toolbar.buttons[id].config = buttons[i];
            YAHOO.GDMA.toolbar.updateToolBarButton(id, buttons[i]);
        }else{            
            
            YAHOO.GDMA.toolbar.buttons[id] = {}
            YAHOO.GDMA.toolbar.buttons[id].name = name;
            YAHOO.GDMA.toolbar.buttons[id].config = buttons[i];
            YAHOO.GDMA.toolbar.buttons[id].button =  YAHOO.GDMA.toolbar.createToolBarButton(name, id, "divToolbar", buttons[i].fn, buttons[i].tooltip, buttons[i].defaultMode);        
        }
    }
}

YAHOO.GDMA.toolbar.initTooltip = function() {
    YAHOO.GDMA.toolbar.tooltip = new YAHOO.widget.Tooltip("ttToolbar", {context: []});
    YAHOO.GDMA.toolbar.tooltipMessages = {};
    YAHOO.GDMA.toolbar.tooltip.contextTriggerEvent.subscribe(
        function(type, args) {
            var context = args[0];
            YAHOO.log("tt context id " + context.id, "warn", "YAHOO.GDMA.toolbar");
            YAHOO.log("Setting tt text to " + YAHOO.GDMA.toolbar.tooltipMessages[context.id] , "warn", "YAHOO.GDMA.toolbar");
            YAHOO.GDMA.toolbar.tooltip.cfg.setProperty("text",YAHOO.GDMA.toolbar.tooltipMessages[context.id]);
        }
    );  
};

YAHOO.GDMA.toolbar.addToToolTipContext = function(id) {
    var context = YAHOO.GDMA.toolbar.tooltip.cfg.getProperty("context");
    for(var i = 0; i < context.length; i++){
        if(context[i] == id)
            return;
    }
    
    context.push(id);
}
// Generic method for creating a toolbar button
YAHOO.GDMA.toolbar.createToolBarButton = function(name, id, container, fn, tooltip, showOrHide) {
    YAHOO.log("Creating button " + id + " buttons", "warn", "YAHOO.GDMA.toolbar");
    var button = new YAHOO.widget.Button( {
        id:         id,
        type:       "push",
        label:      name,
        container:  container,
        onclick : {
            fn : fn
        }
    });

    if( showOrHide != "show"){
        button.hide();
    }
    
    // reuse tooltips as it saves a LOT of browser resources!
    // needs to be here as a dialog could create a button
    if(!YAHOO.GDMA.toolbar.tooltip){
        YAHOO.GDMA.toolbar.initTooltip();
    }
    YAHOO.GDMA.toolbar.addToToolTipContext(id);
    YAHOO.GDMA.toolbar.tooltipMessages[id] = tooltip;
    
    return button;
};

//Generic method for updating toolbar button
YAHOO.GDMA.toolbar.updateToolBarButton = function(id, buttonConfig) {
    var button = YAHOO.GDMA.toolbar.buttons[id];
    button.config = buttonConfig;
    button.button.set("onclick", {fn: buttonConfig.fn});
    button.button.set("name", buttonConfig.name);
    
    if(button.config[YAHOO.GDMA.toolbar.currentMode] == "show"){
        button.button.show();
    }else{
        button.button.hide();
    }
    
    YAHOO.GDMA.toolbar.tooltipMessages[id] = buttonConfig.tooltip;
    return button;
};

//Extend YUI Buttons with some utility methods
if (YAHOO.widget.Button) {
    if (!YAHOO.widget.Button.hide) {
        YAHOO.widget.Button.prototype.hide = function() {
            this.setStyle("display", "none");
        }
    } else {
        alert("ERROR: Trying to create the prototype 'hide' in the unloaded library 'YAHOO.widget.Button' but it already exists!");
    }
    if (!YAHOO.widget.Button.show) {
        YAHOO.widget.Button.prototype.show = function() {
            try{
                this.setStyle("display", "-moz-inline-box");
            }catch (e) {
            }
            this.setStyle("display", "inline-block");
        }
    } else {
        alert("ERROR: Trying to create the prototype 'show' in the unloaded library 'YAHOO.widget.Button' but it already exists!");
    }
} else {
    alert("ERROR: Trying to create a prototype in the unloaded library 'YAHOO.widget.Button'");
}