package com.makotan.realforce.core;

import com.sun.istack.internal.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * User: makotan
 * Date: 2014/09/16
 * Time: 7:00
 */
public abstract class RealForceBase<Server> implements RealForce<Server> {

    protected List<ServerInfo<Server>> serverInfoList = new ArrayList<>();
    protected volatile TreeMap<Long,CallServerInfo<Server>> currentRing = new TreeMap<>();
    protected Instant lastUpdateTime = Instant.MIN;

    protected long toServerHashCode(@NotNull String serverId) {
        if (serverId == null) {
            return -1;
        }
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha1.digest(serverId.getBytes());
            long hash = 0;
            for (byte aDigest : digest) {
                hash = hash * 256 + aDigest;
                if (hash > getMaxHash()) {
                    break;
                }
            }
            return hash % getMaxHash();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<ServerInfo<Server>> getServerInfoList() {
        return serverInfoList;
    }

    protected List<ServerInfo<Server>> getActiveServerInfoList(Instant instant,List<ServerInfo<Server>> allList) {
        List<ServerInfo<Server>> activeList = new ArrayList<>();

        for (ServerInfo<Server> si : allList) {
            Optional<TimeStatus> current = findTimeStatus(si,instant);

            if (current.isPresent()) {
                TimeStatus ts = current.get();
                if (ts.getStatus() != ServerStatus.Booting && ts.getStatus() != ServerStatus.Stop) {
                    activeList.add(si);
                }
            }
        }
        return activeList;
    }

    protected Optional<TimeStatus> findTimeStatus(ServerInfo<Server> serverInfo,Instant instant) {
        Optional<TimeStatus> current = Optional.empty();
        for (TimeStatus ts : serverInfo.getTimeStatusList()) {
            if (ts.getFrom().compareTo(instant) <= 0) {
                current = Optional.of(ts);
            }
        }
        return current;
    }

    protected List<CallServerInfo<Server>> convertCallServerInfoList(List<ServerInfo<Server>> activeList,Instant instant) {
        List<CallServerInfo<Server>> callServerInfos = new ArrayList<>();
        for (ServerInfo<Server> a: activeList) {
            CallServerInfo<Server> csi = new CallServerInfo<Server>(a,findTimeStatus(a,instant).get());
            callServerInfos.add(csi);
        }
        return callServerInfos;
    }

    protected int getServerNameExtendCount() {return 6;}

    protected TreeMap<Long,CallServerInfo<Server>> createCallRing(List<ServerInfo<Server>> activeList , Instant instant , int serverHashCount) {
        List<CallServerInfo<Server>> callSIs = convertCallServerInfoList(activeList,instant);
        TreeMap<Long,CallServerInfo<Server>> hccsi = new TreeMap<>();
        for (int i=0 ; i < serverHashCount ; i++) {
            for (CallServerInfo<Server> csi : callSIs) {
                StringBuilder sb = new StringBuilder(String.valueOf(csi.getServer()));
                for (int j=0 ; j < getServerNameExtendCount() ; j++) { // 文字列の延長
                    sb.append("-").append(i);
                }
                String hd = sb.toString();
                long hc = toServerHashCode(hd);
                hccsi.put(hc,csi);
            }
        }
        return hccsi;
    }

    protected boolean inInterval(Instant checkTime , Instant lastUpdateTime , int interval) {
        Instant ii = checkTime.plus(interval, ChronoUnit.SECONDS);
        if (lastUpdateTime.compareTo(ii) <= 0) {
            return true;
        }
        return false;
    }


    @Override
    public void processServerInfoRole(Instant checkTime) {
        if (inInterval(checkTime, lastUpdateTime, cacheCalcInterval())) {
            List<ServerInfo<Server>> asil = getActiveServerInfoList(checkTime, getServerInfoList());
            TreeMap<Long, CallServerInfo<Server>> callRing = createCallRing(asil, checkTime, getServerHashCount());
            synchronized (this) {
                this.currentRing = callRing;
            }
        }
    }


}
