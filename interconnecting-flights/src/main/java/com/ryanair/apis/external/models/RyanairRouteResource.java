
package com.ryanair.apis.external.models;

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
 * Ryanair Route Resource schema
 * <p>
 * This object represents a route based on the airport's IATA codes
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "airportFrom",
    "airportTo",
    "connectingAirport",
    "newRoute",
    "seasonalRoute"
})
public class RyanairRouteResource {

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportFrom")
    @JsonPropertyDescription("A departure airport IATA code")
    private String airportFrom;
    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportTo")
    @JsonPropertyDescription("An arrival airport IATA code")
    private String airportTo;
    /**
     * A connecting airport IATA code
     * 
     */
    @JsonProperty("connectingAirport")
    @JsonPropertyDescription("A connecting airport IATA code")
    private String connectingAirport;
    /**
     * Is the route a new one?
     * (Required)
     * 
     */
    @JsonProperty("newRoute")
    @JsonPropertyDescription("Is the route a new one?")
    private Boolean newRoute;
    /**
     * Is the route seasonal?
     * (Required)
     * 
     */
    @JsonProperty("seasonalRoute")
    @JsonPropertyDescription("Is the route seasonal?")
    private Boolean seasonalRoute;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportFrom")
    public String getAirportFrom() {
        return airportFrom;
    }

    /**
     * A departure airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportFrom")
    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportTo")
    public String getAirportTo() {
        return airportTo;
    }

    /**
     * An arrival airport IATA code
     * (Required)
     * 
     */
    @JsonProperty("airportTo")
    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    /**
     * A connecting airport IATA code
     * 
     */
    @JsonProperty("connectingAirport")
    public String getConnectingAirport() {
        return connectingAirport;
    }

    /**
     * A connecting airport IATA code
     * 
     */
    @JsonProperty("connectingAirport")
    public void setConnectingAirport(String connectingAirport) {
        this.connectingAirport = connectingAirport;
    }

    /**
     * Is the route a new one?
     * (Required)
     * 
     */
    @JsonProperty("newRoute")
    public Boolean getNewRoute() {
        return newRoute;
    }

    /**
     * Is the route a new one?
     * (Required)
     * 
     */
    @JsonProperty("newRoute")
    public void setNewRoute(Boolean newRoute) {
        this.newRoute = newRoute;
    }

    /**
     * Is the route seasonal?
     * (Required)
     * 
     */
    @JsonProperty("seasonalRoute")
    public Boolean getSeasonalRoute() {
        return seasonalRoute;
    }

    /**
     * Is the route seasonal?
     * (Required)
     * 
     */
    @JsonProperty("seasonalRoute")
    public void setSeasonalRoute(Boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
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
        return new HashCodeBuilder().append(airportFrom).append(airportTo).append(connectingAirport).append(newRoute).append(seasonalRoute).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RyanairRouteResource) == false) {
            return false;
        }
        RyanairRouteResource rhs = ((RyanairRouteResource) other);
        return new EqualsBuilder().append(airportFrom, rhs.airportFrom).append(airportTo, rhs.airportTo).append(connectingAirport, rhs.connectingAirport).append(newRoute, rhs.newRoute).append(seasonalRoute, rhs.seasonalRoute).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
