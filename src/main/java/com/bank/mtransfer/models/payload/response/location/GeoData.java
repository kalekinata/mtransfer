package com.bank.mtransfer.models.payload.response.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoData{
    @SerializedName("suggestions")
    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public static class Suggestion {
        @SerializedName("value")
        private String value;

        @SerializedName("unrestricted_value")
        private String unrestrictedValue;

        @SerializedName("data")
        private Data data;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnrestrictedValue() {
            return unrestrictedValue;
        }

        public void setUnrestrictedValue(String unrestrictedValue) {
            this.unrestrictedValue = unrestrictedValue;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public static class Data {
        @SerializedName("postal_code")
        private String postalCode;

        @SerializedName("country")
        private String country;

        @SerializedName("region")
        private String region;

        @SerializedName("region_with_type")
        private  String region_with_type;

        // Добавьте сюда все оставшиеся поля

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegion_with_type() {
            return region_with_type;
        }

        public void setRegion_with_type(String region_with_type) {
            this.region_with_type = region_with_type;
        }
    }
}