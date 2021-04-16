/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.entitlement.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.common.SiamOwnerTypes;
import com.ibm.wh.siam.core.dao.CoreCommonDAO;
import com.ibm.wh.siam.core.dao.ProductDAO;
import com.ibm.wh.siam.core.response.EntitlementListResponse;
import com.ibm.wh.siam.core.response.EntitlementResponse;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.service.impl.BaseSiamService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;
import com.ibm.wh.siam.entitlement.dao.EntitlementDAO;
import com.ibm.wh.siam.entitlement.dto.Entitlement;
import com.ibm.wh.siam.entitlement.engine.EntitlementEngineIF;
import com.ibm.wh.siam.entitlement.request.EntitlementAddItem;
import com.ibm.wh.siam.entitlement.request.EntitlementAddList;
import com.ibm.wh.siam.entitlement.request.EntitlementAddRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentOwnerQueryItem;
import com.ibm.wh.siam.entitlement.request.EntitlementCurrentRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementModifyItem;
import com.ibm.wh.siam.entitlement.request.EntitlementModifyList;
import com.ibm.wh.siam.entitlement.request.EntitlementModifyRequest;
import com.ibm.wh.siam.entitlement.request.EntitlementSaveItem;
import com.ibm.wh.siam.entitlement.request.EntitlementSaveList;
import com.ibm.wh.siam.entitlement.request.EntitlementSaveRequest;
import com.ibm.wh.siam.entitlement.response.EntitlementAddResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementCurrentResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementModifyResponse;
import com.ibm.wh.siam.entitlement.response.EntitlementSaveResponse;
import com.ibm.wh.siam.entitlement.service.EntitlementService;

/**
 * @author Match Grun
 *
 */
@Component
public class EntitlementServiceImpl
extends BaseSiamService
implements EntitlementService
{
    @Resource
    EntitlementDAO entitlementDao;

    @Resource
    CoreCommonDAO coreCommonDao;

    @Resource
    ProductDAO productDao;

    @Resource
    EntitlementEngineIF entitlementEngine;

    private static final String ERRMSG_NOT_FOUND = "Entitlement(s) not Found.";
    private static final String ERRMSG_NOT_SPECIFIED = "Entitlement(s) not Specified.";
    private static final String ERRMSG_NOT_SPECIFIED_PRODUCT = "Product ID(s) not Specified.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Entitlement.";
    private static final String ERRMSG_INVALID_OWNER_ID = "Invalid Owner ID Specified.";
    private static final String ERRMSG_INVALID_OWNER_TYPE = "Invalid Owner TYPE Specified:";
    private static final String ERRMSG_PRODUCT_IDS_NOT_RECOGNIZED = "Product ID(s) not recognized:";
    private static final String FLD_OWNER_ID = "ownerId";
    private static final String FLD_OWNER_TYPE = "ownerType";
    private static final String FLD_PRODUCT_ID = "productId";
    // private static final String FLD_ENTITLEMENT_ID = "entitlementId";

    private static final Logger logger = LoggerFactory.getLogger( EntitlementServiceImpl.class );

    @Override
    public EntitlementResponse findById(final String entitlementId)
    {
        ResponseStatus sts = null;
        EntitlementResponse response = new EntitlementResponse();
        Entitlement entitlement = entitlementDao.findById(entitlementId);
        response.setEntitlement(entitlement);
        if( entitlement == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public EntitlementListResponse findByOrganizationId( final String orgId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOrganizationId()");
            logger.info( "Org=" + orgId );
        }
        ResponseStatus sts = null;
        EntitlementListResponse response = new EntitlementListResponse();
        Iterable<Entitlement> listEntitlements = entitlementDao.findByOrganizationId(orgId);
        response.setListEntitlements(listEntitlements);
        if( listEntitlements == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }

        // The following works.
        /*
        List<String> listProductCode = new ArrayList<String>();
        listProductCode.add( "App:IR" );
        listProductCode.add( "OTE:IR" );
        listProductCode.add( "EVA:SDA" );
        listProductCode.add( "CAREDIS - TRANSFORM" );
        listProductCode.add( "CAREDISC - ADV PLUS" );

        Iterable<EntitlementProductOwner> list = entitlementDao.findByProductList(listProductCode);
        if( logger.isInfoEnabled() ) {
            logger.info( "list=\n" + list );
        }
        */

        response.setStatus(sts);
        return response;
    }

    /**
     * Validate request object.
     * @param recordUpdater Record updater.
     * @param req           Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final EntitlementSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            EntitlementSaveList esList = req.getEntitlementList();
            listErrors.addAll( fv.validate(recordUpdater, esList ) );
        }

        return listErrors;
    }

    /**
     * Validate request object.
     * @param recordUpdater Record updater.
     * @param req           Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final EntitlementAddRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            EntitlementAddList eaList = req.getEntitlementList();
            listErrors.addAll( fv.validate(recordUpdater, eaList ) );
        }

        return listErrors;
    }

    /**
     * Validate request object.
     * @param recordUpdater Record updater.
     * @param req           Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final RecordUpdaterIF recordUpdater,
            final EntitlementModifyRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            EntitlementModifyList emList = req.getEntitlementList();
            listErrors.addAll( fv.validate(recordUpdater, emList ) );
        }

        return listErrors;
    }

    /**
     * Validate request.
     * @param recordUpdater Record updater.
     * @param esList        List of items to save.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateEntitlementList(
            final RecordUpdaterIF recordUpdater,
            final EntitlementSaveList esList)
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        Set<String> setMessages = new HashSet<String>();
        List<EntitlementSaveItem>listSave = esList.getListToSave();
        for( EntitlementSaveItem item : listSave) {
            List<ValidationError> listEntErrors = fv.validate(recordUpdater, item);
            listErrors.addAll( handleUniqueErrors(setMessages, listEntErrors) );
        }
        return listErrors;
    }

    /**
     * Validate request.
     * @param recordUpdater Record updater.
     * @param eaList        List of items to add.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateEntitlementList(
            final RecordUpdaterIF recordUpdater,
            final EntitlementAddList eaList)
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        Set<String> setMessages = new HashSet<String>();
        List<EntitlementAddItem>listEntitlements = eaList.getListToAdd();
        for( EntitlementAddItem item : listEntitlements ) {
            List<ValidationError> listEntErrors = fv.validate(recordUpdater, item);
            listErrors.addAll( handleUniqueErrors(setMessages, listEntErrors) );
        }
        return listErrors;
    }

    /**
     * Validate request.
     * @param recordUpdater Record updater.
     * @param emList        List of items to modify.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateEntitlementList(
            final RecordUpdaterIF recordUpdater,
            final EntitlementModifyList emList)
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        Set<String> setMessages = new HashSet<String>();
        List<EntitlementModifyItem>listEntitlements = emList.getListToModify();
        for( EntitlementModifyItem item : listEntitlements ) {
            List<ValidationError> listEntErrors = fv.validate(recordUpdater, item);
            listErrors.addAll( handleUniqueErrors(setMessages, listEntErrors) );
        }
        return listErrors;
    }

    /**
     * Ensure that same message has not already added to list of errors.
     * @param setMessages   Set of messages already found. Will be updated.
     * @param listErrors    List of errors to examine.
     * @return  List of new errors.
     */
    private List<ValidationError> handleUniqueErrors(
            final Set<String> setMessages,
            final List<ValidationError> listErrors )
    {
        List<ValidationError> listNew = new ArrayList<ValidationError>();
        for( ValidationError vError : listErrors ) {
            String name = vError.getFieldName();
            if( ! setMessages.contains(name) ) {
                setMessages.add(name);
                listNew.add(vError);
            }
        }
        return listNew;
    }

    @SuppressWarnings("unused")
    private void dumpListSaveItem(
            final String msg,
            final List<EntitlementSaveItem> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @SuppressWarnings("unused")
    private void dumpListAddItem(
            final String msg,
            final List<EntitlementAddItem> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @SuppressWarnings("unused")
    private void dumpListModifyItem(
            final String msg,
            final List<EntitlementModifyItem> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @SuppressWarnings("unused")
    private void dumpList(
            final String msg,
            final List<Entitlement> list )
    {
        if( logger.isInfoEnabled() ) {
            logger.info(msg + " {");
            list.forEach( item -> {
                logger.info( "  - " + item );
            });
            logger.info("}");
        }
    }

    @Override
    public EntitlementSaveResponse save(
            final RecordUpdaterIF recordUpdater,
            final EntitlementSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
        }

        // Validate request
        ResponseStatus sts = null;
        EntitlementSaveResponse response = new EntitlementSaveResponse();
        List<ValidationError> listErrors = validateRequest( recordUpdater, req );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Validate entitlement save list
        EntitlementSaveList esList = req.getEntitlementList();
        String ownerId = esList .getOwnerId();
        String ownerType = esList.getOwnerType();
        if( ownerType != null ) ownerType = ownerType.toUpperCase();

        List<EntitlementSaveItem> listToSave = esList.getListToSave();
        /*
        if( logger.isInfoEnabled() ) {
            dumpListSaveItem( "listToSave", listToSave );
        }
        */

        response.setListToSave(listToSave);

        listErrors = validateEntitlementList(
                recordUpdater,
                esList );

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        List<Entitlement> listExisting = entitlementDao.findByOwner(ownerType, ownerId);
        /*
        if( logger.isInfoEnabled() ) {
            dumpList( "listExisting", listExisting );
        }
        */

        // Resolve changes
        List<Entitlement> listInsert = new ArrayList<Entitlement>();
        List<Entitlement> listModify = new ArrayList<Entitlement>();
        List<Entitlement> listDelete = new ArrayList<Entitlement>();

        // Identify changes
        boolean haveChanges = resolveChanges(
                listToSave,
                listExisting,
                listModify,
                listInsert,
                listDelete,
                ownerType,
                ownerId );

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "haveChanges=" + haveChanges );
            logger.info( "listToSave=" + listToSave );
            logger.info( "listExists=" + listExisting );
            logger.info( "listInsert=" + listInsert );
            logger.info( "listModify=" + listModify );
            logger.info( "listDelete=" + listDelete );
        }
        */

        if( ! haveChanges ) {
            List<Entitlement> listEntitlements = entitlementDao.findByOwner(ownerType, ownerId);
            response.setListEntitlements(listEntitlements);
            sts = statusSuccess();
            response.setStatus(sts);
            return response;
        }

        // Validate product ID's
        List<String> listProductIds = buildListUniqueProductIds( listInsert );
        Map<String, String> mapProducts = productDao.findMapProductIds(listProductIds);
        /*
        if( logger.isInfoEnabled() ) {
            mapProducts.forEach( (n,v) -> {
                logger.info( "[" + n + "]==>[" + v + "]");
            });
        }
        */

        listErrors = validateProducts(
                listProductIds,
                mapProducts ) ;

        // Validate owner ID, only if no other errors found. Performance boost.
        if( listErrors.size() < 1 ) {
            if( ! coreCommonDao.validateOwner(ownerType, ownerId) ) {
                ValidationError vError = new ValidationError( FLD_OWNER_ID, ERRMSG_INVALID_OWNER_ID );
                listErrors.add(vError);
            }
        }

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Verify inserts.
        listErrors = verifyChanges( listInsert );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        boolean haveErrors = processChanges(
                recordUpdater,
                listModify,
                listInsert,
                listDelete);

        if( haveErrors ) {
            sts = statusUpdateFailed();
        }
        else {
            List<Entitlement> listEntitlements = entitlementDao.findByOwner(ownerType, ownerId);
            response.setListEntitlements(listEntitlements);
            sts = statusSuccess();
        }

        response.setStatus(sts);

        return response;
    }

    /**
     * Identify changes to be made.
     * @param listToSave    Items to save.
     * @param listExisting  Existing items.
     * @param listModify    Items to modify.
     * @param listInsert    Items to insert.
     * @param listDelete    Items to delete.
     * @param ownerType     Owner type.
     * @param ownerId       Owner ID.
     * @return <i>true</i> if changes identified.
     */
    private boolean resolveChanges(
            final List<EntitlementSaveItem> listToSave,
            final List<Entitlement> listExisting,
            final List<Entitlement> listModify,
            final List<Entitlement> listInsert,
            final List<Entitlement> listDelete,
            final String ownerType,
            final String ownerId )
    {
        Set<String> setExisting = new HashSet<String>();
        Set<String> setToSave = new HashSet<String>();
        listExisting.forEach( ( item ) -> {
            setExisting.add( item.getEntitlementId() );
        });
        listToSave.forEach( ( item ) -> {
            setToSave.add( item.getEntitlementId() );
        });

        // Identify deletions
        listExisting.forEach( ( item ) -> {
            String entitlementId = item.getEntitlementId();
            if( ! setToSave.contains(entitlementId) ) {
                listDelete.add(item);
            }
        });

        // Identify inserts/modify
        listToSave.forEach( ( item ) -> {
            Entitlement entItem = createItemToSave( item, ownerType, ownerId );
            String entitlementId = item.getEntitlementId();
            if( StringUtils.isEmpty( entitlementId ) ) {
                listInsert.add(entItem);
            }
            else {
                if( setExisting.contains(entitlementId) ) {
                    listModify.add(entItem);
                }
            }
        });

        int sz = listInsert.size() + listModify.size() + listDelete.size();
        if( sz > 0 ) return true;
        return false;
    }

    /**
     * Identify changes to be made.
     * @param listToSave    Items to save.
     * @param ownerType     Owner type.
     * @param ownerId       Owner ID.
     * @return List of entitlements to add.
     */
    private List<Entitlement> resolveAddChanges(
            final List<EntitlementAddItem> listToSave,
            final String ownerType,
            final String ownerId )
    {
        List<Entitlement> listInsert = new ArrayList<Entitlement>();

        // Identify inserts
        listToSave.forEach( ( item ) -> {
            Entitlement entItem = createItemToSave( item, ownerType, ownerId );
            String productId = item.getProductId();
            if( ! StringUtils.isEmpty( productId ) ) {
                listInsert.add(entItem);
            }
        });

        return listInsert;
    }

    /**
     * Identify changes to be made.
     * @param listToSave    Items to save.
     * @param listExisting  Existing items.
     * @param listModify    Items to modify.
     * @param ownerType     Owner type.
     * @param ownerId       Owner ID.
     * @return <i>true</i> if changes identified.
     */
    private boolean resolveModifyChanges(
            final List<EntitlementModifyItem> listToSave,
            final List<Entitlement> listExisting,
            final List<Entitlement> listModify,
            final String ownerType,
            final String ownerId )
    {
        Set<String> setExisting = new HashSet<String>();
        listExisting.forEach( ( item ) -> {
            setExisting.add( item.getEntitlementId() );
        });

        // Identify inserts/modify
        listToSave.forEach( ( item ) -> {
            Entitlement entItem = createItemToSave( item, ownerType, ownerId );
            String entitlementId = item.getEntitlementId();
            if( ! StringUtils.isEmpty( entitlementId ) ) {
                if( setExisting.contains(entitlementId) ) {
                    listModify.add(entItem);
                }
            }
        });

        int sz = listModify.size();
        if( sz > 0 ) return true;
        return false;
    }

    /**
     * Create entitlement from specified save item.
     * @param item      Item being saved.
     * @param ownerType Owner type.
     * @param ownerId   Owner ID.
     * @return  Populated entitlement object.
     */
    private Entitlement createItemToSave(
            final EntitlementSaveItem item,
            final String ownerType,
            final String ownerId )
    {
        Entitlement entItem = new Entitlement();
        entItem.setOwnerType(ownerType);
        entItem.setOwnerId(ownerId);
        entItem.setEntitlementId( item.getEntitlementId() );
        entItem.setProductId( item.getProductId() );
        entItem.setStatus( item.getStatus() );
        entItem.setStartDate( item.getStartDate() );
        entItem.setEndDate( item.getEndDate() );
        return entItem;
    }

    /**
     * Create entitlement from specified add item.
     * @param item      Item being saved.
     * @param ownerType Owner type.
     * @param ownerId   Owner ID.
     * @return  Populated entitlement object.
     */
    private Entitlement createItemToSave(
            final EntitlementAddItem item,
            final String ownerType,
            final String ownerId )
    {
        Entitlement entItem = new Entitlement();
        entItem.setOwnerType(ownerType);
        entItem.setOwnerId(ownerId);
        entItem.setProductId( item.getProductId() );
        entItem.setStatus( item.getStatus() );
        entItem.setStartDate( item.getStartDate() );
        entItem.setEndDate( item.getEndDate() );
        return entItem;
    }

    /**
     * Create entitlement from specified modify item.
     * @param item      Item being saved.
     * @param ownerType Owner type.
     * @param ownerId   Owner ID.
     * @return  Populated entitlement object.
     */
    private Entitlement createItemToSave(
            final EntitlementModifyItem item,
            final String ownerType,
            final String ownerId )
    {
        Entitlement entItem = new Entitlement();
        entItem.setOwnerType(ownerType);
        entItem.setOwnerId(ownerId);
        entItem.setEntitlementId( item.getEntitlementId() );
        entItem.setStatus( item.getStatus() );
        entItem.setStartDate( item.getStartDate() );
        entItem.setEndDate( item.getEndDate() );
        return entItem;
    }


    /**
     * Process all database changes for this request.
     * @param recordUpdater Record updater.
     * @param listInsert    List of items to insert.
     * @param listModify    List of items to modify.
     * @param listDelete    List of items to delete.
     * @return <i>true</i> if errors.
     */
    private boolean processChanges(
            final RecordUpdaterIF recordUpdater,
            final List<Entitlement> listModify,
            final List<Entitlement> listInsert,
            final List<Entitlement> listDelete )
    {
        boolean haveErrors = false;
        // Process deletions
        if( listDelete != null ) {
            for( Entitlement item : listDelete ) {
                try {
                    entitlementDao.deleteById(recordUpdater, item);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    haveErrors = true;
                }
            }
        }
        if( listInsert != null ) {
            for( Entitlement item : listInsert ) {
                try {
                    entitlementDao.insert(recordUpdater, item);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    haveErrors = true;
                }
            }
        }
        if( listModify != null ) {
            for( Entitlement item : listModify ) {
                try {
                    entitlementDao.updateById(recordUpdater, item);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    haveErrors = true;
                }
            }
        }
        return haveErrors;
    }

    /**
     * Verify that inserted objects have product ID specified.
     * @param listInsert List of items to insert.
     * @return  List of errors.
     */
    private List<ValidationError> verifyChanges( final List<Entitlement> listInsert )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "verifyInserts()" );
        }
        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        for( Entitlement item : listInsert ) {
            String productId = item.getProductId();
            if( StringUtils.isEmpty(productId) ) {
                listErrors.add( new ValidationError( FLD_PRODUCT_ID, ERRMSG_NOT_SPECIFIED_PRODUCT ) );
                break;
            }
        }

        return listErrors;
    }

//    /**
//     * Verify that list of inserted objects have product ID specified.
//     * @param listInsert List of items to insert.
//     * @return  List of errors.
//     */
//    private List<ValidationError> verifyProductIdsForInsert( final List<Entitlement> listInsert )
//    {
//        if( logger.isDebugEnabled() ) {
//            logger.debug( "verifyProductIdsForInsert()" );
//        }
//        List<ValidationError> listErrors = new ArrayList<ValidationError>();
//        for( Entitlement item : listInsert ) {
//            String productId = item.getProductId();
//            if( StringUtils.isEmpty(productId)) {
//                new ValidationError( FLD_PRODUCT_ID, ERRMSG_NOT_SPECIFIED_PRODUCT );
//            }
//        }
//        return listErrors;
//    }


    /**
     * Build list of unique product ID's for entitlements being inserted.
     * @param listInsert    List of entitlements being inserted.
     * @return  List of ID's.
     */
    private List<String> buildListUniqueProductIds( final List<Entitlement> listInsert ) {
        Set<String> setIds = new HashSet<String>();
        List<String> listIds = new ArrayList<String>();
        listInsert.forEach(item -> {
            String productId = item.getProductId();
            if( ! StringUtils.isEmpty(productId)) {
                if( ! setIds.contains(productId)) {
                    listIds.add(productId);
                }
                setIds.add(productId);
            }
        });
        return listIds;
    }

    /**
     * Validate product list.
     * @param listProductIds    List of product ID's to check.
     * @param mapValidProducts  Map of valid products.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateProducts(
            final List<String> listProductIds,
            final Map<String, String> mapValidProducts)
    {
        List<ValidationError> listErrors = new ArrayList<ValidationError>();

        // Validate product ID's
        StringBuilder sb = new StringBuilder( 100 );
        boolean flagError = false;
        for( String id : listProductIds ) {
            id = id.trim();
            if( ! mapValidProducts.containsKey( id ) ) {
                if( flagError ) {
                    sb.append( "; " );
                }
                else {
                    sb.append( ERRMSG_PRODUCT_IDS_NOT_RECOGNIZED ).append( " { ");
                }
                sb.append( id );
                flagError = true;
            }
        }

        if( flagError ) {
            sb.append( " }" );
            ValidationError vError = new ValidationError( FLD_PRODUCT_ID, sb.toString() );
            listErrors.add(vError);
        }

        return listErrors;
    }

    @Override
    public EntitlementAddResponse addEntitlements(
            final RecordUpdaterIF recordUpdater,
            final EntitlementAddRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("addEntitlements()");
        }

        // Validate request
        ResponseStatus sts = null;
        EntitlementAddResponse response = new EntitlementAddResponse();
        List<ValidationError> listErrors = validateRequest( recordUpdater, req );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Validate entitlement save list
        EntitlementAddList eaList = req.getEntitlementList();
        String ownerId = eaList .getOwnerId();
        String ownerType = eaList.getOwnerType();
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "ownerId=" + ownerId );
            logger.info( "ownerType=" + ownerType );
        }
        */
        if( ownerType != null ) ownerType = ownerType.toUpperCase();

        List<EntitlementAddItem> listToAdd = eaList.getListToAdd();
        response.setListToAdd(listToAdd);
        /*
        if( logger.isInfoEnabled() ) {
            dumpListAddItem( "listToAdd", listToAdd );
        }
        */

        listErrors = validateEntitlementList(
                recordUpdater,
                eaList );

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Resolve changes
        List<Entitlement> listInsert = resolveAddChanges(
                listToAdd,
                ownerType,
                ownerId );

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "listToSave=" + listToAdd );
            logger.info( "listInsert=" + listInsert );
        }
        */

        // Validate product ID's
        List<String> listProductIds = buildListUniqueProductIds( listInsert );
        Map<String, String> mapProducts = productDao.findMapProductIds(listProductIds);
        /*
        if( logger.isInfoEnabled() ) {
            mapProducts.forEach( (n,v) -> {
                logger.info( "[" + n + "]==>[" + v + "]");
            });
        }
        */

        listErrors = validateProducts(
                listProductIds,
                mapProducts ) ;

        // Validate owner ID, only if no other errors found. Performance boost.
        if( listErrors.size() < 1 ) {
            if( ! coreCommonDao.validateOwner(ownerType, ownerId) ) {
                ValidationError vError = new ValidationError( FLD_OWNER_ID, ERRMSG_INVALID_OWNER_ID );
                listErrors.add(vError);
            }
        }

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Verify inserts.
        listErrors = verifyChanges( listInsert );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        boolean haveErrors = processChanges(
                recordUpdater,
                null,
                listInsert,
                null );

        if( haveErrors ) {
            sts = statusUpdateFailed();
        }
        else {
            List<Entitlement> listSaved = entitlementDao.findByOwner(ownerType, ownerId);
            response.setListEntitlements(listSaved);
            sts = statusSuccess();
        }

        response.setStatus(sts);

        return response;
    }

    @Override
    public EntitlementListResponse findByOwner(
            final String ownerType,
            final String ownerId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("findByOwner()");
            logger.info( "ownerId=" + ownerId );
            logger.info( "ownerType=" + ownerType );
        }

        String ownerTypeUC = SiamOwnerTypes.defaultValue(ownerType);

        ResponseStatus sts = null;
        EntitlementListResponse response = new EntitlementListResponse();
        Iterable<Entitlement> listEntitlements = entitlementDao.findByOwner(ownerTypeUC, ownerId);
        response.setListEntitlements(listEntitlements);
        if( listEntitlements == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }

        response.setStatus(sts);
        return response;
    }

    @Override
    public EntitlementResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String entitlementId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "entitlementId=" + entitlementId );
        }

        EntitlementResponse response = new EntitlementResponse();
        if( StringUtils.isEmpty( entitlementId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No entitlement ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        Entitlement objDeleted = null;
        Entitlement objFound = entitlementDao.findById(entitlementId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = entitlementDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }
        response.setStatus(sts);
        response.setEntitlement(objDeleted);
        return response;
    }

    @Override
    public EntitlementModifyResponse modifyEntitlements(
            final RecordUpdaterIF recordUpdater,
            final EntitlementModifyRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("modifyEntitlements()");
        }

        // Validate request
        ResponseStatus sts = null;
        EntitlementModifyResponse response = new EntitlementModifyResponse();
        List<ValidationError> listErrors = validateRequest( recordUpdater, req );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        // Validate entitlement save list
        EntitlementModifyList emList = req.getEntitlementList();
        String ownerId = emList .getOwnerId();
        String ownerType = emList.getOwnerType();
        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "ownerId=" + ownerId );
            logger.info( "ownerType=" + ownerType );
        }
        */
        if( ownerType != null ) ownerType = ownerType.toUpperCase();

        List<EntitlementModifyItem> listToModify = emList.getListToModify();
        /*
        if( logger.isInfoEnabled() ) {
            dumpListModifyItem( "listToModify", listToModify );
        }
        */

        response.setListToModify(listToModify);

        listErrors = validateEntitlementList(
                recordUpdater,
                emList );

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        List<Entitlement> listExisting = entitlementDao.findByOwner(ownerType, ownerId);
        /*
        if( logger.isInfoEnabled() ) {
            dumpList( "listExisting", listExisting );
        }
        */

        // Resolve changes
        List<Entitlement> listModify = new ArrayList<Entitlement>();

        // Identify changes
        boolean haveChanges = resolveModifyChanges(
                listToModify,
                listExisting,
                listModify,
                ownerType,
                ownerId );

        /*
        if( logger.isInfoEnabled() ) {
            logger.info( "haveChanges=" + haveChanges );
            logger.info( "listToSave=" + listToModify );
            logger.info( "listExists=" + listExisting );
            logger.info( "listModify=" + listModify );
        }
        */

        if( ! haveChanges ) {
            List<Entitlement> listEntitlements = entitlementDao.findByOwner(ownerType, ownerId);
            response.setListEntitlements(listEntitlements);
            sts = statusSuccess();
            return response;
        }

        // Validate owner ID, only if no other errors found. Performance boost.
        if( listErrors.size() < 1 ) {
            if( ! coreCommonDao.validateOwner(ownerType, ownerId) ) {
                ValidationError vError = new ValidationError( FLD_OWNER_ID, ERRMSG_INVALID_OWNER_ID );
                listErrors.add(vError);
            }
        }

        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        boolean haveErrors = processChanges(
                recordUpdater,
                listModify,
                null,
                null );

        if( haveErrors ) {
            sts = statusUpdateFailed();
        }
        else {
            List<Entitlement> listEntitlements = entitlementDao.findByOwner(ownerType, ownerId);
            response.setListEntitlements(listEntitlements);
            sts = statusSuccess();
        }

        response.setStatus(sts);

        return response;
    }

    /**
     * Validate request object.
     * @param recordUpdater Record updater.
     * @param req           Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final EntitlementCurrentRequest req )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );

        // Validate contained item
        if( req != null ) {
            StringBuilder sb = new StringBuilder( 40 );
            List<EntitlementCurrentOwnerQueryItem> listQueryItems = req.getQueryItems();
            if( listQueryItems != null ) {
                for( EntitlementCurrentOwnerQueryItem item : listQueryItems ) {
                    String ownerType = item.getOwnerType();
                    sb.setLength(0);
                    sb.append(ERRMSG_INVALID_OWNER_TYPE).append(" [" ).append( ownerType ).append("]");
                    if( ! SiamOwnerTypes.isValid(ownerType) ) {
                        listErrors.add(new ValidationError( FLD_OWNER_TYPE, sb.toString() ) );
                    }
                }
            }
        }

        return listErrors;
    }

    @Override
    public EntitlementCurrentResponse fetchCurrentEntitlements( final EntitlementCurrentRequest req ) {
        if( logger.isInfoEnabled() ) {
            logger.info("fetchCurrentEntitlements()");
        }

        // Validate request
        ResponseStatus sts = null;
        EntitlementCurrentResponse response = new EntitlementCurrentResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        List<Entitlement> listEntitlements = entitlementEngine.processRequest(req);
        response.setListEntitlements(listEntitlements);
        sts = statusSuccess();
        response.setStatus(sts);

        return response;
    }

}
