package com.example.model;

public class UserPreferencesRequest {
    private String username;
    private Preferences preferences;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public static class Preferences {
        private int trafficConvenience;
        private int serviceQuality;
        private int visitorFlowRate;
        private int culturalAtmosphere;
        private int naturalLandscape;
        private int pricePerformanceRatio;

        // Getters and setters
        public int getTrafficConvenience() {
            return trafficConvenience;
        }

        public void setTrafficConvenience(int trafficConvenience) {
            this.trafficConvenience = trafficConvenience;
        }

        public int getServiceQuality() {
            return serviceQuality;
        }

        public void setServiceQuality(int serviceQuality) {
            this.serviceQuality = serviceQuality;
        }

        public int getVisitorFlowRate() {
            return visitorFlowRate;
        }

        public void setVisitorFlowRate(int visitorFlowRate) {
            this.visitorFlowRate = visitorFlowRate;
        }

        public int getCulturalAtmosphere() {
            return culturalAtmosphere;
        }

        public void setCulturalAtmosphere(int culturalAtmosphere) {
            this.culturalAtmosphere = culturalAtmosphere;
        }

        public int getNaturalLandscape() {
            return naturalLandscape;
        }

        public void setNaturalLandscape(int naturalLandscape) {
            this.naturalLandscape = naturalLandscape;
        }

        public int getPricePerformanceRatio() {
            return pricePerformanceRatio;
        }

        public void setPricePerformanceRatio(int pricePerformanceRatio) {
            this.pricePerformanceRatio = pricePerformanceRatio;
        }
    }
}

