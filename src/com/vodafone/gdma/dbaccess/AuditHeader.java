package com.vodafone.gdma.dbaccess;

import java.sql.Timestamp;


/**
 * @author RGILL
 *
 * This class is an Audit Header record
 */
public class AuditHeader {
    private Long id;
    private Long tableID;
    private String type;
    private String modifiedBy;
    private Timestamp modifiedOn;
    /**
     * @return Returns the iD.
     */
    public Long getID() {
        return id;
    }
    /**
     * @param id The iD to set.
     */
    public void setID(Long id) {
        this.id = id;
    }
    /**
     * @return Returns the modifiedBy.
     */
    public String getModifiedBy() {
        return modifiedBy;
    }
    /**
     * @param modifiedBy The modifiedBy to set.
     */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    /**
     * @return Returns the modifiedOn.
     */
    public Timestamp getModifiedOn() {
        return modifiedOn;
    }
    /**
     * @param modifiedOn The modifiedOn to set.
     */
    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    /**
     * @return Returns the tableID.
     */
    public Long getTableID() {
        return tableID;
    }
    /**
     * @param tableID The tableID to set.
     */
    public void setTableID(Long tableID) {
        this.tableID = tableID;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
