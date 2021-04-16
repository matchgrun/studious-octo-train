/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.content;

import java.io.Serializable;

import com.ibm.wh.siam.core.dao.dto.AccessorDetail;
import com.ibm.wh.siam.core.dto.ContentSetDetail;
import com.ibm.wh.siam.core.response.BaseSiamResponse;

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
public class AccessibleContentSetDetallsResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

//    private String accessorId;
//    private String accessorType;
    private AccessorDetail accessor;
    private Iterable<ContentSetDetail> listContentSets;
}
