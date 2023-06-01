package com.app.demo.service;

import java.util.List;

public class DirectionsApiResponse {
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private List<Leg> legs;

        public List<Leg> getLegs() {
            return legs;
        }
    }

    public static class Leg {
        private Duration duration;

        public Duration getDuration() {
            return duration;
        }
    }

    public static class Duration {
        private int value;
        private String text;

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }
}
