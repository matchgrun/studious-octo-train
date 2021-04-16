/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Match Grun
 *
 */
@RestController
@RequestMapping( "/" )
public class RootController
{

    private static final Logger logger = LoggerFactory.getLogger( RootController.class );

    @RequestMapping("/")
    public String index() {
        logger.info("Index mapping...");
        return "Pg POC";
    }

}
