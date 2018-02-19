package ru.ratanov.kinoman.model.query;

import java.util.ArrayList;

public class FilterParams {

    private String paramKey;
    private String id;
    private String name;

    private FilterParams(String paramKey, String id, String name) {
        this.paramKey = paramKey;
        this.id = id;
        this.name = name;
    }

    public String getParamKey() {
        return paramKey;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static final ArrayList<FilterParams> CATEGORY_PARAMS = new ArrayList<FilterParams>() {{
        add(new FilterParams("t", "0", "Избранные раздачи"));
        add(new FilterParams("t", "1", "Избранные фильмы"));
        add(new FilterParams("t", "101", "|- Комедии"));
        add(new FilterParams("t", "102", "|- Фантастика, фэнтэзи"));
        add(new FilterParams("t", "103", "|- Ужасы, мистика"));
        add(new FilterParams("t", "104", "|- Боевик, военный"));
        add(new FilterParams("t", "105", "|- Триллер, детектив"));
        add(new FilterParams("t", "106", "|- Драма, мелодрама"));
        add(new FilterParams("t", "107", "|- Наше кино"));
        add(new FilterParams("t", "108", "|- Детский, семейный"));
        add(new FilterParams("t", "110", "|- Приключения"));
        add(new FilterParams("t", "111", "|- Истроический"));
        add(new FilterParams("t", "112", "|- Документальный"));
        add(new FilterParams("t", "113", "|- Классика, театр, опера, балет"));
        add(new FilterParams("t", "115", "|- Концерты"));
        add(new FilterParams("t", "116", "|- Спорт"));
        add(new FilterParams("t", "2", "Избранные мультфильмы"));
        add(new FilterParams("t", "21", "|- Русские мультфильмы"));
        add(new FilterParams("t", "22", "|- Буржуйские мультфильмы"));
        add(new FilterParams("t", "23", "|- Аниме"));
        add(new FilterParams("t", "3", "Избранные сериалы"));
        add(new FilterParams("t", "31", "|- Русские сериалы"));
        add(new FilterParams("t", "32", "|- Буржуйские сериалы"));
    }};

    public static final ArrayList<FilterParams> YEAR_PARAMS = new ArrayList<FilterParams>() {{
        add(new FilterParams("d", "0", "Все года"));
        add(new FilterParams("d", "11", "2016-2017"));
        add(new FilterParams("d", "10", "2014-2015"));
        add(new FilterParams("d", "1", "2012-2013"));
        add(new FilterParams("d", "2", "2009-2011"));
        add(new FilterParams("d", "3", "2006-2008"));
        add(new FilterParams("d", "4", "2001-2005"));
        add(new FilterParams("d", "5", "1996-2000"));
        add(new FilterParams("d", "6", "1991-1995"));
        add(new FilterParams("d", "7", "1981-1990"));
        add(new FilterParams("d", "8", "1971-1980"));
        add(new FilterParams("d", "9", "1951-1970"));
    }};

    public static final ArrayList<FilterParams> COUNTRY_PARAMS = new ArrayList<FilterParams>() {{
       add(new FilterParams("k", "0", "Все страны"));
       add(new FilterParams("k", "1", "Россия"));
       add(new FilterParams("k", "2", "США"));
       add(new FilterParams("k", "3", "СССР"));
       add(new FilterParams("k", "4", "Франция"));
       add(new FilterParams("k", "5", "Германия"));
       add(new FilterParams("k", "6", "Италия"));
       add(new FilterParams("k", "7", "Великобритания"));
    }};

    public static final ArrayList<FilterParams> FORMAT_PARAMS = new ArrayList<FilterParams>() {{
        add(new FilterParams("f", "0", "Все форматы"));
        add(new FilterParams("f", "1", "DVD"));
        add(new FilterParams("f", "2", "HD"));
        add(new FilterParams("f", "4", "3D"));
        add(new FilterParams("f", "3", "LossLess"));
    }};

    public static final ArrayList<FilterParams> ADDED_PARAMS = new ArrayList<FilterParams>() {{
        add(new FilterParams("w", "0", "За все время"));
        add(new FilterParams("w", "1", "За неделю"));
        add(new FilterParams("w", "2", "За месяц"));
        add(new FilterParams("w", "3", "За 3 месяца"));
    }};

    public static final ArrayList<FilterParams> SORT_PARAMS = new ArrayList<FilterParams>() {{
        add(new FilterParams("s", "0", "По сидам"));
        add(new FilterParams("s", "1", "По пирам"));
        add(new FilterParams("s", "2", "По комментариям"));
    }};

    public static int getCurrentPosition(ArrayList<FilterParams> filterParams, String value) {
        for (int i = 0; i < filterParams.size(); i++) {
            if (filterParams.get(i).getId().equals(value)) {
                return i;
            }
        }
        return 0;
    }

    public static String getTabTitle(String value) {
        for (int i = 0; i < CATEGORY_PARAMS.size(); i++) {
            if (CATEGORY_PARAMS.get(i).getId().equals(value)) {
                String title = CATEGORY_PARAMS.get(i).getName();
                if (title.startsWith("|- ")) {
                    return title.substring(3);
                } else {
                    return title;
                }
            }
        }
        return null;
    }
}
