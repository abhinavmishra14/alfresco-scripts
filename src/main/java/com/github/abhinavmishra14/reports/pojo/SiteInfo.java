/*
 * Created By: Abhinav Kumar Mishra
 * Copyright &copy; 2017. Abhinav Kumar Mishra. 
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
package com.github.abhinavmishra14.reports.pojo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class SiteInfo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "sitePreset",
    "shortName",
    "title",
    "description",
    "node",
    "tagScope",
    "siteRole",
    "isPublic",
    "visibility"
})
public class SiteInfo {

    /** The url. */
    @JsonProperty("url")
    private String url;
    
    /** The site preset. */
    @JsonProperty("sitePreset")
    private String sitePreset;
    
    /** The short name. */
    @JsonProperty("shortName")
    private String shortName;
    
    /** The title. */
    @JsonProperty("title")
    private String title;
    
    /** The description. */
    @JsonProperty("description")
    private String description;
    
    /** The node. */
    @JsonProperty("node")
    private String node;
    
    /** The tag scope. */
    @JsonProperty("tagScope")
    private String tagScope;
    
    /** The site role. */
    @JsonProperty("siteRole")
    private String siteRole;
    
    /** The is public. */
    @JsonProperty("isPublic")
    private Boolean isPublic;
    
    /** The visibility. */
    @JsonProperty("visibility")
    private String visibility;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Gets the url.
     *
     * @return the url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url the url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the site preset.
     *
     * @return the site preset
     */
    @JsonProperty("sitePreset")
    public String getSitePreset() {
        return sitePreset;
    }

    /**
     * Sets the site preset.
     *
     * @param sitePreset the site preset
     */
    @JsonProperty("sitePreset")
    public void setSitePreset(String sitePreset) {
        this.sitePreset = sitePreset;
    }

    /**
     * Gets the short name.
     *
     * @return the short name
     */
    @JsonProperty("shortName")
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets the short name.
     *
     * @param shortName the short name
     */
    @JsonProperty("shortName")
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the node.
     *
     * @return the node
     */
    @JsonProperty("node")
    public String getNode() {
        return node;
    }

    /**
     * Sets the node.
     *
     * @param node the node
     */
    @JsonProperty("node")
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * Gets the tag scope.
     *
     * @return the tag scope
     */
    @JsonProperty("tagScope")
    public String getTagScope() {
        return tagScope;
    }

    /**
     * Sets the tag scope.
     *
     * @param tagScope the tag scope
     */
    @JsonProperty("tagScope")
    public void setTagScope(String tagScope) {
        this.tagScope = tagScope;
    }

    /**
     * Gets the site role.
     *
     * @return the site role
     */
    @JsonProperty("siteRole")
    public String getSiteRole() {
        return siteRole;
    }

    /**
     * Sets the site role.
     *
     * @param siteRole the site role
     */
    @JsonProperty("siteRole")
    public void setSiteRole(String siteRole) {
        this.siteRole = siteRole;
    }

    /**
     * Gets the is public.
     *
     * @return the checks if is public
     */
    @JsonProperty("isPublic")
    public Boolean getIsPublic() {
        return isPublic;
    }

    /**
     * Sets the is public.
     *
     * @param isPublic the checks if is public
     */
    @JsonProperty("isPublic")
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Gets the visibility.
     *
     * @return the visibility
     */
    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility.
     *
     * @param visibility the visibility
     */
    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
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
}
