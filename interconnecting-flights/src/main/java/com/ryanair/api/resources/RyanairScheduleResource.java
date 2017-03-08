
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
 * Ryanair Schedule Resource schema
 * <p>
 * This object represents a list of available flights for a given departure airport IATA code, an arrival airport IATA code, a year and a month
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "month",
    "days"
})
public class RyanairScheduleResource {

    /**
     * A month of a year
     * (Required)
     * 
     */
    @JsonProperty("month")
    @JsonPropertyDescription("A month of a year")
    private Integer month;
    /**
     * Daily flights schedule
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("days")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Day> days = new LinkedHashSet<Day>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A month of a year
     * (Required)
     * 
     */
    @JsonProperty("month")
    public Integer getMonth() {
        return month;
    }

    /**
     * A month of a year
     * (Required)
     * 
     */
    @JsonProperty("month")
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * Daily flights schedule
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("days")
    public Set<Day> getDays() {
        return days;
    }

    /**
     * Daily flights schedule
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("days")
    public void setDays(Set<Day> days) {
        this.days = days;
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
        return new HashCodeBuilder().append(month).append(days).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RyanairScheduleResource) == false) {
            return false;
        }
        RyanairScheduleResource rhs = ((RyanairScheduleResource) other);
        return new EqualsBuilder().append(month, rhs.month).append(days, rhs.days).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
