YAHOO.namespace("GDMA");

//YAHOO.util.Event.onDOMReady(function() {
//    var myLogReader = new YAHOO.widget.LogReader();
//    myLogReader.hideCategory("info");
//    myLogReader.hideCategory("time");
//    myLogReader.hideCategory("window");
//    myLogReader.hideCategory("iframe");
//    myLogReader.hideCategory("ttip");
//    myLogReader.hideSource("global");
//    myLogReader.hideSource("Config");
//    myLogReader.hideSource("LogReader");
//    myLogReader.hideSource("Attribute");
//    myLogReader.hideSource("AttributeProvider");        
//    myLogReader.hideSource("DataSource");
//});


YAHOO.GDMA.init = function(){
    var units = [
                 { position: 'top', height: 80, resize: false, body: 'top1', gutter: '0px 0px 0px 0px'},
                 { position: 'bottom', height: 30, body: 'footer1', gutter: '0px 0px 0px 0px' }                
             ];
    //server broswer will not be there for admin screens
    if(YAHOO.util.Dom.get('divServerBrowser')){
        units.push(
                { position: 'left', header: 'Server Browser', width: 200, resize: true, body: 'left1', gutter: '2px 4px 2px 0px', collapse: true, collapseSize: 15, scroll: true, animate: true }
        );
        units.push(
                { position: 'center', header: 'Data Grid', body: 'center1', scroll: true, height: '200px', gutter: '2px 0px 2px 0px' }
        );
    }else{
        units.push(
                { position: 'center', header: 'Admin - Servers', body: 'center1', scroll: true, height: '200px', gutter: '20px 20px 20px 20px' }
        );
    }
    
    YAHOO.GDMA.layout = new YAHOO.widget.Layout({ units: units });
    YAHOO.GDMA.layout.render();
    YAHOO.GDMA.layout.on('render', function() {
        if(YAHOO.GDMA.layout.getUnitByPosition('left')){
            YAHOO.GDMA.layout.getUnitByPosition('left').on('close', function() {
                YAHOO.GDMA.closeLeft();
            });
        }
    });

    YAHOO.util.Event.on('tLeft', 'click', function(ev) {
        YAHOO.util.Event.stopEvent(ev);
        YAHOO.GDMA.layout.getUnitByPosition('left').toggle();
    });

    YAHOO.GDMA.closeLeft = function() {
        var a = document.createElement('a');
        a.href = '#';
        a.innerHTML = 'Add Left Unit';
        YAHOO.util.Dom.get('closeLeft').parentNode.appendChild(a);

        YAHOO.util.Dom.setStyle('tLeft', 'display', 'none');
        YAHOO.util.Dom.setStyle('closeLeft', 'display', 'none');
        YAHOO.util.Event.on(a, 'click', function(ev) {
            YAHOO.util.Event.stopEvent(ev);
            YAHOO.util.Dom.setStyle('tLeft', 'display', 'inline');
            YAHOO.util.Dom.setStyle('closeLeft', 'display', 'inline');
            a.parentNode.removeChild(a);
            YAHOO.GDMA.layout.addUnit(YAHOO.GDMA.layout.get('units')[3]);
            YAHOO.GDMA.layout.getUnitByPosition('left').on('close', function() {
                YAHOO.GDMA.closeLeft();
            });
        });
    };
        
};    

YAHOO.util.Event.onDOMReady(YAHOO.GDMA.init);