package vue.utils;

public enum Colors {
    COLOR_1("344E41"),
    COLOR_2("3A5A40"),
    COLOR_3("588157"),
    COLOR_4("A3B18A"),
    COLOR_5("DAD7CD"),
    COLOR_6("E1FFDB");

    private final String hexCode;

    Colors(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return getHexCode(false);
    }

    public String getHexCode(boolean withHashtag) {
        return hexCode;
    }
}
