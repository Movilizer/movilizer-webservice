package com.movilizer.mds.webservice.models.maf;


public abstract class MafSource {
    private Long scriptSystemId;
    private String scriptSrc;
    private String description;

    public MafSource(Long scriptSystemId, String scriptSrc, String description) {
        this.scriptSystemId = scriptSystemId;
        this.scriptSrc = scriptSrc;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MafSource mafSource = (MafSource) obj;

        if (scriptSystemId != null ? !scriptSystemId.equals(mafSource.scriptSystemId)
                : mafSource.scriptSystemId != null) {
            return false;
        }
        return description != null ? description.equals(mafSource.description)
                : mafSource.description == null;
    }

    @Override
    public int hashCode() {
        int result = scriptSystemId != null ? scriptSystemId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public Long getScriptSystemId() {
        return scriptSystemId;
    }

    public void setScriptSystemId(Long scriptSystemId) {
        this.scriptSystemId = scriptSystemId;
    }

    public String getScriptSrc() {
        return scriptSrc;
    }

    public void setScriptSrc(String scriptSrc) {
        this.scriptSrc = scriptSrc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
