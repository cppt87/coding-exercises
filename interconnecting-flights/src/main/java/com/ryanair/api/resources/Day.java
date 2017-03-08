
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
 * List of flights scheduled in a day
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "day",
    "fligths"
})
public class Day {

    /**
     * A day of a month
     * (Required)
     * 
     */
    @JsonProperty("day")
    @JsonPropertyDescription("A day of a month")
    private Integer day;
    /**
     * A list of flights for given day
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("fligths")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Fligth> fligths = new LinkedHashSet<Fligth>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A day of a month
     * (Required)
     * 
     */
    @JsonProperty("day")
    public Integer getDay() {
        return day;
    }

    /**
     * A day of a month
     * (Required)
     * 
     */
    @JsonProperty("day")
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * A list of flights for given day
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("fligths")
    public Set<Fligth> getFligths() {
        return fligths;
    }

    /**
     * A list of flights for given day
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("fligths")
    public void setFligths(Set<Fligth> fligths) {
        this.fligths = fligths;
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
        return new HashCodeBuilder().append(day).append(fligths).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Day) == false) {
            return false;
        }
        Day rhs = ((Day) other);
        return new EqualsBuilder().append(day, rhs.day).append(fligths, rhs.fligths).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
