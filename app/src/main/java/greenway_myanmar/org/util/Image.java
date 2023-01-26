package greenway_myanmar.org.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.gson.Exclude;

import java.util.UUID;

@Entity
public class Image implements Parcelable {
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("link")
    private String link;
    @Exclude
    private String filePath;
    @Exclude
    private Uri uri;

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readString();
        this.url = in.readString();
        this.link = in.readString();
        this.filePath = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static Image from(String url) {
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setUrl(url);
        return image;
    }

    public static Image fromFilePath(String imageFilePath) {
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setFilePath(imageFilePath);
        return image;
    }

    public static Image fromUri(Uri imageUri) {
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setUri(imageUri);
        return image;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.link);
        dest.writeString(this.filePath);
        dest.writeParcelable(this.uri, flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (!id.equals(image.id)) return false;
        if (url != null ? !url.equals(image.url) : image.url != null) return false;
        if (link != null ? !link.equals(image.link) : image.link != null) return false;
        return filePath != null ? filePath.equals(image.filePath) : image.filePath == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        return result;
    }

}
