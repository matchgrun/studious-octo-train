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

import com.ibm.wh.siam.common.cache.CacheManager;
import com.ibm.wh.siam.core.common.RecordUpdaterIF;
import com.ibm.wh.siam.core.dao.ProductDAO;
import com.ibm.wh.siam.core.dao.dto.ProductRange;
import com.ibm.wh.siam.core.dto.Product;
import com.ibm.wh.siam.core.request.product.ProductSaveRequest;
import com.ibm.wh.siam.core.response.ResponseStatus;
import com.ibm.wh.siam.core.response.product.ProductListResponse;
import com.ibm.wh.siam.core.response.product.ProductRangeListResponse;
import com.ibm.wh.siam.core.response.product.ProductResponse;
import com.ibm.wh.siam.core.service.ProductService;
import com.ibm.wh.siam.core.validator.FieldValidator;
import com.ibm.wh.siam.core.validator.ValidationError;

/**
 * @author Match Grun
 *
 */
@Component
public class ProductServiceImpl
extends BaseSiamService
implements ProductService<Object>
{
    @Resource
    ProductDAO productDao;

    private static final String ERRMSG_NOT_FOUND = "Product not Found.";
    private static final String ERRMSG_INVALID_OBJECT = "Invalid Product.";
    private static final String ERRMSG_NOT_SPECIFIED = "Product not Specified.";

    private static final Logger logger = LoggerFactory.getLogger( ProductServiceImpl.class );

    CacheManager cacheManager = new CacheManager();

    @Override
    public ProductListResponse findAll() {
        ProductListResponse response = new ProductListResponse();
        Iterable<Product> list = productDao.findAll();
        response.setListProducts(list);
        response.setStatus(statusSuccess());
        return response;
    }

    @Override
    public ProductResponse findByCode(final String prodCode) {
        if( logger.isInfoEnabled() ) {
            logger.info("findByCode()");
            logger.info( "prodCode=" + prodCode );
        }
        ResponseStatus sts = null;
        ProductResponse response = new ProductResponse();
        Product item = productDao.findByCode(prodCode);
        response.setProduct(item);
        if( item == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);
        return response;
    }

    @Override
    public ProductResponse findById( final String prodId) {
        if( logger.isInfoEnabled() ) {
            logger.info("findById()");
            logger.info( "prodId=" + prodId );
        }
        ResponseStatus sts = null;
        ProductResponse response = new ProductResponse();
        Product item = productDao.findById(prodId);
        response.setProduct(item);
        if( item == null ) {
            sts = statusNotFound( ERRMSG_NOT_FOUND );
        }
        else {
            sts = statusSuccess();
        }
        response.setStatus(sts);

        return response;
    }

    @Override
    public ProductResponse save(
            final RecordUpdaterIF recordUpdater,
            final Product objToSave)
    {
        if( logger.isInfoEnabled() ) {
            logger.info("save()");
            logger.info( "objToSave=" + objToSave );
        }

        ProductResponse response = new ProductResponse();

        Product objSaved = null;
        String objId = objToSave.getProductId();
        Product objFound = productDao.findById(objId);
        if( logger.isInfoEnabled() ) {
            logger.info( "objFound=" + objFound );
        }

        // Check whether there is another product with the same code
        String codeCheck = objToSave.getProductCode();
        Product objOther = productDao.findByCode(codeCheck);
        if( logger.isInfoEnabled() ) {
            logger.info( "objOther=" + objOther );
        }

        ResponseStatus sts = null;
        int chk = checkKeys(objToSave, objFound, objOther);
        boolean flagNotify = false;

        // Validate object
        if( chk != ACTION_DUPLICATE ) {
            List<ValidationError> listErrors = validateProduct(recordUpdater, objToSave);
            if( listErrors.size() > 0 ) {
                sts = validationErrors();
                sts.setValidationErrors(listErrors);
                response.setStatus(sts);
                response.setProduct(objToSave);
                return response;
            }
        }

        if( chk == ACTION_DUPLICATE ) {
            sts = statusDuplicateKey();
        }
        else if( chk == ACTION_INSERT ) {
            if( logger.isInfoEnabled() ) {
                logger.info( "Inserting..." );
            }
            try {
                objSaved = productDao.insert(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusInsertFailed();
                }
                else {
                    sts = statusSuccess();
                    flagNotify = true;
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
                objSaved = productDao.updateById(recordUpdater, objToSave);
                if( objSaved == null ) {
                    sts = statusUpdateFailed();
                }
                else {
                    sts = statusSuccess();
                    flagNotify = true;
                }
            }
            catch( Exception e) {
                sts = statusUpdateFailed();
            }
        }

        response.setStatus(sts);
        response.setProduct(objSaved);

        if( flagNotify ) {
            cacheManager.flushCachesByCategory(PRODUCT_CACHE_CATEGORY);
        }

        return response;
    }

    @Override
    public ProductResponse deleteById(
            final RecordUpdaterIF recordUpdater,
            final String prodId )
    {
        if( logger.isInfoEnabled() ) {
            logger.info("deleteById()");
            logger.info( "Product=" + prodId );
        }

        ProductResponse response = new ProductResponse();
        if( StringUtils.isEmpty( prodId ) ) {
            if( logger.isErrorEnabled() ) {
                logger.error( "No product ID." );
            }
            response.setStatus( statusInvalid( ERRMSG_INVALID_OBJECT ) );
            return response;
        }

        ResponseStatus sts = null;
        Product objDeleted = null;
        Product objFound = productDao.findById(prodId);
        boolean flagNotify = false;
        if( objFound == null ) {
            response.setStatus(statusNotFound( ERRMSG_NOT_FOUND ));
        }
        else {
            objDeleted = productDao.deleteById( recordUpdater, objFound );
            if( objDeleted == null ) {
                sts = statusDeleteFailed();
            }
            else {
                sts = statusSuccess();
                flagNotify = true;
            }
        }
        response.setStatus(sts);
        response.setProduct(objDeleted);

        if( flagNotify ) {
            cacheManager.flushCachesByCategory(PRODUCT_CACHE_CATEGORY);
        }

        return response;
    }

    /**
     * Check object keys.
     * @param objSave   Object to save.
     * @param objFound  Object found using primary key value.
     * @param objOther  Object found using alternate key value.
     * @return  Keys status value, as follows:
     */
    private int checkKeys(
            final Product objSave,
            final Product objFound,
            final Product objOther )
    {
        if( objFound == null ) {
            // Inserting data
            if( objOther == null ) {
                // Allow insert to proceed
                return ACTION_INSERT;
            }
            // This would create duplicate on insert
            return ACTION_DUPLICATE;
        }

        // Updating data
        String objCheck = objSave.getProductCode();
        if( objFound.getProductCode().equals( objCheck ) ) {
            // Attempting to change current data, Allow update to proceed
            return ACTION_UPDATE;
        }

        // Check whether there is another record with the same code
        if( objOther == null ) {
            return ACTION_UPDATE;
        }

        // This would create duplicate on update
        return ACTION_DUPLICATE;
    }

    private List<ValidationError> validateProduct(
            final RecordUpdaterIF recordUpdater,
            final Product prod )
    {
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validate(recordUpdater, prod );
        return listErrors;
    }

    @Override
    public ProductResponse save(
            final RecordUpdaterIF recordUpdater,
            final ProductSaveRequest req )
    {
        if( logger.isInfoEnabled() ) {
            logger.info( "save()");
            logger.info( "req=" + req );
        }

        ProductResponse response = new ProductResponse();
        List<ValidationError> listErrors = validateRequest( req );
        if( listErrors.size() > 0 ) {
            ResponseStatus sts = validationErrorObject(ERRMSG_NOT_SPECIFIED);
            sts.setValidationErrors(listErrors);
            response.setStatus(sts);
            return response;
        }

        Product objToSave = req.getProduct();
        response = save( recordUpdater, objToSave );
        return response;
    }

    /**
     * Validate request object.
     * @param req   Request.
     * @return  List of validation errors.
     */
    private List<ValidationError> validateRequest(
            final ProductSaveRequest req )
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "validateRequest()" );
        }
        FieldValidator fv = new FieldValidator();
        List<ValidationError> listErrors = fv.validateRequest( req );
        return listErrors;
    }

    @Override
    public ProductRangeListResponse findAllActiveProducts() {
        ProductRangeListResponse response = new ProductRangeListResponse();
        Iterable<ProductRange> list = productDao.findActiveProductRange();
        response.setListProducts(list);
        response.setStatus(statusSuccess());
        return response;
    }

}
