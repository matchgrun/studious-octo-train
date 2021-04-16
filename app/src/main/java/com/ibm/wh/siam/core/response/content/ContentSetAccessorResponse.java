/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.response.content;

import java.io.Serializable;

import com.ibm.wh.siam.core.dto.ContentSet;
import com.ibm.wh.siam.core.dto.ContentSetAccess;
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
public class ContentSetAccessorResponse
extends BaseSiamResponse
implements Serializable
{
    private static final long serialVersionUID = 1L;

    private ContentSet contentSet;

    private Iterable<ContentSetAccess> listAccessors;

}
