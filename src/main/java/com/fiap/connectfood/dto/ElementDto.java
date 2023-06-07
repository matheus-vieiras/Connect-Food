package com.fiap.connectfood.dto;

public class ElementDto {

    private DistanceDto distance;
    private DurationDto duration;
    private String status;

    public DistanceDto getDistance() {
        return distance;
    }

    public void setDistance(DistanceDto distance) {
        this.distance = distance;
    }

    public DurationDto getDuration() {
        return duration;
    }

    public void setDuration(DurationDto duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
