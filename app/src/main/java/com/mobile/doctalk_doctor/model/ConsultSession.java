package com.mobile.doctalk_doctor.model;

import java.util.Date;

public class ConsultSession {
    private Date timeStart;
    private Date timeEnd;
    private int requestConsultId;
    private int doctorId;
    private int starRating;
    private String comment;

    public ConsultSession() {
    }

    public ConsultSession(Date timeStart, Date timeEnd, int requestConsultId, int doctorId, int starRating, String comment) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.requestConsultId = requestConsultId;
        this.doctorId = doctorId;
        this.starRating = starRating;
        this.comment = comment;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getRequestConsultId() {
        return requestConsultId;
    }

    public void setRequestConsultId(int requestConsultId) {
        this.requestConsultId = requestConsultId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
