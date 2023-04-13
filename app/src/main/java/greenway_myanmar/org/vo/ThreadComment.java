package greenway_myanmar.org.vo;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.gson.Exclude;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import greenway_myanmar.org.R;
import greenway_myanmar.org.util.MyanmarZarConverter;
import greenway_myanmar.org.util.StickerUtil;

@Entity(primaryKeys = {"id"})
public class ThreadComment {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_UPLOADING = "uploading";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_DELETING = "deleting";
    public static final String STATUS_UPDATING = "updating";

    @SerializedName("id")
    @NonNull
    private String id;

    @SerializedName("body")
    private String body;

    @SerializedName("image")
    private String imageUrl;

    @Exclude private String imageFilePath;

    @SerializedName("likes")
    private int likeCount;

    @SerializedName("sticker_id")
    private String stickerId;

    @SerializedName("user")
    @Embedded(prefix = "user_")
    private User user;

    @SerializedName("thread_id")
    private String threadId;

    @SerializedName("liked")
    private boolean isLiked;

    @SerializedName("created_at")
    private Date createdAt;

    @Exclude private char commentType;
    @Exclude private String status;
    @Exclude private String clientId;
    @Exclude private UUID workerId;

    @SerializedName("is_recommended")
    private boolean isRecommended;

    @SerializedName("recommended_count")
    private int recommendedCount;

    @SerializedName("is_solved")
    private boolean isSolved;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public int getRecommendedCount() {
        return recommendedCount;
    }

    public void setRecommendedCount(int recommendedCount) {
        this.recommendedCount = recommendedCount;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public char getCommentType() {
        return commentType;
    }

    public void setCommentType(char commentType) {
        this.commentType = commentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public UUID getWorkerId() {
        return workerId;
    }

    public void setWorkerId(UUID workerId) {
        this.workerId = workerId;
    }

    public String getPrettyTime() {
        if (createdAt == null) return "";

        PrettyTime t = new PrettyTime(new Date(), new Locale("MM"));
        return MyanmarZarConverter.toMyanmarNumber(t.format(createdAt));
    }

    public String getStatusMessage() {
        if (STATUS_PENDING.equals(status)) {
            return "လိုင်းရလျှင် အလိုအလျောက် တင်သွားပါမည်";
        } else if (STATUS_UPLOADING.equals(status)) {
            return "တင်နေပါသည် ...";
        } else if (STATUS_UPDATING.equals(status)) {
            return "လိုင်းရလျှင် အလိုအလျောက် ပြင်သွားပါမည် ...";
        } else if (STATUS_DELETING.equals(status)) {
            return "လိုင်းရလျှင် အလိုအလျောက် ဖျက်သွားပါမည် ...";
        } else {
            return getPrettyTime();
        }
    }

    public @ColorRes int getStatusMessageColor() {
        if (STATUS_PENDING.equals(status)) {
            return R.color.theme_accent;
        } else if (STATUS_UPLOADING.equals(status) || STATUS_UPDATING.equals(status)) {
            return R.color.theme_primary;
        } else if (STATUS_DELETING.equals(status)) {
            return R.color.theme_accent_fallback;
        } else {
            return R.color.app_secondary_text;
        }
    }

    public boolean isPending() {
        return STATUS_PENDING.equals(status)
                || STATUS_UPLOADING.equals(status)
                || STATUS_DELETING.equals(status)
                || STATUS_UPDATING.equals(status);
    }

    public Sticker getSticker() {
        return StickerUtil.getStickerById(stickerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadComment that = (ThreadComment) o;
        return likeCount == that.likeCount
                && isLiked == that.isLiked
                && commentType == that.commentType
                && isRecommended == that.isRecommended
                && recommendedCount == that.recommendedCount
                && isSolved == that.isSolved
                && id.equals(that.id)
                && Objects.equals(body, that.body)
                && Objects.equals(imageUrl, that.imageUrl)
                && Objects.equals(imageFilePath, that.imageFilePath)
                && Objects.equals(stickerId, that.stickerId)
                && Objects.equals(user, that.user)
                && Objects.equals(threadId, that.threadId)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(status, that.status)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(workerId, that.workerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                body,
                imageUrl,
                imageFilePath,
                likeCount,
                stickerId,
                user,
                threadId,
                isLiked,
                createdAt,
                commentType,
                status,
                clientId,
                workerId,
                isRecommended,
                recommendedCount,
                isSolved);
    }

    // Thread Status, 'pending', 'deleting'
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({STATUS_PENDING, STATUS_DELETING, STATUS_UPLOADING, STATUS_SUCCESS})
    public @interface Status {}
}
