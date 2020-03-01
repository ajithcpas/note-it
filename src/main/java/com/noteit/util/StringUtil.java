package com.noteit.util;

public final class StringUtil
{
    private StringUtil()
    {
    }

    public static boolean containsIgnoreCase(String source, String substring)
    {
        return source.toLowerCase().contains(substring.toLowerCase());
    }

    public static int ignoreCaseIndexOf(String source, String substring)
    {
        return source.toLowerCase().indexOf(substring.toLowerCase());
    }

    public static boolean isBlank(String text)
    {
        return text == null || text.trim().isEmpty();
    }
}
