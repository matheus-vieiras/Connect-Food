package com.fiap.connectfood.dto;

import java.util.List;

public class LocationDto {

    private List<String> destination_addresses;
    private List<String> origin_addresses;
    private List<RowDto> rows;
    private String status;

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<RowDto> getRows() {
        return rows;
    }

    public void setRows(List<RowDto> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
