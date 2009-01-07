package com.vodafone.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.dao.ColumnDao;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.util.BaseSpringImplTest;

public class ColumnDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(ColumnDaoImplTest.class);

    @Autowired
    private ColumnDao columnDao;

    @Test
    public void all() {
        Column column = columnDao.get(295L);
        assertNotNull("Column is null", column);
        column.setName("namechange");
        assertTrue("Table list is empty ", "namechange".equals(column.getName()));
        columnDao.save(column);
        assertTrue("Table list is empty ", "namechange".equals(column.getName()));
    }

}
