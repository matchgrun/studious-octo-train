/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.busunit.dao.impl.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.ibm.wh.siam.busunit.dao.impl.mapper.SearchItemRowMapper;
import com.ibm.wh.siam.common.search.SearchDefinition;
import com.ibm.wh.siam.common.search.SearchFilterIF;
import com.ibm.wh.siam.common.search.SearchWindow;
import com.ibm.wh.siam.core.dao.impl.SiamTableNames;

/**
 * @author Match Grun
 *
 */
@Component
public class SearchBuilder
implements SearchBuilderIF
{
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger( SearchBuilder.class );

    /**
     * Search constants.
     */
    private static final String FILTER_NAME_PREFIX = "filter";
    private static final String SQL_WILDCARD = "%";

    /**
     * Search builders,
     */
    private static Map<String, SearchTargetIF> mapSearchTargets = buildMapSearchTargets();

    /**
     * Build map of all search targets.
     * @return Map of targets keyed by target name.
     */
    private static Map<String, SearchTargetIF> buildMapSearchTargets() {
        Map<String, SearchTargetIF> map = new HashMap<String, SearchTargetIF>();
        buildMapEntry( map, new SearchTargetOrganization() );
        buildMapEntry( map, new SearchTargetGroup() );
        return map;
    }

    /**
     * Populate map entry for specified search target.
     * @param map           Map to populate.
     * @param searchTarget  Target.
     */
    private static void buildMapEntry(
            final Map<String, SearchTargetIF> map,
            final SearchTargetIF searchTarget )
    {
        map.put( searchTarget.getTargetName(), searchTarget );
    }

    /**
     * Build SQL statement for specified target.
     * @param searchTarget  Search target definition.
     * @param searchWindow  Search window.
     * @param listFilters   List of search filters.
     * @return SQL statement.
     */
    private static String buildSqlResults(
            final SearchTargetIF searchTarget,
            final SearchWindow searchWindow,
            final List<SearchFilterIF> listFilters )
    {
        StringBuilder sb = new StringBuilder( 200 );
        sb.append( "select " );
        sb.append( searchTarget.getColumnItem() ).append( ", " );
        sb.append( searchTarget.getColumnDescription() ).append( ", ");
        sb.append( searchTarget.getColumnStatus() );

        sb.append( "\nfrom " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( searchTarget.getTableName() );

        if( listFilters != null ) {
            buildSearchFilters( searchTarget, sb, listFilters);
        }
        sb.append( "\norder by " );
        sb.append( searchTarget.getColumnDescription() ).append( ", ");
        sb.append( searchTarget.getColumnItem() );

        sb.append( "\nlimit " ).append( searchWindow.getWindowSize() );
        sb.append( "\noffset ").append( searchWindow.getStartPosition() );

        return sb.toString();
    }

    /**
     * Build SQL statement for count for specified target.
     * @param searchTarget  Search target definition.
     * @param listFilters   List of search filters.
     * @return SQL statement.
     */
    private static String buildSqlCount(
            final SearchTargetIF searchTarget,
            final List<SearchFilterIF> listFilters )
    {
        StringBuilder sb = new StringBuilder( 200 );
        sb.append( "select count(*) as count_search" );
        sb.append( "\nfrom " );
        sb.append( SiamTableNames.SIAM_DB_NAME ).append( "." ).append( searchTarget.getTableName() );

        if( listFilters != null ) {
            buildSearchFilters( searchTarget, sb, listFilters);
        }

        return sb.toString();
    }

    @Override
    public String buildSqlSearch( final SearchDefinition searchDefinition )
    {
        String sql= null;
        SearchTargetIF searchTarget = mapSearchTargets.get( searchDefinition.getSearchTarget() );
        if( searchTarget != null ) {
            SearchWindow searchWindow = searchDefinition.getSearchWindow();
            List<SearchFilterIF> listFilters = searchDefinition.getListSearchFilters();
            /*
            if( logger.isInfoEnabled() ) {
                logger.info( "listFilters=\n" + listFilters );
            }
            */
            sql = buildSqlResults( searchTarget, searchWindow, listFilters );
        }

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( sql );
        }
        */

        return sql;
    }

    @Override
    public SearchItemRowMapper buildRowMapper( final SearchDefinition searchDefinition ) {
        SearchItemRowMapper rowMapper = new SearchItemRowMapper();
        SearchTargetIF searchTarget = mapSearchTargets.get(searchDefinition.getSearchTarget());
        if( searchTarget != null ) {
            rowMapper.setNameItem( searchTarget.getColumnItem() );
            rowMapper.setNameDescription( searchTarget.getColumnDescription() );
            rowMapper.setNameStatus( searchTarget.getColumnStatus() );
        }
        return rowMapper;
    }

    /**
     * Build search filters into SQL statement.
     * @param searchTarget  Search target.
     * @param sb            Buffer containing SQL statement.
     * @param listFilters   List of search filters.
     */
    private static void buildSearchFilters(
            final SearchTargetIF searchTarget,
            final StringBuilder sb,
            final List<SearchFilterIF> listFilters )
    {
        if( listFilters.isEmpty() ) return;

        int ind = 0;
        boolean flagFirst = true;
        for( Iterator<SearchFilterIF> it = listFilters.iterator(); it.hasNext(); ind++) {
            SearchFilterIF filter = it.next();

            String expr = buildFilterParameterName( filter, ind );
            if( expr != null ) {
                if( flagFirst ) {
                    sb.append("\nwhere " );
                }
                else {
                    sb.append("\nand " );
                }
                sb.append( filter.getFieldName() ).append( " ilike " ).append( ":" ).append( expr );
                flagFirst = false;
            }
        }
    }

    /**
     * Build parameter name used in SQL statement.
     * @param filter    Search filter.
     * @param ind       Index.
     * @return  Parameter name.
     */
    private static String buildFilterParameterName(
            final SearchFilterIF filter,
            final int ind )
    {
        StringBuilder sb = new StringBuilder(40);
        sb.append( FILTER_NAME_PREFIX ).append( ind );
        return sb.toString();
    }

    /**
     * Build parameter entry into parameter source.
     * @param params    Parameters.
     * @param filter    Search filter.
     * @param ind       Index.
     */
    private static void buildParameterEntry(
            final MapSqlParameterSource params,
            final SearchFilterIF filter,
            final int ind )
    {
        String parmName = buildFilterParameterName( filter, ind );

        StringBuilder sb = new StringBuilder(40);
        int scope = filter.getScope();
        String term = filter.getSearchTerm();
        if( scope == SearchFilterIF.EXACT_MATCH ) {
            sb.append( term );
        }
        else if( scope == SearchFilterIF.BEGINS_WITH ) {
            sb.append( term ).append( SQL_WILDCARD );
        }
        else if( scope == SearchFilterIF.CONTAINS ) {
            sb.append( SQL_WILDCARD ).append( term ).append( SQL_WILDCARD );
        }

        if( sb.length() > 0 ) {
            params.addValue(parmName, sb.toString() );
        }
    }

    @Override
    public MapSqlParameterSource buildSqlParameterSource( final SearchDefinition searchDefinition ) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        SearchTargetIF searchTarget = mapSearchTargets.get( searchDefinition.getSearchTarget() );
        if( searchTarget != null ) {
            List<SearchFilterIF> listFilters = searchDefinition.getListSearchFilters();
            if( listFilters != null ) {
                /*
                if( logger.isInfoEnabled() ) {
                    logger.info( "listFilters=\n" + listFilters );
                }
                */

                int ind = 0;
                for( Iterator<SearchFilterIF> it = listFilters.iterator(); it.hasNext(); ind++ ) {
                    SearchFilterIF filter = it.next();
                    buildParameterEntry( params, filter, ind );
                }
            }
        }
        return params;
    }

    @Override
    public String buildSqlCount( final SearchDefinition searchDefinition )
    {
        String sql= null;
        SearchTargetIF searchTarget = mapSearchTargets.get( searchDefinition.getSearchTarget() );
        if( searchTarget != null ) {
            /* */
            List<SearchFilterIF> listFilters = searchDefinition.getListSearchFilters();
            /*
            if( logger.isInfoEnabled() ) {
                logger.info( "listFilters=\n" + listFilters );
            }
            */
            sql = buildSqlCount(searchTarget, listFilters);
        }

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( sql );
        }
        */
        return sql;
    }

}
