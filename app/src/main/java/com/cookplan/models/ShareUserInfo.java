package com.cookplan.models;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ShareUserInfo {

    public String ownerUserId;
    public String ownerUserName;
    public String clientUserEmail;
    public SharedData sharedData;

    public ShareUserInfo() {
    }

    public ShareUserInfo(String ownerUserId, String ownerUserName, String clientUserId, SharedData sharedData) {
        this.ownerUserId = ownerUserId;
        this.ownerUserName = ownerUserName;
        this.clientUserEmail = clientUserId;
        this.sharedData = sharedData;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public String getClientUserEmail() {
        return clientUserEmail;
    }

    public SharedData getSharedData() {
        return sharedData;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }
}
