package com.mycompany.app.Dto;

public class MachineryReservationDTO {
    private Long id;
    private MachineryDTO machinery;
    private ReservationDTO reservation;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MachineryDTO getMachinery() {
        return this.machinery;
    }

    public void setMachinery(MachineryDTO machinery) {
        this.machinery = machinery;
    }

    public ReservationDTO getReservation() {
        return this.reservation;
    }

    public void setReservation(ReservationDTO reservation) {
        this.reservation = reservation;
    }

}