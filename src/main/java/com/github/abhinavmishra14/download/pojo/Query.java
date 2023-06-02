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
 * The Class Query.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query",
    "language"
})
public class Query {

    /** The query. */
    @JsonProperty("query")
    private String query;
    
    /** The language. */
    @JsonProperty("language")
    private String language;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Gets the query.
     *
     * @return the query
     */
    @JsonProperty("query")
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query.
     *
     * @param query the new query
     */
    @JsonProperty("query")
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     *
     * @param language the new language
     */
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
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
        sb.append(Query.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("query");
        sb.append('=');
        sb.append(((this.query == null)?"<null>":this.query));
        sb.append(',');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null)?"<null>":this.language));
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
        result = ((result* 31)+((this.language == null)? 0 :this.language.hashCode()));
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
        if ((other instanceof Query) == false) {
            return false;
        }
        Query rhs = ((Query) other);
        return ((((this.language == rhs.language)||((this.language!= null)&&this.language.equals(rhs.language)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.query == rhs.query)||((this.query!= null)&&this.query.equals(rhs.query))));
    }

}
