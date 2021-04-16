/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.CoreCommonDAO;
import com.ibm.wh.siam.core.dao.dto.OwnerDetail;
import com.ibm.wh.siam.core.dao.impl.extractor.SingleColumnItemExtractor;
import com.ibm.wh.siam.core.dao.impl.mapper.OwnerDetailRowMapper;
import com.ibm.wh.siam.core.dao.impl.owner.OwnerSqlBuilderIF;
import com.ibm.wh.siam.core.util.AttributeUtil;

/**
 * @author Match Grun
 *
 */
@Repository
public class DAOCoreCommonImpl
extends BaseSiamDAO
implements CoreCommonDAO
{

    // DB function to determine owner type.
    private static final String SQL_IDENTIFY_OWNER_TYPE =
            "select " + SiamTableNames.SIAM_DB_NAME + ".siam_identify_owner_type( :ownerId )";

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( DAOCoreCommonImpl.class );

    @Resource
    OwnerSqlBuilderIF ownerBuilder;

    // Setup template.
    NamedParameterJdbcTemplate namedTemplate;
    public DAOCoreCommonImpl( NamedParameterJdbcTemplate template ) {
        this.namedTemplate = template;
    }

    @Override
    public String identifyOwnerType( final String ownerId ) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ownerId", ownerId );
        return namedTemplate.queryForObject(SQL_IDENTIFY_OWNER_TYPE, params, String.class );
    }

    @Override
    public OwnerDetail fetchOwnerDetail( final String ownerId ) {
        String ownerType = identifyOwnerType(ownerId);
        return fetchOwnerDetail(ownerType, ownerId);
    }

    @Override
    public OwnerDetail fetchOwnerDetail(
            final String ownerType,
            final String ownerId )
    {
        OwnerDetail ownerDetail = null;
        if( SiamOwnerTypes.isValid( ownerType) ) {
            String sql = ownerBuilder.fetchSqlStatement(ownerType);
            if( sql != null ) {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue( OwnerSqlBuilderIF.PARM_NAME_OWNER_TYPE, ownerType );
                params.addValue( OwnerSqlBuilderIF.PARM_NAME_OWNER_ID, ownerId );
                ownerDetail =
                        namedTemplate.queryForObject(
                                sql,
                                params,
                                new OwnerDetailRowMapper() );
                /*
                if( logger.isInfoEnabled() ) {
                    logger.info( "ownerDetail=" + ownerDetail);
                }
                */
            }
        }
        return ownerDetail;
    }

    @Override
    public boolean validateOwner(
            final String ownerType,
            final String ownerId )
    {
        if( AttributeUtil.isValid( ownerType) ) {
            String sql = ownerBuilder.fetchSqlStatementValidate(ownerType);
            /*
            if( logger.isInfoEnabled() ) {
                logger.info("sql=\n" + sql);
            }
            */
            if( sql != null ) {
                MapSqlParameterSource params = new MapSqlParameterSource();
                params.addValue( OwnerSqlBuilderIF.PARM_NAME_OWNER_ID, ownerId );

                SingleColumnItemExtractor extractor = new SingleColumnItemExtractor();
                extractor.setNameItem(OwnerSqlBuilderIF.ALIAS_ID);

                List<String> list = namedTemplate.query( sql, params, extractor);
                if( list != null ) {
                    if( list.size() == 1 ) {
                        return true;
                    }

                }
            }

        }
        return false;
    }
}
