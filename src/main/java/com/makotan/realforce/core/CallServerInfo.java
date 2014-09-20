package com.makotan.realforce.core;

import com.makotan.realforce.core.OperationType;
import com.makotan.realforce.core.ServerInfo;
import com.makotan.realforce.core.ServerStatus;
import com.makotan.realforce.core.TimeStatus;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 17:59
 */
public class CallServerInfo<Server> {
    final Server server;
    final ServerStatus status;
    final OperationType operationType;


    public CallServerInfo(Server server,ServerStatus status , OperationType operationType) {
        this.server = server;
        this.status = status;
        this.operationType = operationType;
    }

    public CallServerInfo(ServerInfo<Server> info , TimeStatus ts) {
        this(info.getServer() , ts.getStatus() , ts.getOperationType());
    }

    public Server getServer() {
        return server;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        return "CallServerInfo{" +
                "server=" + server +
                ", status=" + status +
                ", operationType=" + operationType +
                '}';
    }

}
