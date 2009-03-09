package ie.clients.gdma.dao.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.ColumnDao;
import ie.clients.gdma.dao.DynamicDao;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.util.BaseSpringImplTest;
import ie.clients.gdma.web.command.ColumnUpdate;
import ie.clients.gdma.web.command.PaginatedRequest;
import ie.clients.gdma.web.command.PaginatedResponse;
import ie.clients.gdma.web.command.UpdateRequest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DynamicDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(DynamicDaoImplTest.class);

    @Autowired
    private DynamicDao dynamicDao;

    @Autowired
    private ColumnDao columnDao;

    @Test
    public void load() {

        PaginatedRequest paginatedRequest = new PaginatedRequest();
        paginatedRequest.setServerId(271L);
        paginatedRequest.setTableId(298L);
        paginatedRequest.setRecordOffset(25);
        paginatedRequest.setRowsPerPage(8);

        PaginatedResponse paginatedResponse = dynamicDao.get(paginatedRequest);

        assertNotNull(paginatedResponse);
        assertTrue(paginatedResponse.getRecords().size() == 8);
        for (Object object : paginatedResponse.getRecords()) {

            List list = (ArrayList) object;
            String row = "";
            for (Object object2 : list) {
                row += (object2 == null ? "NULL" : object2.toString()) + ",";

            }
            LOG.debug(row);
        }
    }

    @Test
    public void load2() {

        PaginatedRequest paginatedRequest = new PaginatedRequest();
        paginatedRequest.setServerId(271L);
        paginatedRequest.setTableId(295L);
        paginatedRequest.setRecordOffset(0);
        paginatedRequest.setRowsPerPage(25);

        PaginatedResponse paginatedResponse = dynamicDao.get(paginatedRequest);

        assertNotNull(paginatedResponse);

        assertTrue(paginatedResponse.getRecords().size() == 25);
        for (Object object : paginatedResponse.getRecords()) {

            List list = (ArrayList) object;
            String row = "";
            for (Object object2 : list) {
                row += (object2 == null ? "NULL" : object2.toString()) + ",";

            }
            LOG.debug(row);
        }
    }

    @Test
    public void loadDropDown() {

        Column store = columnDao.get(318L);
        Column display = columnDao.get(319L);
        LOG.debug(display.getTable() == null);
        List data = dynamicDao.getDropDownData(display, store);
        for (Object object : data) {
            LOG.debug(object.getClass().getName());
        }

    }

    @Test
    @Rollback(false)
    public void all() {

        // TODO clear down delete me table first

        long deleteMeServerId = 296;
        long deleteMeTableId = 322;

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setServerId(deleteMeServerId);
        updateRequest.setTableId(deleteMeTableId);

        // read in some sample data
        String testServerFile = "ie/clients/gdma/dao/jdbc/addRecords.xml";
        XStream xstream = new XStream(new PureJavaReflectionProvider(), new DomDriver());
        List<List<ColumnUpdate>> updates = (List<List<ColumnUpdate>>) xstream.fromXML(getClass().getClassLoader()
                .getResourceAsStream(testServerFile));

        updateRequest.setUpdates(updates);

        // add some records first
        dynamicDao.addRecord(updateRequest);

        // load records

        PaginatedRequest paginatedRequest = new PaginatedRequest();
        paginatedRequest.setServerId(deleteMeServerId);
        paginatedRequest.setTableId(deleteMeTableId);
        paginatedRequest.setRecordOffset(0);
        paginatedRequest.setRowsPerPage(3);

        PaginatedResponse paginatedResponse = dynamicDao.get(paginatedRequest);

        assertNotNull(paginatedResponse);

        List record = (List) paginatedResponse.getRecords().get(0);
        for (Object object : record) {
            if (object != null) {
                LOG.debug(object.getClass().getName() + " : " + object.toString());
            } else {
                LOG.debug("NULL");
            }

        }

        for (List<ColumnUpdate> list : updateRequest.getUpdates()) {
            for (ColumnUpdate columnUpdate : list) {
                columnUpdate.setOldColumnValue(columnUpdate.getNewColumnValue());
                columnUpdate.setNewColumnValue(null);
            }
        }

        // now try delete
        dynamicDao.deleteRecords(updateRequest);

    }

}
