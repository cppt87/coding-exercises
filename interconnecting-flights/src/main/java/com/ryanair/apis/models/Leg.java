
package com.ryanair.apis.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * A single leg that a flight is composed of
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "departureAirport",
    "arrivalAirport",
    "departureDateTime",
    "arrivalDateTime"
})
public class Leg {

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("departureAirport")
    @JsonPropertyDescription("A departure airport IATA code")
    private String departureAirport;
    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("arrivalAirport")
    @JsonPropertyDescription("An arrival airport IATA code")
    private String arrivalAirport;
    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("departureDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonPropertyDescription("Any type of date. ISO 8601 datetime")
    private Date departureDateTime;
    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("arrivalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonPropertyDescription("Any type of date. ISO 8601 datetime")
    private Date arrivalDateTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("departureAirport")
    public String getDepartureAirport() {
        return departureAirport;
    }

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("departureAirport")
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("arrivalAirport")
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("arrivalAirport")
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("departureDateTime")
    public Date getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("departureDateTime")
    public void setDepartureDateTime(Date departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("arrivalDateTime")
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    /**
     * Any type of date. ISO 8601 datetime
     * (Required)
     * 
     */
    @JsonProperty("arrivalDateTime")
    public void setArrivalDateTime(Date arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
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
        return new HashCodeBuilder().append(departureAirport).append(arrivalAirport).append(departureDateTime).append(arrivalDateTime).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leg) == false) {
            return false;
        }
        Leg rhs = ((Leg) other);
        return new EqualsBuilder().append(departureAirport, rhs.departureAirport).append(arrivalAirport, rhs.arrivalAirport).append(departureDateTime, rhs.departureDateTime).append(arrivalDateTime, rhs.arrivalDateTime).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
