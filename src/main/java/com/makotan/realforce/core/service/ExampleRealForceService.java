package com.makotan.realforce.core.service;

import com.makotan.realforce.core.RealForceBase;
import com.makotan.realforce.core.CallServerInfo;
import com.makotan.realforce.core.client.RealForceClient;

import java.time.Instant;

/**
 * User: makotan
 * Date: 2014/09/15
 * Time: 17:32
 */
public abstract class ExampleRealForceService<Server,ID> extends RealForceBase<Server> implements RealForceClient<Server,ID> {
}
