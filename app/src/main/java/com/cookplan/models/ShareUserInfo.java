package com.cookplan.models;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ShareUserInfo {

    public String ownerUserId;
    public String ownerUserName;
    public String clientUserEmail;

    public ShareUserInfo() {
    }

    public ShareUserInfo(String ownerUserId, String ownerUserName, String clientUserId) {
        this.ownerUserId = ownerUserId;
        this.ownerUserName = ownerUserName;
        this.clientUserEmail = clientUserId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public String getClientUserEmail() {
        return clientUserEmail;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }
}
