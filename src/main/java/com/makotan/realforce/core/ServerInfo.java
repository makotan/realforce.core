package com.makotan.realforce.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 13:02
 */
public class ServerInfo<Server> {
    final Server server;
    final List<TimeStatus> timeStatusList;


    public ServerInfo(Server server , List<TimeStatus> timeStatusList) {
        this.server = server;
        this.timeStatusList = timeStatusList;
    }

    public ServerInfo(Server server) {
        this.server = server;
        this.timeStatusList = new ArrayList<>();
    }

    public Server getServer() {
        return server;
    }

    public ServerInfo<Server> withServer(Server server) {
        ServerInfo<Server> info = new ServerInfo<Server>(server,timeStatusList);
        return info;
    }

    public List<TimeStatus> getTimeStatusList() {
        return Collections.unmodifiableList(timeStatusList);
    }

    public ServerInfo<Server> withTimeStatusList(List<TimeStatus> timeStatusList) {
        ServerInfo<Server> info = new ServerInfo<Server>(server,timeStatusList);
        return info;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "server=" + server +
                ", timeStatusList=" + timeStatusList +
                '}';
    }
}
