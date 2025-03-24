package me.zacharycram.cosmiccraftweapons.utility;

import org.bukkit.ChatColor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String getCurrentDate(String pattern) {
        LocalDate localDate = LocalDate.now();

        DateTimeFormatter dateTimeFormatter;
        try {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        } catch (UnsupportedTemporalTypeException exception) {
            throw new NullPointerException("Pattern format is invalid.");
        }

        return localDate.format(dateTimeFormatter);
    }

    public static String getCurrentTime(String pattern) {
        LocalTime localTime = LocalTime.now();

        DateTimeFormatter dateTimeFormatter;
        try {
            dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        } catch (UnsupportedTemporalTypeException exception) {
            throw new NullPointerException("Pattern format is invalid.");
        }

        return localTime.format(dateTimeFormatter);
    }

    public static String color(String msg) {
        if (msg == null) return null;
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> color(List<String> list) {
        if (list == null) return null;
        List<String> colored = new ArrayList<>();
        for (String s : list) {
            colored.add(color(s));
        }
        return colored;
    }

    public static String stripColoredText(String text) {
        return text == null ? null : ChatColor.stripColor(text);
    }
}
