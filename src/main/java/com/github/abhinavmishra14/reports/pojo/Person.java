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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class Person.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "userName",
    "nodeRef",
    "enabled",
    "firstName",
    "lastName",
    "jobtitle",
    "organization",
    "organizationId",
    "location",
    "telephone",
    "mobile",
    "email",
    "companyaddress1",
    "companyaddress2",
    "companyaddress3",
    "companypostcode",
    "companytelephone",
    "companyfax",
    "companyemail",
    "skype",
    "instantmsg",
    "userStatus",
    "userStatusTime",
    "googleusername",
    "quota",
    "sizeCurrent",
    "emailFeedDisabled",
    "persondescription",
    "groups",
    "siteInfo"
})
public class Person {

    /** The url. */
    @JsonProperty("url")
    private String url;
    
    /** The user name. */
    @JsonProperty("userName")
    private String userName;
    
    /** The node ref. */
    @JsonProperty("nodeRef")
    private String nodeRef;
    
    /** The enabled. */
    @JsonProperty("enabled")
    private Boolean enabled;
    
    /** The first name. */
    @JsonProperty("firstName")
    private String firstName;
    
    /** The last name. */
    @JsonProperty("lastName")
    private String lastName;
    
    /** The jobtitle. */
    @JsonProperty("jobtitle")
    private Object jobtitle;
    
    /** The organization. */
    @JsonProperty("organization")
    private Object organization;
    
    /** The organization id. */
    @JsonProperty("organizationId")
    private Object organizationId;
    
    /** The location. */
    @JsonProperty("location")
    private Object location;
    
    /** The telephone. */
    @JsonProperty("telephone")
    private Object telephone;
    
    /** The mobile. */
    @JsonProperty("mobile")
    private Object mobile;
    
    /** The email. */
    @JsonProperty("email")
    private String email;
    
    /** The companyaddress1. */
    @JsonProperty("companyaddress1")
    private Object companyaddress1;
    
    /** The companyaddress2. */
    @JsonProperty("companyaddress2")
    private Object companyaddress2;
    
    /** The companyaddress3. */
    @JsonProperty("companyaddress3")
    private Object companyaddress3;
    
    /** The companypostcode. */
    @JsonProperty("companypostcode")
    private Object companypostcode;
    
    /** The companytelephone. */
    @JsonProperty("companytelephone")
    private Object companytelephone;
    
    /** The companyfax. */
    @JsonProperty("companyfax")
    private Object companyfax;
    
    /** The companyemail. */
    @JsonProperty("companyemail")
    private Object companyemail;
    
    /** The skype. */
    @JsonProperty("skype")
    private Object skype;
    
    /** The instantmsg. */
    @JsonProperty("instantmsg")
    private Object instantmsg;
    
    /** The user status. */
    @JsonProperty("userStatus")
    private Object userStatus;
    
    /** The user status time. */
    @JsonProperty("userStatusTime")
    private Object userStatusTime;
    
    /** The googleusername. */
    @JsonProperty("googleusername")
    private Object googleusername;
    
    /** The quota. */
    @JsonProperty("quota")
    private Integer quota;
    
    /** The size current. */
    @JsonProperty("sizeCurrent")
    private Integer sizeCurrent;
    
    /** The email feed disabled. */
    @JsonProperty("emailFeedDisabled")
    private Boolean emailFeedDisabled;
    
    /** The persondescription. */
    @JsonProperty("persondescription")
    private Object persondescription;
    
    /** The groups. */
    @JsonProperty("groups")
    private List<Group> groups = null;
    
    @JsonProperty("siteInfo")
    private List<SiteInfo> siteInfo = null;
    
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
     * Gets the user name.
     *
     * @return the user name
     */
    @JsonProperty("userName")
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the user name
     */
    @JsonProperty("userName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the node ref.
     *
     * @return the node ref
     */
    @JsonProperty("nodeRef")
    public String getNodeRef() {
        return nodeRef;
    }

    /**
     * Sets the node ref.
     *
     * @param nodeRef the node ref
     */
    @JsonProperty("nodeRef")
    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    /**
     * Gets the enabled.
     *
     * @return the enabled
     */
    @JsonProperty("enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the enabled
     */
    @JsonProperty("enabled")
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the first name
     */
    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the last name
     */
    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the jobtitle.
     *
     * @return the jobtitle
     */
    @JsonProperty("jobtitle")
    public Object getJobtitle() {
        return jobtitle;
    }

    /**
     * Sets the jobtitle.
     *
     * @param jobtitle the jobtitle
     */
    @JsonProperty("jobtitle")
    public void setJobtitle(Object jobtitle) {
        this.jobtitle = jobtitle;
    }

    /**
     * Gets the organization.
     *
     * @return the organization
     */
    @JsonProperty("organization")
    public Object getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     *
     * @param organization the organization
     */
    @JsonProperty("organization")
    public void setOrganization(Object organization) {
        this.organization = organization;
    }

    /**
     * Gets the organization id.
     *
     * @return the organization id
     */
    @JsonProperty("organizationId")
    public Object getOrganizationId() {
        return organizationId;
    }

    /**
     * Sets the organization id.
     *
     * @param organizationId the organization id
     */
    @JsonProperty("organizationId")
    public void setOrganizationId(Object organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    @JsonProperty("location")
    public Object getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location the location
     */
    @JsonProperty("location")
    public void setLocation(Object location) {
        this.location = location;
    }

    /**
     * Gets the telephone.
     *
     * @return the telephone
     */
    @JsonProperty("telephone")
    public Object getTelephone() {
        return telephone;
    }

    /**
     * Sets the telephone.
     *
     * @param telephone the telephone
     */
    @JsonProperty("telephone")
    public void setTelephone(Object telephone) {
        this.telephone = telephone;
    }

    /**
     * Gets the mobile.
     *
     * @return the mobile
     */
    @JsonProperty("mobile")
    public Object getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile.
     *
     * @param mobile the mobile
     */
    @JsonProperty("mobile")
    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the companyaddress1.
     *
     * @return the companyaddress1
     */
    @JsonProperty("companyaddress1")
    public Object getCompanyaddress1() {
        return companyaddress1;
    }

    /**
     * Sets the companyaddress1.
     *
     * @param companyaddress1 the companyaddress1
     */
    @JsonProperty("companyaddress1")
    public void setCompanyaddress1(Object companyaddress1) {
        this.companyaddress1 = companyaddress1;
    }

    /**
     * Gets the companyaddress2.
     *
     * @return the companyaddress2
     */
    @JsonProperty("companyaddress2")
    public Object getCompanyaddress2() {
        return companyaddress2;
    }

    /**
     * Sets the companyaddress2.
     *
     * @param companyaddress2 the companyaddress2
     */
    @JsonProperty("companyaddress2")
    public void setCompanyaddress2(Object companyaddress2) {
        this.companyaddress2 = companyaddress2;
    }

    /**
     * Gets the companyaddress3.
     *
     * @return the companyaddress3
     */
    @JsonProperty("companyaddress3")
    public Object getCompanyaddress3() {
        return companyaddress3;
    }

    /**
     * Sets the companyaddress3.
     *
     * @param companyaddress3 the companyaddress3
     */
    @JsonProperty("companyaddress3")
    public void setCompanyaddress3(Object companyaddress3) {
        this.companyaddress3 = companyaddress3;
    }

    /**
     * Gets the companypostcode.
     *
     * @return the companypostcode
     */
    @JsonProperty("companypostcode")
    public Object getCompanypostcode() {
        return companypostcode;
    }

    /**
     * Sets the companypostcode.
     *
     * @param companypostcode the companypostcode
     */
    @JsonProperty("companypostcode")
    public void setCompanypostcode(Object companypostcode) {
        this.companypostcode = companypostcode;
    }

    /**
     * Gets the companytelephone.
     *
     * @return the companytelephone
     */
    @JsonProperty("companytelephone")
    public Object getCompanytelephone() {
        return companytelephone;
    }

    /**
     * Sets the companytelephone.
     *
     * @param companytelephone the companytelephone
     */
    @JsonProperty("companytelephone")
    public void setCompanytelephone(Object companytelephone) {
        this.companytelephone = companytelephone;
    }

    /**
     * Gets the companyfax.
     *
     * @return the companyfax
     */
    @JsonProperty("companyfax")
    public Object getCompanyfax() {
        return companyfax;
    }

    /**
     * Sets the companyfax.
     *
     * @param companyfax the companyfax
     */
    @JsonProperty("companyfax")
    public void setCompanyfax(Object companyfax) {
        this.companyfax = companyfax;
    }

    /**
     * Gets the companyemail.
     *
     * @return the companyemail
     */
    @JsonProperty("companyemail")
    public Object getCompanyemail() {
        return companyemail;
    }

    /**
     * Sets the companyemail.
     *
     * @param companyemail the companyemail
     */
    @JsonProperty("companyemail")
    public void setCompanyemail(Object companyemail) {
        this.companyemail = companyemail;
    }

    /**
     * Gets the skype.
     *
     * @return the skype
     */
    @JsonProperty("skype")
    public Object getSkype() {
        return skype;
    }

    /**
     * Sets the skype.
     *
     * @param skype the skype
     */
    @JsonProperty("skype")
    public void setSkype(Object skype) {
        this.skype = skype;
    }

    /**
     * Gets the instantmsg.
     *
     * @return the instantmsg
     */
    @JsonProperty("instantmsg")
    public Object getInstantmsg() {
        return instantmsg;
    }

    /**
     * Sets the instantmsg.
     *
     * @param instantmsg the instantmsg
     */
    @JsonProperty("instantmsg")
    public void setInstantmsg(Object instantmsg) {
        this.instantmsg = instantmsg;
    }

    /**
     * Gets the user status.
     *
     * @return the user status
     */
    @JsonProperty("userStatus")
    public Object getUserStatus() {
        return userStatus;
    }

    /**
     * Sets the user status.
     *
     * @param userStatus the user status
     */
    @JsonProperty("userStatus")
    public void setUserStatus(Object userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * Gets the user status time.
     *
     * @return the user status time
     */
    @JsonProperty("userStatusTime")
    public Object getUserStatusTime() {
        return userStatusTime;
    }

    /**
     * Sets the user status time.
     *
     * @param userStatusTime the user status time
     */
    @JsonProperty("userStatusTime")
    public void setUserStatusTime(Object userStatusTime) {
        this.userStatusTime = userStatusTime;
    }

    /**
     * Gets the googleusername.
     *
     * @return the googleusername
     */
    @JsonProperty("googleusername")
    public Object getGoogleusername() {
        return googleusername;
    }

    /**
     * Sets the googleusername.
     *
     * @param googleusername the googleusername
     */
    @JsonProperty("googleusername")
    public void setGoogleusername(Object googleusername) {
        this.googleusername = googleusername;
    }

    /**
     * Gets the quota.
     *
     * @return the quota
     */
    @JsonProperty("quota")
    public Integer getQuota() {
        return quota;
    }

    /**
     * Sets the quota.
     *
     * @param quota the quota
     */
    @JsonProperty("quota")
    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    /**
     * Gets the size current.
     *
     * @return the size current
     */
    @JsonProperty("sizeCurrent")
    public Integer getSizeCurrent() {
        return sizeCurrent;
    }

    /**
     * Sets the size current.
     *
     * @param sizeCurrent the size current
     */
    @JsonProperty("sizeCurrent")
    public void setSizeCurrent(Integer sizeCurrent) {
        this.sizeCurrent = sizeCurrent;
    }

    /**
     * Gets the email feed disabled.
     *
     * @return the email feed disabled
     */
    @JsonProperty("emailFeedDisabled")
    public Boolean getEmailFeedDisabled() {
        return emailFeedDisabled;
    }

    /**
     * Sets the email feed disabled.
     *
     * @param emailFeedDisabled the email feed disabled
     */
    @JsonProperty("emailFeedDisabled")
    public void setEmailFeedDisabled(Boolean emailFeedDisabled) {
        this.emailFeedDisabled = emailFeedDisabled;
    }

    /**
     * Gets the persondescription.
     *
     * @return the persondescription
     */
    @JsonProperty("persondescription")
    public Object getPersondescription() {
        return persondescription;
    }

    /**
     * Sets the persondescription.
     *
     * @param persondescription the persondescription
     */
    @JsonProperty("persondescription")
    public void setPersondescription(Object persondescription) {
        this.persondescription = persondescription;
    }

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    @JsonProperty("groups")
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Sets the groups.
     *
     * @param groups the groups
     */
    @JsonProperty("groups")
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     * Gets the site info.
     *
     * @return the site info
     */
    @JsonProperty("siteInfo")
    public List<SiteInfo> getSiteInfo() {
		return siteInfo;
	}

	/**
	 * Sets the site info.
	 *
	 * @param siteInfo the site info
	 */
    @JsonProperty("siteInfo")
	public void setSiteInfo(List<SiteInfo> siteInfo) {
    	if (siteInfo == null) {
    		this.siteInfo = new ArrayList<SiteInfo>();
    	} else {
		  this.siteInfo = siteInfo;
    	}
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
