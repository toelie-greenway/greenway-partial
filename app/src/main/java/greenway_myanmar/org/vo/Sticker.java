package greenway_myanmar.org.vo;

public class Sticker {

    private String id;

    private int iconResId;

    public Sticker() {}

    public Sticker(String id, int iconResId) {
        this.id = id;
        this.iconResId = iconResId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
