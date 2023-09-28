package storm.skyline.model;

import static javax.persistence.FetchType.EAGER;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.skyline.model.BasicSkyTuple;
import com.skyline.model.SkyAction;
import com.skyline.model.SkyTuple;
import com.skyline.utility.MapperUtility;

import storm.skyline.model.converter.VectorConverter;

@Entity
public class StormSkyTuple implements SkyTuple {

    private static final long serialVersionUID = 770297296904537344L;

    @Id
    private String identifier;

    @ElementCollection(fetch = EAGER)
    @Convert(converter = VectorConverter.class)
    private List<String> values;

    @JsonInclude(Include.NON_NULL)
    @Column(nullable = true)
    private String dominate;

    @Column(nullable = false)
    private Long created;

    @JsonInclude(Include.NON_NULL)
    @Column(nullable = true)
    private Long expiration;

    @JsonIgnore
    @Column(nullable = false)
    private SkyAction action;

    @JsonIgnore
    @Column(nullable = false)
    private int mapId;

    @JsonIgnore
    @Column(nullable = false)
    private int reduceId;

    @Column(nullable = false)
    private Integer layer;

    public StormSkyTuple() {
        values = new ArrayList<String>();
        identifier = UUID.randomUUID().toString();
        created = Instant.now().toEpochMilli();
        action = SkyAction.INSERTION;
        layer = 0;
    }

    public StormSkyTuple(BasicSkyTuple tuple) {
        this();
        BeanUtils.copyProperties(tuple, this);
    }

    public StormSkyTuple(Object[] values) {
        this();
        setValues(values);
    }

    public StormSkyTuple(Object[] values, Long expiration) {
        this(values);
        this.expiration = expiration;
    }

    public StormSkyTuple(Object[] values, String identifier) {
        this(values);
        this.identifier = identifier;
    }

    public StormSkyTuple(Object[] values, String identifier, Long expiration) {
        this(values, identifier);
        this.expiration = expiration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object[] getValues() {
        return values.toArray(new Object[values.size()]);
    }

    public void setValues(Object[] values) {
        this.values.clear();
        for (Object value : values) {
            this.values.add(String.valueOf(value));
        }
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

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getReduceId() {
        return reduceId;
    }

    public void setReduceId(int reduceId) {
        this.reduceId = reduceId;
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
        return ((StormSkyTuple) obj).getIdentifier().equals(identifier);
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