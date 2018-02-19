package ru.ratanov.kinoman.model.content;

public class SameItem {

    private String title;
    private String seeds;
    private String size;
    private String date;
    private String pageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuality() {
        String[] params = title.split(" / ");
        int length = params.length;
        return params[length - 1];
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTranslate() {
        String[] params = title.split(" / ");
        String full;
        int length = params.length;
        if (length == 6) {
            full = params[length - 3];
        } else {
            full = params[length - 2];
        }
        if (full.contains("(") && full.contains(")")) {
            int start = full.indexOf("(") - 1;
            int end = full.indexOf(")") + 1;
            String parentheses = full.substring(start, end);
            return full.replace(parentheses, "");
        } else {
            return full;
        }
    }

    public String getSeries() {
        String[] params = title.split(" / ");
        String title = params[0];
        if (title.contains("(") && title.contains(")")) {
            int start = title.lastIndexOf("(") + 1;
            int end = title.lastIndexOf(")");
            return title.substring(start, end);
        } else {
            return "";
        }
    }
}
