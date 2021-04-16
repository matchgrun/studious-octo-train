/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dto;

import java.io.Serializable;
import java.util.Date;

import com.ibm.wh.siam.core.annotations.SiamString;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Product
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String productId;

    @SiamString(maxLength=50, fieldName = "Product-Code")
    private String productCode;

    @SiamString(maxLength=255)
    private String description;
    private Date startDate;
    private Date endDate;

    private String ldapEntryDN;

}
