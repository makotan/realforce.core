package com.makotan.realforce.core.client;

import com.makotan.realforce.core.RealForceBase;
import com.makotan.realforce.core.CallServerInfo;
import com.sun.istack.internal.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 17:31
 */
public abstract class RealForceClientBase<Server,ID> extends RealForceBase<Server> implements RealForceClient<Server,ID> {

    public long toHashCode(@NotNull ID id) {
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-256");
            byte[] digest = sha1.digest(String.valueOf(id).getBytes());
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
    public CallServerInfo<Server> getServerInfo(ID id) {
        long key = toHashCode(id);
        TreeMap<Long,CallServerInfo<Server>> cr = currentRing;
        if (cr == null) {
            return null;
        }
        Map.Entry<Long, CallServerInfo<Server>> lowerEntry = cr.lowerEntry(key);
        if (lowerEntry == null) {
            lowerEntry = cr.lastEntry();
        }
        if (lowerEntry == null) {
            return null;
        }
        return lowerEntry.getValue();
    }

}
