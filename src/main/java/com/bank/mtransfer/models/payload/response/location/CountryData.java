package com.bank.mtransfer.models.payload.response.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryData {

    @SerializedName("suggestions")
    private List<CountryData.Suggestion> suggestions;

    public List<CountryData.Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<CountryData.Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public static class Suggestion {
        @SerializedName("value")
        private String value;

        @SerializedName("unrestricted_value")
        private String unrestrictedValue;

        @SerializedName("data")
        private CountryData.Data data;

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

        public CountryData.Data getData() {
            return data;
        }

        public void setData(CountryData.Data data) {
            this.data = data;
        }
    }

    public static class Data {
        @SerializedName("code")
        private String code;
        @SerializedName("alfa2")
        private String alfa2;
        @SerializedName("alfa3")
        private String alfa3;
        @SerializedName("name_short")
        private String name_short;
        @SerializedName("name")
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAlfa2() {
            return alfa2;
        }

        public void setAlfa2(String alfa2) {
            this.alfa2 = alfa2;
        }

        public String getAlfa3() {
            return alfa3;
        }

        public void setAlfa3(String alfa3) {
            this.alfa3 = alfa3;
        }

        public String getName_short() {
            return name_short;
        }

        public void setName_short(String name_short) {
            this.name_short = name_short;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
