package com.movilizer.mds.webservice.models.maf;


public class MafGenericScript extends MafSource {
    private String scriptKey;
    private String name;

    public MafGenericScript(Long scriptSystemId, String scriptSrc, String description,
                            String scriptKey, String name) {
        super(scriptSystemId, scriptSrc, description);
        this.scriptKey = scriptKey;
        this.name = name;
    }

    public String getScriptKey() {
        return scriptKey;
    }

    public void setScriptKey(String scriptKey) {
        this.scriptKey = scriptKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
