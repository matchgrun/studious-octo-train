/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.engine;

import java.util.Date;

import com.ibm.wh.siam.entitlement.dto.Entitlement;

/**
 * Implements an entitlement range filter.
 * @author Match Grun
 *
 */
public class EntitlementRangeFilter {

    long timeCurrent = System.currentTimeMillis();

    /**
     * Test whether specified entitlement is in range.
     * @param ent   Entitlement to check.
     * @return  <i>true<i> Valid entitlement.
     */
    public boolean checkEntitlementRange( final Entitlement ent ) {
        Date dtStart = ent.getStartDate();
        Date dtEnd = ent.getEndDate();

        if( dtStart == null ) {
            if( dtEnd == null ) {
                return true;
            }
            long tEnd = dtEnd.getTime();
            if( tEnd >= timeCurrent ) {
                return true;
            }
            return false;
        }

        long tStart = dtStart.getTime();
        if( tStart <= timeCurrent ) {
            if( dtEnd == null ) {
                return true;
            }
            long tEnd = dtEnd.getTime();
            if( tEnd >= timeCurrent ) {
                return true;
            }
        }

        return false;
    }

}
