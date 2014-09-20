package com.makotan.realforce.core.service;

import com.makotan.realforce.core.RealForce;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 13:42
 */
public interface RealForceService<Server,Message> extends RealForce<Server> {
    /**
     * ServerInfoのルールに従って処理を行う<br/>
     * このメソッドは比較的高頻度で呼ばれるので同期化必須
     */
    void processServerInfoRule();
}
