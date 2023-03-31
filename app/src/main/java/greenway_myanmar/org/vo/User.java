package greenway_myanmar.org.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.gson.Exclude;

@Entity
public class User implements Parcelable {

    public static final int INVALID_USER_ID = -1;
    public static final Creator<User> CREATOR =
            new Creator<User>() {
                @Override
                public User createFromParcel(Parcel source) {
                    return new User(source);
                }

                @Override
                public User[] newArray(int size) {
                    return new User[size];
                }
            };

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("is_author")
    @Expose
    private boolean isAuthor;

    @SerializedName("role")
    private String role;

    @Exclude private boolean hasBook;

    @SerializedName("app_version")
    @Expose
    private String appVersion;

    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;

    @SerializedName("is_shop_owner")
    private boolean isShopOwner;
    // TODO: Check the usage and delete if there is no usage
    @SerializedName("information_status")
    @Expose
    private String Information_status;

    @Exclude private String avatarFilePath;

    public User() {}

    protected User(Parcel in) {
        this.id = in.readInt();
        this.email = in.readString();
        this.username = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.phone = in.readString();
        this.role = in.readString();
        this.isAuthor = in.readByte() != 0;
        this.hasBook = in.readByte() != 0;
        this.appVersion = in.readString();
        this.firebaseToken = in.readString();
        this.isShopOwner = in.readByte() != 0;
        this.Information_status = in.readString();
        this.avatarFilePath = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean hasBook() {
        return hasBook;
    }

    public void setHasBook(boolean hasBook) {
        this.hasBook = hasBook;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public boolean isShopOwner() {
        return isShopOwner;
    }

    public void setShopOwner(boolean shopOwner) {
        isShopOwner = shopOwner;
    }

    public String getInformation_status() {
        return Information_status;
    }

    public void setInformation_status(String information_status) {
        Information_status = information_status;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public boolean isNewUser() {
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        // used in BookShelf author list spinner
        return !TextUtils.isEmpty(name) ? name : username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isTechnician() {
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.email);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.phone);
        dest.writeString(this.role);
        dest.writeByte(this.isAuthor ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasBook ? (byte) 1 : (byte) 0);
        dest.writeString(this.appVersion);
        dest.writeString(this.firebaseToken);
        dest.writeByte(this.isShopOwner ? (byte) 1 : (byte) 0);
        dest.writeString(this.Information_status);
        dest.writeString(this.avatarFilePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (isAuthor != user.isAuthor) return false;
        if (hasBook != user.hasBook) return false;
        if (isShopOwner != user.isShopOwner) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        if (appVersion != null ? !appVersion.equals(user.appVersion) : user.appVersion != null)
            return false;
        if (firebaseToken != null
                ? !firebaseToken.equals(user.firebaseToken)
                : user.firebaseToken != null) return false;
        if (Information_status != null
                ? !Information_status.equals(user.Information_status)
                : user.Information_status != null) return false;
        return avatarFilePath != null
                ? avatarFilePath.equals(user.avatarFilePath)
                : user.avatarFilePath == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (isAuthor ? 1 : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (hasBook ? 1 : 0);
        result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
        result = 31 * result + (firebaseToken != null ? firebaseToken.hashCode() : 0);
        result = 31 * result + (isShopOwner ? 1 : 0);
        result = 31 * result + (Information_status != null ? Information_status.hashCode() : 0);
        result = 31 * result + (avatarFilePath != null ? avatarFilePath.hashCode() : 0);
        return result;
    }
}
