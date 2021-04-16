package com.ibm.wh.siam.core.common;

/*
 * Class Name:  RecordUpdater.java
 * Description: Implements a record updater for save and modify requests.
 *
 * Copyright 2020 IBM Watson Health.
 */

/**
 * Implements a record updater for save and modify requests.
 *
 * @author Match Grun
 */
public class RecordUpdater
implements RecordUpdaterIF, java.io.Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Username making update.
     */
    protected String name = null;

    /**
     * Application making update.
     */
    protected String application = null;

    /**
     * Default constructor.
     */
    public RecordUpdater() {
    }

    /**
     * Construct from name and application.
     * @param name
     * @param application
     */
    public RecordUpdater(
        final String name,
        final String application )
    {
        this.name = name;
        this.application = application;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer( 100 );
        String cls = this.getClass().getName();
        int iPos = cls.lastIndexOf('.');
        sb.append( cls.substring(1+iPos)).append( ":{ " );
        sb.append( name ).append( "; " );
        sb.append( application ).append( " }" );
        return sb.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public void setApplication(final String application) {
        this.application = application;
    }

}

// =============================================================================
// End of Source.
// =============================================================================
//