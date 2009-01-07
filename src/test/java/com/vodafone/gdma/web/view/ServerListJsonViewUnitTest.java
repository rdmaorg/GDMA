package com.vodafone.gdma.web.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vodafone.gdma.domain.Server;

public class ServerListJsonViewUnitTest{

    protected static Logger LOG = Logger.getLogger(ServerListJsonViewUnitTest.class);

    private List<Server> servers;

    @Before
    public void setup(){
        //create map
        XStream xstream = new XStream(new PureJavaReflectionProvider(), new DomDriver());
        String file = "com/vodafone/gdma/web/view/servers.xml";
        servers = (ArrayList<Server>) xstream.fromXML(this.getClass().getClassLoader().getResourceAsStream(file));
    }

    @Test
    public void renderMergedOutputModel ()
            throws Exception {
        Map map = new HashMap<String, List>();
        map.put("servers", servers);
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServerListJsonView view = new ServerListJsonView();
        view.render(map, null, response);
        LOG.debug(response.getContentAsString());
    }

}
