package com.redhat.iot.trackman.datagen.util;

import org.json.simple.JSONObject;

public final class JsonUtil {

    private static final double DEFAULT_DOUBLE = 0;
    private static final int DEFAULT_INT = 0;

    public static double toDouble( final JSONObject jsonObj, final String key ) {
        final Object value = jsonObj.get( key );
        return toDoubleValue( value );
    }

    public static double toDoubleValue( final Object value ) {
        if ( value == null ) {
            return DEFAULT_DOUBLE;
        }

        if ( value instanceof String ) {
            final String txt = ( String )value;

            if ( txt.isEmpty() ) {
                return DEFAULT_DOUBLE;
            }

            return Double.parseDouble( txt );
        }

        if ( value instanceof Number ) {
            return ( ( Number )value ).doubleValue();
        }

        throw new RuntimeException( "Unable to convert value to a double" );
    }

    public static int toInt( final JSONObject jsonObj, final String key ) {
        final Object value = jsonObj.get( key );

        if ( value == null ) {
            return DEFAULT_INT;
        }

        if ( value instanceof String ) {
            final String txt = ( String )value;

            if ( txt.isEmpty() ) {
                return DEFAULT_INT;
            }

            return Integer.parseInt( txt );
        }

        if ( value instanceof Number ) {
            return ( ( Number )value ).intValue();
        }

        throw new RuntimeException( "Unable to convert value to an int" );
    }

    /**
     * Don't allow construction outside of this class.
     */
    private JsonUtil() {
        // nothing to do
    }
}
