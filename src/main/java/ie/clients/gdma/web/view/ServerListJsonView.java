package ie.clients.gdma.web.view;

import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.AbstractView;


public class ServerListJsonView extends AbstractView {

    protected static Logger LOG = Logger.getLogger(ServerListJsonView.class);

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setContentType("application/json");
        JSONArray jsonServers = new JSONArray();
        List<Server> servers = (List<Server>) model.get("servers");
        for (Server server : servers) {
            JSONObject jsonServer = new JSONObject();
            jsonServer.put("id", server.getId());
            jsonServer.put("name", server.getName());

            JSONArray jsonTables = new JSONArray();

            for (Table table : server.getTables()) {
                JSONObject jsonTable = new JSONObject();
                jsonTable.put("id", table.getId());
                jsonTable.put("name", table.getName());
                jsonTables.add(jsonTable);
            }

            jsonServer.put("tables", jsonTables);
            jsonServers.add(jsonServer);
        }
        jsonServers.write(response.getWriter());

    }
}
