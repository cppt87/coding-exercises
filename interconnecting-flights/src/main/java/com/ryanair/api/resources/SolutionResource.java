
package com.ryanair.api.resources;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Solution Resource schema
 * <p>
 * This object represents a flight departing from a given departure airport not earlier than the specified departure datetime and arriving to a given arrival airport not later than the specified arrival datetime
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "stops",
    "legs"
})
public class SolutionResource {

    /**
     * Flight's number of stops
     * (Required)
     * 
     */
    @JsonProperty("stops")
    @JsonPropertyDescription("Flight's number of stops")
    private Integer stops;
    /**
     * List of legs that a flight is composed of
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("legs")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Leg> legs = new LinkedHashSet<Leg>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Flight's number of stops
     * (Required)
     * 
     */
    @JsonProperty("stops")
    public Integer getStops() {
        return stops;
    }

    /**
     * Flight's number of stops
     * (Required)
     * 
     */
    @JsonProperty("stops")
    public void setStops(Integer stops) {
        this.stops = stops;
    }

    /**
     * List of legs that a flight is composed of
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("legs")
    public Set<Leg> getLegs() {
        return legs;
    }

    /**
     * List of legs that a flight is composed of
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("legs")
    public void setLegs(Set<Leg> legs) {
        this.legs = legs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(stops).append(legs).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SolutionResource) == false) {
            return false;
        }
        SolutionResource rhs = ((SolutionResource) other);
        return new EqualsBuilder().append(stops, rhs.stops).append(legs, rhs.legs).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
