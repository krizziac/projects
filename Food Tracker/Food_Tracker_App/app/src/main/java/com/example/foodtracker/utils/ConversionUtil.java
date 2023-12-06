package com.example.foodtracker.utils;

import com.example.foodtracker.model.IngredientUnit.IngredientAmount;
import com.example.foodtracker.model.IngredientUnit.IngredientUnit;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Provides utility for converting ingredient amounts and performing actions such as calculating missing amounts
 */
public abstract class ConversionUtil {

    public static final double KILOGRAM_TO_KILOGRAM_CONVERSION = 1.0;
    public static final double KILOGRAM_TO_GRAM_CONVERSION = 1000.0;
    public static final double KILOGRAM_TO_OUNCE_CONVERSION = 35.274;
    public static final double KILOGRAM_TO_POUND_CONVERSION = 2.205;
    public static final double CUPS_TO_CUPS_CONVERSION = 1.0;
    public static final double CUPS_TO_MILLILITRE_CONVERSION = 236.588;
    public static final double CUPS_TO_LITRE_CONVERSION = 0.2366;
    public static final double CUPS_TO_TEASPOON_CONVERSION = 48.0;
    public static final double CUPS_TO_TABLESPOON_CONVERSION = 16.0;
    public static final double CUPS_TO_OUNCES_CONVERSION = 8.0;

    private static final EnumMap<IngredientUnit, EnumMap<IngredientUnit, Double>> CONVERSION_MAP = getConversionMap();

    /**
     * For weight ingredient units, we use kilogram as our baseline,
     * all other conversions are constructed as a ratio
     * <p>
     * For volume ingredient units, we use cups as our baseline,
     * all other conversions are constructed as a ratio from this
     */
    private static EnumMap<IngredientUnit, EnumMap<IngredientUnit, Double>> getConversionMap() {
        EnumMap<IngredientUnit, EnumMap<IngredientUnit, Double>> conversionMaps = new EnumMap<>(IngredientUnit.class);

        EnumMap<IngredientUnit, Double> kilogramConversions = getKilogramConversions();
        conversionMaps.put(IngredientUnit.KILOGRAM, kilogramConversions);
        conversionMaps.put(IngredientUnit.GRAM, getRelativeConversionMap(kilogramConversions, KILOGRAM_TO_GRAM_CONVERSION));
        conversionMaps.put(IngredientUnit.POUND, getRelativeConversionMap(kilogramConversions, KILOGRAM_TO_POUND_CONVERSION));

        EnumMap<IngredientUnit, Double> cupConversions = getCupConversions();
        conversionMaps.put(IngredientUnit.CUP, cupConversions);
        conversionMaps.put(IngredientUnit.LITRE, getRelativeConversionMap(cupConversions, CUPS_TO_LITRE_CONVERSION));
        conversionMaps.put(IngredientUnit.MILLILITRE, getRelativeConversionMap(cupConversions, CUPS_TO_MILLILITRE_CONVERSION));
        conversionMaps.put(IngredientUnit.TABLESPOON, getRelativeConversionMap(cupConversions, CUPS_TO_TABLESPOON_CONVERSION));
        conversionMaps.put(IngredientUnit.TEASPOON, getRelativeConversionMap(cupConversions, CUPS_TO_TEASPOON_CONVERSION));

        EnumMap<IngredientUnit, Double> ounceConversionMap = new EnumMap<>(IngredientUnit.class);
        ounceConversionMap.putAll(getRelativeConversionMap(kilogramConversions, KILOGRAM_TO_OUNCE_CONVERSION));
        ounceConversionMap.putAll(getRelativeConversionMap(cupConversions, CUPS_TO_OUNCES_CONVERSION));
        conversionMaps.put(IngredientUnit.OUNCE, ounceConversionMap);
        EnumMap<IngredientUnit, Double> unitConversionMap = new EnumMap<>(IngredientUnit.class);
        unitConversionMap.put(IngredientUnit.UNIT, 1.0);
        conversionMaps.put(IngredientUnit.UNIT, unitConversionMap);
        return conversionMaps;
    }

    private static EnumMap<IngredientUnit, Double> getKilogramConversions() {
        EnumMap<IngredientUnit, Double> kilogramConversionMap = new EnumMap<>(IngredientUnit.class);
        kilogramConversionMap.put(IngredientUnit.KILOGRAM, KILOGRAM_TO_KILOGRAM_CONVERSION);
        kilogramConversionMap.put(IngredientUnit.GRAM, KILOGRAM_TO_GRAM_CONVERSION);
        kilogramConversionMap.put(IngredientUnit.OUNCE, KILOGRAM_TO_OUNCE_CONVERSION);
        kilogramConversionMap.put(IngredientUnit.POUND, KILOGRAM_TO_POUND_CONVERSION);
        return kilogramConversionMap;
    }

    private static EnumMap<IngredientUnit, Double> getCupConversions() {
        EnumMap<IngredientUnit, Double> cupConversionMap = new EnumMap<>(IngredientUnit.class);
        cupConversionMap.put(IngredientUnit.CUP, CUPS_TO_CUPS_CONVERSION);
        cupConversionMap.put(IngredientUnit.LITRE, CUPS_TO_LITRE_CONVERSION);
        cupConversionMap.put(IngredientUnit.MILLILITRE, CUPS_TO_MILLILITRE_CONVERSION);
        cupConversionMap.put(IngredientUnit.OUNCE, CUPS_TO_OUNCES_CONVERSION);
        cupConversionMap.put(IngredientUnit.TABLESPOON, CUPS_TO_TABLESPOON_CONVERSION);
        cupConversionMap.put(IngredientUnit.TEASPOON, CUPS_TO_TEASPOON_CONVERSION);
        return cupConversionMap;
    }

    private static EnumMap<IngredientUnit, Double> getRelativeConversionMap(EnumMap<IngredientUnit, Double> relativeConversionMap, double conversionRatio) {
        EnumMap<IngredientUnit, Double> conversionMap = relativeConversionMap.clone();
        for (Map.Entry<IngredientUnit, Double> conversionEntry : conversionMap.entrySet()) {
            conversionMap.put(conversionEntry.getKey(), conversionEntry.getValue() / conversionRatio);
        }
        return conversionMap;
    }

    /**
     * Attempts to convert the {@code from} to the equivalent {@code to} amount
     *
     * @param from {@link IngredientAmount} to convert from
     * @param to   {@link IngredientAmount} to convert to
     * @return a new converted ingredient amount
     * @throws IllegalArgumentException in the case that this conversion is impossible
     */
    public static IngredientAmount convertAmount(IngredientAmount from, IngredientUnit to) throws IllegalArgumentException {
        if (CONVERSION_MAP.containsKey(from.getUnit()) && Objects.requireNonNull(CONVERSION_MAP.get(from.getUnit())).containsKey(to)) {
            Double conversionRatio = Objects.requireNonNull(CONVERSION_MAP.get(from.getUnit())).get(to);
            if (conversionRatio == null) {
                throw new IllegalArgumentException("Conversion is not possible");
            }
            double conversionAmount = from.getAmount() * conversionRatio;
            return new IngredientAmount(to, conversionAmount);
        } else {
            throw new IllegalArgumentException("Conversion is not possible");
        }
    }

    /**
     * Get the amount needed for an ingredient based on an amount {@code owned} and an amount {@code needed}
     * the units of which will be converted to the units of the amount {@code needed}
     *
     * <p>If we have more than the amount that is needed the ingredient amount will be returned as 0.</p>
     *
     * @throws IllegalArgumentException if the conversion between the two amounts is not possible
     */
    public static IngredientAmount getMissingAmount(IngredientAmount owned, IngredientAmount needed) throws IllegalArgumentException {
        IngredientAmount convertedAmount = convertAmount(owned, needed.getUnit());
        convertedAmount.setAmount(Math.max(needed.getAmount() - convertedAmount.getAmount(), 0));
        return convertedAmount;
    }
}
