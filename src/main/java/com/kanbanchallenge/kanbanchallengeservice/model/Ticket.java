package com.kanbanchallenge.kanbanchallengeservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    /**
     * Id is used to account for each unique ticket. Without it we would not know weather the ticket
     * requires updating or a new ticket needs to be created.
     */
    String id = null;
    String description;
    String createdBy;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createdDate;
    SwimmingLane assignedSwimmingLane;

    public Ticket() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setAssignedSwimmingLane(SwimmingLane assignedSwimmingLane) {
        this.assignedSwimmingLane = assignedSwimmingLane;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public SwimmingLane getAssignedSwimmingLane() {
        return assignedSwimmingLane;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
