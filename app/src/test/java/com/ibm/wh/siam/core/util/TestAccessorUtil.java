/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.core.dao.dto.AccessorDetail;
import com.ibm.wh.siam.core.dao.dto.OwnerDetail;

/**
 * @author Match Grun
 *
 */
public class TestAccessorUtil {

    private static final String TEST_OWNER_ID = "testOwnerId";
    private static final String TEST_OWNER_TYPE = "testOwnerType";
    private static final String TEST_OWNER_CODE = "testOwnerCode";
    private static final String TEST_OWNER_NAME = "testOwnerName";

    private static final Logger logger = LoggerFactory.getLogger( TestAccessorUtil.class );

    @Test
    public void testUtil01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil01()");
        }

        OwnerDetail ownerDetail = new OwnerDetail();
        ownerDetail.setOwnerId(TEST_OWNER_ID);
        ownerDetail.setOwnerType(TEST_OWNER_TYPE);
        ownerDetail.setOwnerCode(TEST_OWNER_CODE);
        ownerDetail.setOwnerName(TEST_OWNER_NAME);
        if( logger.isDebugEnabled() ) {
            logger.debug( "ownerDetail=" + ownerDetail );
        }

        AccessorDetail accessorDetail = AccessorUtil.createFromOwner(ownerDetail);
        if( logger.isDebugEnabled() ) {
            logger.debug( "accessorDetail=" + accessorDetail );
        }

        assertTrue( accessorDetail.getAccessorId().equals(TEST_OWNER_ID), "Failed test assignment: Owner_ID" );
        assertTrue( accessorDetail.getAccessorType().equals(TEST_OWNER_TYPE), "Failed test assignment: Owner_Type" );
        assertTrue( accessorDetail.getAccessorCode().equals(TEST_OWNER_CODE), "Failed test assignment: Owner_Code" );
        assertTrue( accessorDetail.getAccessorName().equals(TEST_OWNER_NAME), "Failed test assignment: Owner_Name" );
    }

}
