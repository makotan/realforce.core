package com.makotan.realforce.core.client;

import com.makotan.realforce.core.*;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * User: makotan
 * Date: 2014/09/19
 * Time: 6:28
 */
public class RealForceClientBaseTest {

    public static class RealForceClientTest extends RealForceClientBase<String,Long> {
        @Override
        public boolean updateServerInfoList() {
            return false;
        }

        public void setServerInfoList(List<ServerInfo<String>> serverInfoList) {
            this.serverInfoList = serverInfoList;
        }
    }

    RealForceClientTest realForceBase = new RealForceClientTest();

    @Before
    public void setup() {
        realForceBase = new RealForceClientTest();

        List<ServerInfo<String>> serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("12.34.23.56" , -3*60));
        serverInfoList.add(createServerInfo("12.34.147.98" , -3*60));
        serverInfoList.add(createServerInfo("12.34.9.0" , -3*60));
        realForceBase.setServerInfoList(serverInfoList);

    }

    ServerInfo<String> createServerInfo(String name , int shiftSec) {
        List<TimeStatus> timeStatusList = new ArrayList<>();
        timeStatusList.add(new TimeStatus(Instant.now().plus(shiftSec, ChronoUnit.SECONDS), ServerStatus.Booting, OperationType.Auto));
        timeStatusList.add(new TimeStatus(Instant.now().plus(60+shiftSec, ChronoUnit.SECONDS), ServerStatus.StartWait, OperationType.Auto));
        timeStatusList.add(new TimeStatus(Instant.now().plus(2*60+shiftSec, ChronoUnit.SECONDS), ServerStatus.Running, OperationType.Auto));
        timeStatusList.add(new TimeStatus(Instant.now().plus(7*60+shiftSec, ChronoUnit.SECONDS), ServerStatus.StopWait, OperationType.Auto));
        timeStatusList.add(new TimeStatus(Instant.now().plus(8*60+shiftSec, ChronoUnit.SECONDS), ServerStatus.Stop, OperationType.Auto));
        ServerInfo<String> serverInfo = new ServerInfo<String>(name, timeStatusList);
        return serverInfo;
    }

    @Test
    public void test_getServerInfo() {
        realForceBase.processServerInfoRole();
        Map<String,Long> counter = new HashMap<>();
        for (long i=0 ; i < 1000_000_000 ; i+= Math.random()*100) {
            CallServerInfo<String> serverInfo = realForceBase.getServerInfo(i);
            Long c = counter.getOrDefault(serverInfo.getServer() , 0L);
            counter.put(serverInfo.getServer(), c + 1);
        }
        counter.forEach((s,l) -> System.out.println("count:" + l + "/server:" + s));
    }

}
