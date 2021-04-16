/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.common;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

/**
 * @author Match Grun
 *
 */
public class SiamOwnerTypes
{
    public final static String UNKNOWN = "-unknown-";

    public final static String GROUP = "GROUP";
    public final static String ORGANIZATION = "ORGANIZATION";
    public final static String PRODUCT = "PRODUCT";
    public final static String PERSON = "PERSON";
    public final static String ACCOUNT = "ACCOUNT";
    public final static String CREDENTIAL = "CREDENTIAL";
    public final static String ROLE = "ROLE";
    public final static String CONTENT_SET = "CONTENT-SET";
    public final static String CONTENT_SET_ACCESS = "CONTENT-SET-ACCESS";

    private static Set<String> SET_OWNERS = buildOwners();
    private static String VALUES_ARE = buildValuesAre();

    private static Set<String> buildOwners() {
        Set<String> set = new HashSet<String>();
        set.add(GROUP);
        set.add(ORGANIZATION);
        set.add(PRODUCT);
        set.add(PERSON);
        set.add(ACCOUNT);
        set.add(CREDENTIAL);
        set.add(ROLE);
        return set;
    }

    private static String buildValuesAre() {
        return SET_OWNERS.toString();
    }

    public static boolean isValid( final String ownerType ) {
        if( ownerType == null ) return false;
        return SET_OWNERS.contains(ownerType);
    }

    public static String defaultValue( final String ownerType ) {
        if( ! StringUtils.isEmpty(ownerType) ) {
            return ownerType.toUpperCase();
        }
        return ownerType;
    }

    public static String valuesAre() {
        return VALUES_ARE;
    }

}
