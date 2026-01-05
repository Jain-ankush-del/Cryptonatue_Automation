package framework.elements.styles;

public enum Styles {

    AFTER("::after"),
    BACKGROUND("background"),
    BACKGROUND_IMAGE("background-image"),
    BEFORE("::before"),
    Z_INDEX("z-index");

    private final String value;

    Styles(String style) {
        this.value = style;
    }

    public String getValue() {
        return value;
    }
}