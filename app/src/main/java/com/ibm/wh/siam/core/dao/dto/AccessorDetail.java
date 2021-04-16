/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.dao.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Match Grun
 *
 */
@Data
@NoArgsConstructor
public class AccessorDetail
implements Serializable
{
    private static final long serialVersionUID = -1L;
    private String accessorId;
    private String accessorType;
    private String accessorCode;
    private String accessorName;
}
