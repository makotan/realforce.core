package com.makotan.realforce.core;

import java.time.Instant;
import java.util.List;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 12:59
 */
public interface RealForce<Server> {
    /**
     * 最新のServerInfoのListのキャッシュを返す。readOnly
     * @return ServerInfoのList
     */
    List<ServerInfo<Server>> getServerInfoList();

    /**
     * ServerInfoのListを最新状態に更新する
     * @return 更新したとき true 更新しなかった(出来なかった)とき false
     */
    boolean updateServerInfoList();

    /**
     * ハッシュ値の最大を返す。サーバ全てを停止しない限り変更不可<br/>
     * service側サーバ台数が小規模の場合は256未満、大規模が前提ならもっと大きくする<br/>
     * 2のn乗-1になる様にすること<br/>
     * @return ハッシュ値の最大値
     */
    default int getMaxHash(){return 512;}

    /**
     * サーバ1台あたりのハッシュ生成数<br/>
     * サーバ台数*getServerHashCount() <<<< getMaxHash() である事が必須<br/>
     * 高頻度の変更は望ましくない<br/>
     * @return サーバ辺りのハッシュ生成数
     */
    default int getServerHashCount() {
        return 10;
    }

    /**
     * ハッシュ値の再計算インターバル(秒)
     * @return interval
     */
    default int cacheCalcInterval() {
        return 3;
    }


    default void processServerInfoRole() {
        processServerInfoRole(Instant.now());
    }

    void processServerInfoRole(Instant instant);

}
