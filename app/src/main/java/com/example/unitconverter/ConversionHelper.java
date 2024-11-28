package com.example.unitconverter;

import java.util.HashMap;
import java.util.Map;

public class ConversionHelper {
    // Map to store conversion rates between units
    private static final Map<String, Double> conversionRates = new HashMap<>();

    static {
        // Populate conversion rates for the units
        conversionRates.put("m_mm", 1000.0);
        conversionRates.put("m_mile", 0.000621371);
        conversionRates.put("m_foot", 3.28084);
        conversionRates.put("m_inch", 39.3701);

        conversionRates.put("mm_m", 0.001);
        conversionRates.put("mm_mile", 0.000000621371);
        conversionRates.put("mm_foot", 0.00328084);
        conversionRates.put("mm_inch", 0.0393701);

        conversionRates.put("mile_m", 1609.34);
        conversionRates.put("mile_mm", 1609344.0);
        conversionRates.put("mile_foot", 5280.0);
        conversionRates.put("mile_inch", 63360.0);

        conversionRates.put("foot_m", 0.3048);
        conversionRates.put("foot_mm", 304.8);
        conversionRates.put("foot_mile", 0.000189394);
        conversionRates.put("foot_inch", 12.0);

        // New conversion rates involving inches
        conversionRates.put("inch_m", 0.0254);
        conversionRates.put("inch_mm", 25.4);
        conversionRates.put("inch_mile", 0.0000157828);
        conversionRates.put("inch_foot", 0.0833333);
    }

    public static double convert(String fromUnit, String toUnit, double value) {
        if (fromUnit.equals(toUnit)) {
            return value; // If fromUnit and toUnit are the same, no conversion needed
        }

        String key = fromUnit + "_" + toUnit;
        if (conversionRates.containsKey(key)) {
            return value * conversionRates.get(key);
        } else {
            throw new IllegalArgumentException("Conversion rate not found for " + fromUnit + " to " + toUnit);
        }
    }

}
