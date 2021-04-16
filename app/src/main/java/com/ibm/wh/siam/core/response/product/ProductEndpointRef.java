/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.product;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class ProductEndpointRef
implements Serializable
{
    private static final long serialVersionUID = -1L;

   private String endpointId;
   private String productId;
   private String endpointUrl;
   private String productCode;

}
