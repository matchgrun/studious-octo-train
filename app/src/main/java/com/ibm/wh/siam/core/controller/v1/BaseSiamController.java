/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.exc.UsernameException;
import com.ibm.wh.siam.core.util.RecordUpdaterUtil;

/**
 * @author Match Grun
 *
 */
public abstract class BaseSiamController {

    private static final Logger logger = LoggerFactory.getLogger( BaseSiamController.class );

    protected RecordUpdaterIF fetchUpdater( final HttpHeaders headers )
    throws UsernameException
    {
        RecordUpdaterIF updater = RecordUpdaterUtil.createRecordUpdater(headers);
        if( logger.isInfoEnabled() ) {
            logger.info("updater=" + updater );
        }
        return updater;
    }

}
