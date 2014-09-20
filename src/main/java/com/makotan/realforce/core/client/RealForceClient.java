package com.makotan.realforce.core.client;

import com.makotan.realforce.core.CallServerInfo;
import com.makotan.realforce.core.RealForce;

import java.time.Instant;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 13:35
 */
public interface RealForceClient<Server,ID> extends RealForce<Server> {

    /**
     * データの持つIDから操作可能なServerInfoを取得する
     * @param id データのID
     * @return 操作可能なServerInfo。ただし送信不可の場合もある。getServerInfoListに1つ以上データがあればnullは返らない
     */
    CallServerInfo<Server> getServerInfo(ID id);

}
