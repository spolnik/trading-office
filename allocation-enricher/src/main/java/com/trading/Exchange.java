package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class Exchange implements Serializable {

    private String country;
    private String countryCode;
    private String mic;
    private String name;
    private String acronym;
    private String city;
    private String website;
    private String comments;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("country", country)
                .add("countryCode", countryCode)
                .add("mic", mic)
                .add("name", name)
                .add("acronym", acronym)
                .add("city", city)
                .add("website", website)
                .add("comments", comments)
                .toString();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Exchange that = (Exchange) o;

        return Objects.equals(mic, that.mic);

    }

    @Override
    public int hashCode() {
        return Objects.hash(mic);
    }
}
