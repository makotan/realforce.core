package com.makotan.realforce.core;

import java.time.Instant;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 13:09
 */
public class TimeStatus {
    Instant from;
    ServerStatus status;
    OperationType operationType;

    public TimeStatus(Instant from , ServerStatus status , OperationType operationType) {
        this.from = from;
        this.status = status;
        this.operationType = operationType;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "TimeStatus{" +
                "from=" + from +
                ", status=" + status +
                ", operationType=" + operationType +
                '}';
    }
}
