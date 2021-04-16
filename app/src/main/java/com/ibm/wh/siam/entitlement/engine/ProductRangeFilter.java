/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.engine;

import java.util.Date;

import com.ibm.wh.siam.core.dao.dto.ProductRange;

/**
 * Implements an entitlement range filter.
 * @author Match Grun
 *
 */
public class ProductRangeFilter {

    long timeCurrent = System.currentTimeMillis();

    /**
     * Test whether specified product is in range.
     * @param prod  Product to check.
     * @return  <i>true<i> Valid entitlement.
     */
    public boolean checkProductRange( final ProductRange prod ) {
        Date dtStart = prod.getStartDate();
        Date dtEnd = prod.getEndDate();

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
