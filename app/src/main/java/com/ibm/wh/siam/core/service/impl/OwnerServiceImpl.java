/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.CoreCommonDAO;
import com.ibm.wh.siam.core.dao.dto.OwnerDetail;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.owner.OwnerDetailResponse;
import com.ibm.wh.siam.core.response.owner.OwnerTypeResponse;
import com.ibm.wh.siam.core.service.OwnerService;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class OwnerServiceImpl
extends BaseSiamService
implements OwnerService
{
    @Resource
    CoreCommonDAO coreCommonDao;

    private static final String ERRMSG_NOT_FOUND = "Owner Type not Found.";

    @SuppressWarnings("unused")
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Object.";
    private static final String ERRMSG_INVALID_OWNER_TYPE = "Invalid Owner Type.";

    private static final Logger logger = LoggerFactory.getLogger( OwnerServiceImpl.class );

    @Override
    public OwnerTypeResponse findOwnerType(final String ownerId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findOwnerType()");
            logger.info( "ownerId=" + ownerId );
        }

        ResponseStatus sts = null;
        OwnerTypeResponse response = new OwnerTypeResponse();
        response.setOwnerId(ownerId);

        String ownerType = coreCommonDao.identifyOwnerType(ownerId);
        response.setOwnerType(ownerType);
        if( StringUtils.isEmpty(ownerType) ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else if( ownerType.equalsIgnoreCase( SiamOwnerTypes.UNKNOWN ) ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    public static void validateOwnerType(
            final List<ValidationError> listErrors,
            final String ownerType )
    {
        if( ! SiamOwnerTypes.isValid(ownerType)) {
            ValidationError vError = new ValidationError( "ownerType", ERRMSG_INVALID_OWNER_TYPE );
            listErrors.add(vError);
        }
    }

    @Override
    public OwnerDetailResponse findOwnerDetail(String ownerId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findOwnerType()");
            logger.info( "ownerId=" + ownerId );
        }

        ResponseStatus sts = null;
        OwnerDetailResponse response = new OwnerDetailResponse();

        OwnerDetail ownerDetail = coreCommonDao.fetchOwnerDetail(ownerId);
        response.setOwnerDetail(ownerDetail);
        if( ownerDetail == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

}
