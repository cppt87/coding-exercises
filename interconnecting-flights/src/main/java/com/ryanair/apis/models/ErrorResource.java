
package com.ryanair.apis.models;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Error Resource schema
 * <p>
 * This object represents a common error object
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "errors"
})
public class ErrorResource {

    /**
     * List of errors reported
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errors")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Error> errors = new LinkedHashSet<Error>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * List of errors reported
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errors")
    public Set<Error> getErrors() {
        return errors;
    }

    /**
     * List of errors reported
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("errors")
    public void setErrors(Set<Error> errors) {
        this.errors = errors;
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
        return new HashCodeBuilder().append(errors).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ErrorResource) == false) {
            return false;
        }
        ErrorResource rhs = ((ErrorResource) other);
        return new EqualsBuilder().append(errors, rhs.errors).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
