/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.request.product;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.Product;
import com.ibm.wh.siam.core.request.Metadata;

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
public class ProductSaveRequest
implements Serializable
{
   private static final long serialVersionUID = 1L;

   private Metadata metadata;
   private Product product;

}
