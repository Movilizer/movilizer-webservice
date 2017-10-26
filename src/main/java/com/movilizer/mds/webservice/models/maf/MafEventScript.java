package com.movilizer.mds.webservice.models.maf;


public class MafEventScript extends MafSource {
    private String scriptKey;
    private String scriptTriggers;
    private String appSpace;
    private String moveletKey;
    private String moveletKeyExt;
    private String deviceAddress;
    private String action;
    private String location;
    private String bizStep;

    public MafEventScript(Long scriptSystemId, String scriptSrc, String description,
                          String scriptKey, String scriptTriggers, String appSpace,
                          String moveletKey, String moveletKeyExt, String deviceAddress,
                          String action, String location, String bizStep) {
        super(scriptSystemId, scriptSrc, description);
        this.scriptKey = scriptKey;
        this.scriptTriggers = scriptTriggers;
        this.appSpace = appSpace;
        this.moveletKey = moveletKey;
        this.moveletKeyExt = moveletKeyExt;
        this.deviceAddress = deviceAddress;
        this.action = action;
        this.location = location;
        this.bizStep = bizStep;
    }

    public String getScriptKey() {
        return scriptKey;
    }

    public void setScriptKey(String scriptKey) {
        this.scriptKey = scriptKey;
    }

    public String getScriptTriggers() {
        return scriptTriggers;
    }

    public void setScriptTriggers(String scriptTriggers) {
        this.scriptTriggers = scriptTriggers;
    }

    public String getAppSpace() {
        return appSpace;
    }

    public void setAppSpace(String appSpace) {
        this.appSpace = appSpace;
    }

    public String getMoveletKey() {
        return moveletKey;
    }

    public void setMoveletKey(String moveletKey) {
        this.moveletKey = moveletKey;
    }

    public String getMoveletKeyExt() {
        return moveletKeyExt;
    }

    public void setMoveletKeyExt(String moveletKeyExt) {
        this.moveletKeyExt = moveletKeyExt;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBizStep() {
        return bizStep;
    }

    public void setBizStep(String bizStep) {
        this.bizStep = bizStep;
    }
}
