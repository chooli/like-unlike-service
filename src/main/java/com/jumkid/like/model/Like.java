package com.jumkid.like.model;

public class Like {

    public enum Fields {
        CLIENT_ID("clientId"), ENTITY_NAME("entityName"), ENTITY_ID("entityId"), VALUE("value");

        private String value;

        private Fields(String value){ this.value = value; }

        public String value() {return value;}

    }

    public Like(String clientId, String entityName, String entityId, Long value) {
        this.clientId = clientId;
        this.entityName = entityName;
        this.entityId = entityId;
        this.value = value;
    }

    private String clientId;
    private String entityName;
    private String entityId;
    private Long value;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
