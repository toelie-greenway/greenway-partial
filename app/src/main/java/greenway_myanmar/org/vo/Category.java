package greenway_myanmar.org.vo;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Entity(primaryKeys = {"id", "parentId"})
public class Category implements Serializable {

    public static final String UNKNOWN_ID = "-1";
    public static final String TYPE_AGRI = "agri";
    public static final String TYPE_LIVESTOCK = "livestock";
    public static final String TYPE_AQUACULTURE = "fishery";
    @NonNull private String id;
    private String title;
    private String description;
    private int order;

    @SerializedName("type")
    private String type;

    @SerializedName("image")
    private String image;

    @NonNull
    @SerializedName("parent_id")
    private String parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @NonNull
    public String getParentId() {
        return parentId;
    }

    public void setParentId(@NonNull String parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isAgriCategory() {
        return TYPE_AGRI.equals(type);
    }

    public boolean isLivestockCategory() {
        return TYPE_LIVESTOCK.equals(type);
    }

    public boolean isAquaCategory() {
        return TYPE_AQUACULTURE.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (order != category.order) return false;
        if (!id.equals(category.id)) return false;
        if (title != null ? !title.equals(category.title) : category.title != null) return false;
        if (description != null
                ? !description.equals(category.description)
                : category.description != null) return false;
        if (type != null ? !type.equals(category.type) : category.type != null) return false;
        if (image != null ? !image.equals(category.image) : category.image != null) return false;
        return parentId != null ? parentId.equals(category.parentId) : category.parentId == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + order;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_AGRI, TYPE_LIVESTOCK, TYPE_AQUACULTURE})
    public @interface CategoryType {}
}
