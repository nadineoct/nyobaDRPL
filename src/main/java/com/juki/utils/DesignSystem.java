package com.juki.utils;

import javafx.scene.paint.Color;

public class DesignSystem {
    // Violet Eggplant Palette
    public static final String VIOLET_50 = "#fdf3ff";
    public static final String VIOLET_100 = "#fae7ff";
    public static final String VIOLET_200 = "#f6ceff";
    public static final String VIOLET_300 = "#f3a7ff";
    public static final String VIOLET_400 = "#ee72ff";
    public static final String VIOLET_500 = "#e23df8";
    public static final String VIOLET_600 = "#ca1ddc";
    public static final String VIOLET_700 = "#aa15b6";
    public static final String VIOLET_800 = "#8d1395";
    public static final String VIOLET_900 = "#76157a";
    public static final String VIOLET_950 = "#4f0052";

    // Ripe Lemon Palette
    public static final String LEMON_50 = "#fefce8";
    public static final String LEMON_100 = "#fffbc2";
    public static final String LEMON_200 = "#fff289";
    public static final String LEMON_300 = "#ffe341";
    public static final String LEMON_400 = "#fdd112";
    public static final String LEMON_500 = "#ecb706";
    public static final String LEMON_600 = "#cc8d02";
    public static final String LEMON_700 = "#a36405";
    public static final String LEMON_800 = "#864f0d";
    public static final String LEMON_900 = "#724011";
    public static final String LEMON_950 = "#432105";

    // Neutral & UI Colors from Figma
    public static final String NEUTRAL_900 = "#292929";
    public static final String NEUTRAL_800 = "#434343";
    public static final String NEUTRAL_500 = "#767676";
    public static final String NEUTRAL_400 = "#A5A5A5";
    public static final String NEUTRAL_300 = "#D6D6D6";
    public static final String SUCCESS_GREEN = "#82DD55";
    public static final String STREAK_ORANGE = "#FFA930";
    public static final String BORDER_YELLOW = "#F1B900";
    
    // Utility methods to get Color objects directly
    public static Color getViolet(int shade) {
        return Color.web(getVioletHex(shade));
    }

    public static Color getLemon(int shade) {
        return Color.web(getLemonHex(shade));
    }

    private static String getVioletHex(int shade) {
        return switch (shade) {
            case 50 -> VIOLET_50;
            case 100 -> VIOLET_100;
            case 200 -> VIOLET_200;
            case 300 -> VIOLET_300;
            case 400 -> VIOLET_400;
            case 500 -> VIOLET_500;
            case 600 -> VIOLET_600;
            case 700 -> VIOLET_700;
            case 800 -> VIOLET_800;
            case 900 -> VIOLET_900;
            case 950 -> VIOLET_950;
            default -> VIOLET_800;
        };
    }

    private static String getLemonHex(int shade) {
        return switch (shade) {
            case 50 -> LEMON_50;
            case 100 -> LEMON_100;
            case 200 -> LEMON_200;
            case 300 -> LEMON_300;
            case 400 -> LEMON_400;
            case 500 -> LEMON_500;
            case 600 -> LEMON_600;
            case 700 -> LEMON_700;
            case 800 -> LEMON_800;
            case 900 -> LEMON_900;
            case 950 -> LEMON_950;
            default -> LEMON_300;
        };
    }
}
