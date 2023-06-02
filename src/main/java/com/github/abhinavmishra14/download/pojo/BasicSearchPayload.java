/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2023. Abhinav Kumar Mishra. 
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.abhinavmishra14.download.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class BasicSearchPayload.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query",
    "paging"
})
public class BasicSearchPayload {

    /** The query. */
    @JsonProperty("query")
    private Query query;
    
    /** The paging. */
    @JsonProperty("paging")
    private Paging paging;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Gets the query.
     *
     * @return the query
     */
    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the query.
     *
     * @param query the new query
     */
    @JsonProperty("query")
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * Gets the paging.
     *
     * @return the paging
     */
    @JsonProperty("paging")
    public Paging getPaging() {
        return paging;
    }

    /**
     * Sets the paging.
     *
     * @param paging the new paging
     */
    @JsonProperty("paging")
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     * Gets the additional properties.
     *
     * @return the additional properties
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /**
     * Sets the additional property.
     *
     * @param name the name
     * @param value the value
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BasicSearchPayload.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("query");
        sb.append('=');
        sb.append(((this.query == null)?"<null>":this.query));
        sb.append(',');
        sb.append("paging");
        sb.append('=');
        sb.append(((this.paging == null)?"<null>":this.paging));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.paging == null)? 0 :this.paging.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.query == null)? 0 :this.query.hashCode()));
        return result;
    }

    /**
     * Equals.
     *
     * @param other the other
     * @return true, if successful
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BasicSearchPayload) == false) {
            return false;
        }
        BasicSearchPayload rhs = ((BasicSearchPayload) other);
        return ((((this.paging == rhs.paging)||((this.paging!= null)&&this.paging.equals(rhs.paging)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.query == rhs.query)||((this.query!= null)&&this.query.equals(rhs.query))));
    }

}
