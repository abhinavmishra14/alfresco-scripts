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
 * The Class Paging.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "maxItems",
    "skipCount"
})
public class Paging {

    /** The max items. */
    @JsonProperty("maxItems")
    private String maxItems;
    
    /** The skip count. */
    @JsonProperty("skipCount")
    private String skipCount;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * Gets the max items.
     *
     * @return the max items
     */
    @JsonProperty("maxItems")
    public String getMaxItems() {
        return maxItems;
    }

    /**
     * Sets the max items.
     *
     * @param maxItems the new max items
     */
    @JsonProperty("maxItems")
    public void setMaxItems(String maxItems) {
        this.maxItems = maxItems;
    }

    /**
     * Gets the skip count.
     *
     * @return the skip count
     */
    @JsonProperty("skipCount")
    public String getSkipCount() {
        return skipCount;
    }

    /**
     * Sets the skip count.
     *
     * @param skipCount the new skip count
     */
    @JsonProperty("skipCount")
    public void setSkipCount(String skipCount) {
        this.skipCount = skipCount;
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
        sb.append(Paging.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("maxItems");
        sb.append('=');
        sb.append(((this.maxItems == null)?"<null>":this.maxItems));
        sb.append(',');
        sb.append("skipCount");
        sb.append('=');
        sb.append(((this.skipCount == null)?"<null>":this.skipCount));
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
        result = ((result* 31)+((this.maxItems == null)? 0 :this.maxItems.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.skipCount == null)? 0 :this.skipCount.hashCode()));
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
        if ((other instanceof Paging) == false) {
            return false;
        }
        Paging rhs = ((Paging) other);
        return ((((this.maxItems == rhs.maxItems)||((this.maxItems!= null)&&this.maxItems.equals(rhs.maxItems)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.skipCount == rhs.skipCount)||((this.skipCount!= null)&&this.skipCount.equals(rhs.skipCount))));
    }

}
