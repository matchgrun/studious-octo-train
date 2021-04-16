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
public class ContentSetAccess
extends BaseSiamObject
implements Serializable
{
    private static final long serialVersionUID = -1L;

    private String contentSetId;
    private String contentSetAccessId;
    private String accessorId;

    @SiamString(maxLength=20)
    private String accessorType;

    @SiamString(maxLength=40)
    private String contentAccessId;

    private Date startDate;
    private Date endDate;
}
