
package com.ryanair.apis.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * A single flight
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "number",
    "departureTime",
    "arrivalTime"
})
public class Flight {

    /**
     * A flight number
     * (Required)
     * 
     */
    @JsonProperty("number")
    @JsonPropertyDescription("A flight number")
    private String number;
    /**
     * A departure time in the departure airport timezone
     * (Required)
     * 
     */
    @JsonProperty("departureTime")
    @JsonPropertyDescription("A departure time in the departure airport timezone")
    private String departureTime;
    /**
     * An arrival time in the arrival airport timezone
     * (Required)
     * 
     */
    @JsonProperty("arrivalTime")
    @JsonPropertyDescription("An arrival time in the arrival airport timezone")
    private String arrivalTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A flight number
     * (Required)
     * 
     */
    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    /**
     * A flight number
     * (Required)
     * 
     */
    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * A departure time in the departure airport timezone
     * (Required)
     * 
     */
    @JsonProperty("departureTime")
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * A departure time in the departure airport timezone
     * (Required)
     * 
     */
    @JsonProperty("departureTime")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * An arrival time in the arrival airport timezone
     * (Required)
     * 
     */
    @JsonProperty("arrivalTime")
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * An arrival time in the arrival airport timezone
     * (Required)
     * 
     */
    @JsonProperty("arrivalTime")
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
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
        return new HashCodeBuilder().append(number).append(departureTime).append(arrivalTime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Flight) == false) {
            return false;
        }
        Flight rhs = ((Flight) other);
        return new EqualsBuilder().append(number, rhs.number).append(departureTime, rhs.departureTime).append(arrivalTime, rhs.arrivalTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
