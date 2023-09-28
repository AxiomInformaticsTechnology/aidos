package com.skyline.model;

import java.io.Serializable;

public interface SkyTuple extends Serializable {

    public Object[] getValues();

    public void setValues(Object[] values);

    public String getIdentifier();

    public void setIdentifier(String identifier);

    public String getDominate();

    public void setDominate(String dominate);

    public Long getCreated();

    public void setCreated(Long created);

    public Long getExpiration();

    public void setExpiration(Long expiration);

    public SkyAction getAction();

    public void setAction(SkyAction action);

    public int getLayer();

    public void setLayer(int layer);

    public void layerUp();

    public void layerDown();

    public boolean isExpired();

}
