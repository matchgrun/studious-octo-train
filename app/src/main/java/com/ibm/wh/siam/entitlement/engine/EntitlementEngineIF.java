/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.engine;

import java.util.List;

import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentRequest;

/**
 * @author Match Grun
 *
 */
public interface EntitlementEngineIF {

    public List<Entitlement> processRequest( final EntitlementCurrentRequest request );

}
