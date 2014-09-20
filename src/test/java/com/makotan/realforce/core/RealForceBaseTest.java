package com.makotan.realforce.core;

import org.junit.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


/**
 * User: makotan
 * Date: 2014/09/16
 * Time: 20:31
 */
public class RealForceBaseTest {
    RealForceBase<String> realForceBase = new RealForceBase<String>() {
        @Override
        public boolean updateServerInfoList() {
            return false;
        }
    };

    @Before
    public void setup() {
        realForceBase = new RealForceBase<String>() {
            @Override
            public boolean updateServerInfoList() {
                return false;
            }
        };
        List<ServerInfo<String>> serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , 0));
        serverInfoList.add(createServerInfo("server2" , 0));
        serverInfoList.add(createServerInfo("server3" , 0));
        realForceBase.serverInfoList = serverInfoList;

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
    public void testToServerHashCode() {
        long aaa = realForceBase.toServerHashCode("aaa");
        assertNotNull(aaa);
        long n = realForceBase.toServerHashCode(null);
        assertEquals(-1 , n);
    }

    @Test
    public void test_getServerInfoList() {
        List<ServerInfo<String>> serverInfoList = realForceBase.getServerInfoList();
        assertNotNull(serverInfoList);
    }

    @Test
    public void test_findTimeStatus() {
        ServerInfo<String> info = createServerInfo("server1", 0);
        Optional<TimeStatus> status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Booting , status.get().getStatus());

        info = createServerInfo("server1", -1*60+1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Booting , status.get().getStatus());

        info = createServerInfo("server1", -1*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StartWait , status.get().getStatus());

        info = createServerInfo("server1", -1*60-1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StartWait , status.get().getStatus());

        info = createServerInfo("server1", -2*60+1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StartWait , status.get().getStatus());

        info = createServerInfo("server1", -2*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Running , status.get().getStatus());

        info = createServerInfo("server1", -2*60-1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Running , status.get().getStatus());

        info = createServerInfo("server1", -3*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Running , status.get().getStatus());

        info = createServerInfo("server1", -7*60+1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Running, status.get().getStatus());

        info = createServerInfo("server1", -7*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StopWait, status.get().getStatus());

        info = createServerInfo("server1", -7*60-1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StopWait, status.get().getStatus());

        info = createServerInfo("server1", -8*60+1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.StopWait , status.get().getStatus());

        info = createServerInfo("server1", -8*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Stop , status.get().getStatus());

        info = createServerInfo("server1", -8*60-1);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Stop , status.get().getStatus());

        info = createServerInfo("server1", -10*60);
        status = realForceBase.findTimeStatus(info, Instant.now());
        assertTrue(status.isPresent());
        assertEquals(ServerStatus.Stop , status.get().getStatus());
    }

    @Test
    public void test_getActiveServerInfoList() {
        List<ServerInfo<String>> list = realForceBase.getActiveServerInfoList(Instant.now(), realForceBase.getServerInfoList());
        assertEquals(0 , list.size());

        List<ServerInfo<String>> serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , -1*60));
        serverInfoList.add(createServerInfo("server2" , -2*60));
        serverInfoList.add(createServerInfo("server3" , 0));
        list = realForceBase.getActiveServerInfoList(Instant.now(), serverInfoList);
        assertEquals(2, list.size());

        serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , -1*60));
        serverInfoList.add(createServerInfo("server2" , -2*60));
        serverInfoList.add(createServerInfo("server3" , -7*60));
        list = realForceBase.getActiveServerInfoList(Instant.now(), serverInfoList);
        assertEquals(3, list.size());

        serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , -1*60));
        serverInfoList.add(createServerInfo("server2" , -2*60));
        serverInfoList.add(createServerInfo("server3" , -8*60));
        list = realForceBase.getActiveServerInfoList(Instant.now(), serverInfoList);
        assertEquals(2, list.size());

    }

    @Test
    public void test_convertCallServerInfoList() {

        List<ServerInfo<String>> serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , -1*60));
        serverInfoList.add(createServerInfo("server2" , -2*60));
        serverInfoList.add(createServerInfo("server3" , -7*60));
        List<ServerInfo<String>> list = realForceBase.getActiveServerInfoList(Instant.now(), serverInfoList);
        assertEquals(3, list.size());

        List<CallServerInfo<String>> callServerInfos = realForceBase.convertCallServerInfoList(list, Instant.now());
        assertEquals(callServerInfos.size() , 3);
    }

    @Test
    public void test_createCallRing() {
        List<ServerInfo<String>> serverInfoList = new ArrayList<>();
        serverInfoList.add(createServerInfo("server1" , -1*60));
        serverInfoList.add(createServerInfo("server2" , -2*60));
        serverInfoList.add(createServerInfo("server3" , -7*60));
        List<ServerInfo<String>> list = realForceBase.getActiveServerInfoList(Instant.now(), serverInfoList);
        TreeMap<Long, CallServerInfo<String>> callRing = realForceBase.createCallRing(list, Instant.now(), 8);
        assertNotNull(callRing);
        callRing.forEach((i, csi) -> System.out.println("i=" + i + "/ csi=" + csi));
        assertEquals(22 , callRing.size());
    }

    @Test
    public void test_inInterval() {
        boolean b = realForceBase.inInterval(Instant.now(), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(1), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(2), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(3), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(4), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(5), Instant.now(), 5);
        assertTrue(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(6), Instant.now(), 5);
        assertFalse(b);
        b = realForceBase.inInterval(Instant.now().minusSeconds(7), Instant.now(), 5);
        assertFalse(b);
    }

    @Test
    public void test_processServerInfoRole() {
        realForceBase.processServerInfoRole(Instant.now());
        realForceBase.processServerInfoRole(Instant.now().plusSeconds(1));
        realForceBase.processServerInfoRole(Instant.now().plusSeconds(2));
        realForceBase.processServerInfoRole(Instant.now().plusSeconds(3));
        for (int i=1; i < 100 ; i++) {
            realForceBase.processServerInfoRole(Instant.now().plus(i,ChronoUnit.MINUTES));
        }
    }

}
