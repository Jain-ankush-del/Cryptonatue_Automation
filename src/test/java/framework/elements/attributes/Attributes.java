package framework.elements.attributes;

public enum Attributes {



    CLASS("class"),
    HREF("href"),
    ID("id"),
    NAME("name"),
    PLACEHOLDER("placeholder"),
    REL("rel"),
    SRC("src"),
    TEXT("textContent"),
    VALUE("value"),
    SRCSET("srcset"),
    DATALINK("data-links");

    private final String value;

    Attributes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
