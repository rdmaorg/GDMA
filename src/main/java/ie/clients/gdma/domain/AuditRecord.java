package ie.clients.gdma.domain;

/**
 * @author RGILL
 * 
 * This class is an Audit Record
 */
public class AuditRecord {
    private Long id;
    private Long auditHeaderID;
    private Long columnID;
    private String oldValue;
    private String newValue;

    /**
     * @return Returns the auditHeaderID.
     */
    public Long getAuditHeaderID() {
        return auditHeaderID;
    }

    /**
     * @param auditHeaderID
     *            The auditHeaderID to set.
     */
    public void setAuditHeaderID(Long auditHeaderID) {
        this.auditHeaderID = auditHeaderID;
    }

    /**
     * @return Returns the columnID.
     */
    public Long getColumnID() {
        return columnID;
    }

    /**
     * @param columnID
     *            The columnID to set.
     */
    public void setColumnID(Long columnID) {
        this.columnID = columnID;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the newValue.
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * @param newValue
     *            The newValue to set.
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * @return Returns the oldValue.
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue
     *            The oldValue to set.
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
}
