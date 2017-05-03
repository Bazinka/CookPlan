package com.cookplan.models;

import java.util.List;

/**
 * Created by DariaEfimova on 12.04.17.
 */

public class ShareUserInfo {

    private String id;
    public String ownerUserId;
    public String ownerUserName;
    public List<String> clientUserEmailList;

    public ShareUserInfo() {
    }

    public ShareUserInfo(String ownerUserId, String ownerUserName, List<String> clientUserEmails) {
        this.ownerUserId = ownerUserId;
        this.ownerUserName = ownerUserName;
        this.clientUserEmailList = clientUserEmails;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public List<String> getClientUserEmailList() {
        return clientUserEmailList;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
