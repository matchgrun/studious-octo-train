/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.ProductEndpointDAO;
import com.ibm.wh.siam.core.dto.ProductEndpoint;
import com.ibm.wh.siam.core.request.product.ProductEndpointItem;
import com.ibm.wh.siam.core.request.product.ProductEndpointSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.product.ProductEndpointListResponse;
import com.ibm.wh.siam.core.response.product.ProductEndpointRef;
import com.ibm.wh.siam.core.response.product.ProductEndpointResponse;
import com.ibm.wh.siam.core.response.product.ProductEndpointSaveResponse;
import com.ibm.wh.siam.core.response.product.ProductListEndpointResponse;
import com.ibm.wh.siam.core.service.ProductEndpointService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class ProductEndpointServiceImpl
extends BaseSiamService
implements ProductEndpointService
{
    @Resource
    ProductEndpointDAO endpointDao;


    private static final String ERRMSG_NOT_FOUND = "Product Endpoint not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Product Endpoint.";
    private static final String ERRMSG_NOT_SPECIFIED = "Product Endpoint not Specified.";

    private static final Logger logger = LoggerFactory.getLogger( ProductEndpointService.class );

    @Override
    public ProductEndpointListResponse findByProduct( final String productId ) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByProduct()");
            logger.info( "productId=" + productId );
        }
        ResponseStatus sts = null;
        ProductEndpointListResponse response = new ProductEndpointListResponse();
        List<ProductEndpoint> list = endpointDao.findByProduct(productId);
        response.setProductId(productId);
        response.setListEndpoints(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ProductEndpointResponse findById(String endpointId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "endpointId=" + endpointId );
        }
        ResponseStatus sts = null;
        ProductEndpointResponse response = new ProductEndpointResponse();
        ProductEndpoint endpoint = endpointDao.findById(endpointId);
        response.setEndpoint(endpoint);
        if( endpoint == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ProductEndpointResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String endpointId)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "endpointId=" + endpointId );
        }

        ProductEndpointResponse response = new ProductEndpointResponse();
        if( StringUtils.isEmpty( endpointId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No endpoint ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        ProductEndpoint objDeleted = null;
        ProductEndpoint objFound = endpointDao.findById(endpointId);
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = endpointDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
            }
        }

        response.setStatus(sts);
        response.setEndpoint(objDeleted);
        return response;
    }

    @Override
    public ProductEndpointResponse save(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpoint objToSave) {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave" + objToSave );
        }

        ProductEndpointResponse response = new ProductEndpointResponse();

        ProductEndpoint objSaved = null;
        String endpointId = objToSave.getEndpointId();
        ProductEndpoint objFound = endpointDao.findById(endpointId);

        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Perform validation
        ResponseStatus sts = null;
        List<ValidationError> listErrors = validateProductEndpoint(recordUpdater, objToSave);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setEndpoint(objToSave);
            return response;
        }

        if( objFound == null ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }

            try {
                objSaved = endpointDao.insert(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusInsertFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                sts = statusInsertFailed();
            }
        }
        else {
            // Update existing record
            if( logger.isInfoEnabled() ) {
                logger.info( "Updating..." );
            }
            try {
                objSaved = endpointDao.updateById(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusUpdateFailed();
                }
                else {
                    sts = statusSuccess();
                }
            }
            catch( Exception e) {
                sts = statusUpdateFailed();
            }
        }

        response.setStatus(sts);
        response.setEndpoint(objSaved);

        return response;
    }

    /**
     * Validate a single endpoint object.
     * @param recordUpdater Record updater.
     * @param endpoint  Endpoint.
     * @return  List of errors.
     */
    private List<ValidationError> validateProductEndpoint(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpoint endpoint )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, endpoint);
        return listErrors;
    }

    /**
     * Validate an endpoint item.
     * @param recordUpdater Record updater.
     * @param endpointItem  Endpoint item.
     * @return  List of errors.
     */
    private List<ValidationError> validateProductEndpoints(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpointItem endpointItem )
    {
        FieldValidator fv = new FieldValidator();
        String productId = endpointItem.getProductId();

        List<ValidationError> listErrors = new ArrayList<ValidationError>();
        if( StringUtils.isEmpty(productId) ) {
            ValidationError vError = new ValidationError("productId",FieldValidator.MSG_EMPTY);
            listErrors.add(vError);
        }

        List<ProductEndpoint>listEndpoints = endpointItem.getListEndpoints();
        for( ProductEndpoint endpoint : listEndpoints ) {
            listErrors.addAll( fv.validate(recordUpdater, endpoint) );
        }
        return listErrors;
    }

    /**
     * Identify changes to the list of endpoints.
     * @param listToSave    List of endpoints to save
     * @param listExisting  List of existing endpoints.
     * @param listInsert    List to insert.
     * @param listModify    List to modify; this will always be empty.
     * @param listDelete    List to delete.
     * @return
     */
    private boolean resolveChanges(
            final List<ProductEndpoint> listToSave,
            final List<ProductEndpoint> listExisting,
            final List<ProductEndpoint> listInsert,
            final List<ProductEndpoint> listModify,
            final List<ProductEndpoint> listDelete )
    {
        Map<String,ProductEndpoint> mapExisting = new HashMap<String,ProductEndpoint>();
        Map<String,ProductEndpoint> mapToSave= new HashMap<String,ProductEndpoint>();

        // Identify existing endpoints
        listExisting.forEach( ( item ) -> {
            String url = item.getEndpointUrl();
            if( mapExisting.containsKey( url ) ) {
                // Duplicate item - Remove
                listDelete.add(item);
            }
            else {
                // Keep for processing
                mapExisting.put( url, item );
            }
        });

        // Identify new endpoints
        listToSave.forEach( ( item ) -> {
            String url = item.getEndpointUrl();
            if( ! mapExisting.containsKey(url) ) {
                // Don't have URL. Need to save.
                if( ! mapToSave.containsKey( url ) ) {
                    listInsert.add(item);
                }
            }
            // Mark item
            mapToSave.put( url, item );
        });

        // Identify existing endpoints not being saved
        mapExisting.forEach( ( url, item ) -> {
            if( ! mapToSave.containsKey(url) ) {
                // Need to remove
                listDelete.add(item);
            }
        });

        int sz = listInsert.size() + listModify.size() + listDelete.size();
        if( sz > 0 ) return true;
        return false;
    }

    @Override
    public ProductEndpointSaveResponse save(
            final RecordUpdaterIF recordUpdater,
            final ProductEndpointSaveRequest req)
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()" );
        }

        ProductEndpointSaveResponse response = new ProductEndpointSaveResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        ProductEndpointItem endpointItem = req.getProductEndpoint();
        String productId = endpointItem.getProductId();
        if( logger.isInfoEnabled() ) {
            logger.info( "productId=" + productId );
        }

        List<ProductEndpoint> listToSave = endpointItem.getListEndpoints();
        if( logger.isInfoEnabled() ) {
            logger.info( "req=" + req );
        }

        // Assign productId
        listToSave.forEach( ( endpoint ) -> {
            endpoint.setProductId(productId);
        });

        // Validate endpoints
        ResponseStatus sts = null;
        listErrors = validateProductEndpoints(recordUpdater, endpointItem);
        if( listErrors.size() > 0 ) {
            sts = validationErrors();
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            response.setProductId(productId);
            response.setListEndpoints(listToSave);
            return response;
        }

        List<ProductEndpoint> listExisting = endpointDao.findByProduct(productId);

        // Resolve changes
        List<ProductEndpoint> listInsert = new ArrayList<ProductEndpoint>();
        List<ProductEndpoint> listModify = new ArrayList<ProductEndpoint>();
        List<ProductEndpoint> listDelete = new ArrayList<ProductEndpoint>();

        // Identify changes
        boolean haveChanges = resolveChanges(
                listToSave,
                listExisting,
                listInsert,
                listModify,
                listDelete);

        if( logger.isInfoEnabled() ) {
            logger.info( "haveChanges=" + haveChanges );
            dumpList( "listExisting", listExisting );
            dumpList( "listToSave", listToSave );
            dumpList( "listInsert", listInsert );
            dumpList( "listModify", listModify );
            dumpList( "listDelete", listDelete );
        }

        // Save changes
        boolean haveErrors = processChanges(
                recordUpdater,
                listInsert,
                listModify,
                listDelete);
        if( haveErrors ) {
            response.setListEndpoints(listToSave);;
            sts = statusUpdateFailed();
        }
        else {
            List<ProductEndpoint> listSaved = endpointDao.findByProduct(productId);
            response.setListEndpoints(listSaved);
            sts = statusSuccess();
        }

        response.setProductId(productId);
        response.setStatus(sts);

        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final ProductEndpointSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }


    /**
     * Apply database changes.
     * @param recordUpdater Record updater.
     * @param listInsert    List of objects to save.
     * @param listModify    List of objects to modify.
     * @param listDelete    List of objects to delete.
     * @return <i>true</i> if errors.
     */
    private boolean processChanges(
            final RecordUpdaterIF recordUpdater,
            final List<ProductEndpoint> listInsert,
            final List<ProductEndpoint> listModify,
            final List<ProductEndpoint> listDelete )
    {
        boolean haveErrors = false;

        // Process deletions
        for( ProductEndpoint item : listDelete ) {
            try {
                endpointDao.deleteById(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        for( ProductEndpoint item : listInsert ) {
            try {
                endpointDao.insert(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        /*
        for( ProductEndpoint item : listModify ) {
            try {
                endpointDao.updateById(recordUpdater, item);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                haveErrors = true;
            }
        }
        */
        return haveErrors;
    }

    private void dumpList(
            final String msg,
            final List<ProductEndpoint> list )
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
    public ProductListEndpointResponse findProductByEndpointUrl(String endpointUrl) {

        if( logger.isInfoEnabled() ) {
            logger.info("findByEndpointUrl()");
            logger.info( "endpointUrl=" + endpointUrl );
        }
        ResponseStatus sts = null;
        ProductListEndpointResponse response = new ProductListEndpointResponse();
        List<ProductEndpointRef> list = endpointDao.findEndpointByUrl(endpointUrl);
        response.setEndpointUrl(endpointUrl);
        response.setListProductEndpoints(list);
        if( list == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

}
