package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import com.vodafone.gdma.dao.ColumnDao;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.util.SqlUtil;

/**
 * @author Ronan Gill
 * 
 */
public class ColumnDaoImpl extends HibernateDaoSupport implements ColumnDao {

    public Column get(Long id) {
        return (Column) getHibernateTemplate().load(Column.class, id);
    }

    public Column save(Column column) {
        applyRules(column);
        getHibernateTemplate().saveOrUpdate(column);
        return column;
    }

    public void save(List<Column> columns) {
        for (Column column : columns) {
            applyRules(column);
        }
        getHibernateTemplate().saveOrUpdateAll(columns);
    }

    private void applyRules(Column column) {
        if (column.isPrimarykey()) {
            column.setDisplayed(true);
            column.setAllowUpdate(false);
            column.setNullable(false);
            column.setSpecial("N");
        }

        if (!column.isNullable()) {
            column.setAllowInsert(true);

        }

        if (column.isAllowInsert()) {
            column.setDisplayed(true);
        }

        if (column.isAllowUpdate()) {
            column.setDisplayed(true);
        }

        if (StringUtils.hasText(column.getSpecial())) {
            if ("U".equals(column.getSpecial())) {
                // check column type
                if (!SqlUtil.isText(column.getColumnTypeString())) {
                    throw new IllegalArgumentException("Column [" + column.getName() + "] of type ["
                            + column.getColumnTypeString()
                            + "] can not be set to 'special type User' as is is not of type text.");
                }
                column.setAllowInsert(false);
                column.setAllowUpdate(false);

            }
            if ("D".equals(column.getSpecial())) {
                // check column type
                if (!SqlUtil.isDate(column.getColumnTypeString())) {
                    throw new IllegalArgumentException("Column [" + column.getName()
                            + "] can not be set to 'special type Date' as is is not of type date.");
                }
                column.setAllowInsert(false);
                column.setAllowUpdate(false);
            }
        }

    }

}
