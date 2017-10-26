package com.movilizer.mds.webservice.models.maf;

import com.google.gson.annotations.SerializedName;


public class MafLibraryScript extends MafSource {
    private String name;
    @SerializedName("public")
    private Boolean isPublic;
    private String namespace;

    public MafLibraryScript(Long scriptSystemId, String scriptSrc, String description, String name,
                            Boolean isPublic, String namespace) {
        super(scriptSystemId, scriptSrc, description);
        this.name = name;
        this.isPublic = isPublic;
        this.namespace = namespace;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }

        MafLibraryScript that = (MafLibraryScript) obj;

        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (isPublic != null ? !isPublic.equals(that.isPublic) : that.isPublic != null) {
            return false;
        }
        return namespace != null ? namespace.equals(that.namespace) : that.namespace == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isPublic != null ? isPublic.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
