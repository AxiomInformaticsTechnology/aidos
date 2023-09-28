package com.skyline.model;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.skyline.utility.MapperUtility;

public class AbstractSkyTuple implements SkyTuple {

    private static final long serialVersionUID = 3393684909152106440L;

    private Object[] values;

    private String identifier;

    @JsonInclude(Include.NON_NULL)
    private String dominate;

    private Long created;

    @JsonInclude(Include.NON_NULL)
    private Long expiration;

    @JsonIgnore
    private SkyAction action;

    private Integer layer;

    public AbstractSkyTuple() {
        identifier = UUID.randomUUID().toString();
        created = Instant.now().toEpochMilli();
        action = SkyAction.INSERTION;
        layer = 0;
    }

    public AbstractSkyTuple(Object[] values) {
        this();
        this.values = values;
    }

    public AbstractSkyTuple(Object[] values, Long expiration) {
        this(values);
        this.expiration = expiration;
    }

    public AbstractSkyTuple(Object[] values, String identifier) {
        this(values);
        this.identifier = identifier;
    }

    public AbstractSkyTuple(Object[] values, String identifier, Long expiration) {
        this(values, identifier);
        this.expiration = expiration;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDominate() {
        return dominate;
    }

    public void setDominate(String dominate) {
        this.dominate = dominate;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public SkyAction getAction() {
        return action;
    }

    public void setAction(SkyAction action) {
        this.action = action;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void layerUp() {
        layer--;
    }

    public void layerDown() {
        layer++;
    }

    @JsonIgnore
    public boolean isExpired() {
        boolean expired = false;
        if (expiration != null) {
            expired = Instant.now().toEpochMilli() >= expiration;
        }
        return expired;
    }

    @Override
    public boolean equals(Object obj) {
        return ((BasicSkyTuple) obj).getIdentifier().equals(identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return MapperUtility.serialize(this);
    }

}
