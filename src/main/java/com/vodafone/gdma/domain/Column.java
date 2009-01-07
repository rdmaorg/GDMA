package com.vodafone.gdma.domain;

/**
 * @author rgill
 *
 */
/**
 * @author ronan
 * 
 */
public class Column {

    private Long id;

    private String name;

    private int columnType;

    private String columnTypeString;

    private Column dropDownColumnDisplay;

    private Column dropDownColumnStore;

    private boolean displayed;

    private boolean allowInsert;

    private boolean allowUpdate;

    private boolean nullable;

    private boolean primarykey;

    private String special;

    private Table table;

    private Integer minWidth;

    private Integer maxWidth;

    private Integer orderby;

    public Integer getOrderby() {
        return orderby;
    }

    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeString() {
        return columnTypeString;
    }

    public void setColumnTypeString(String columnTypeString) {
        this.columnTypeString = columnTypeString;
    }

    public Column getDropDownColumnDisplay() {
        return dropDownColumnDisplay;
    }

    public void setDropDownColumnDisplay(Column dropDownColumnDisplay) {
        this.dropDownColumnDisplay = dropDownColumnDisplay;
    }

    public Column getDropDownColumnStore() {
        return dropDownColumnStore;
    }

    public void setDropDownColumnStore(Column dropDownColumnStore) {
        this.dropDownColumnStore = dropDownColumnStore;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public boolean isAllowInsert() {
        return allowInsert;
    }

    public void setAllowInsert(boolean allowInsert) {
        this.allowInsert = allowInsert;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Column))
            return false;
        final Column that = (Column) other;
        return this.name.equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean isPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(boolean primarykey) {
        this.primarykey = primarykey;
    }

}
