package greenway_myanmar.org.vo;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.greenwaymyanmar.common.data.api.v1.gson.Exclude;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import greenway_myanmar.org.db.converter.PostCropTypeConverter;
import greenway_myanmar.org.util.MyanmarZarConverter;

@Entity
@TypeConverters({PostCropTypeConverter.class})
public class Post implements Serializable {

    @PrimaryKey @NonNull private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("slug")
    private String slug;

    @SerializedName("cover")
    private String coverImageUrl;

    @SerializedName("cover_title")
    private String coverImageTitle;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("description")
    private String description;

    @SerializedName("useful_count")
    private int usefulCount;

    @SerializedName("not_useful_count")
    private int notUsefulCount;

    @SerializedName("youtube_iframe")
    private String youtubeIFrame;

    @SerializedName("audio_file")
    private String audioFile;

    @SerializedName("view_counts")
    private int viewCount;

    @SerializedName("url")
    private String url;

    @SerializedName("download_link")
    private String downloadLink;

    @SerializedName("author")
    @Embedded(prefix = "author_")
    private User author;

    @SerializedName("category")
    @Embedded(prefix = "category_")
    private Category category;
    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("saved")
    private boolean saved;
    @Exclude private List<Integer> cropTypeIds;
    @SerializedName("comments_count")
    private int commentCount;

    @SerializedName("is_loved")
    private boolean isLoved;

    @SerializedName("is_liked")
    private boolean isLiked;
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageTitle() {
        return coverImageTitle;
    }

    public void setCoverImageTitle(String coverImageTitle) {
        this.coverImageTitle = coverImageTitle;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUsefulCount() {
        return usefulCount;
    }

    public void setUsefulCount(int usefulCount) {
        this.usefulCount = usefulCount;
    }

    public int getNotUsefulCount() {
        return notUsefulCount;
    }

    public void setNotUsefulCount(int notUsefulCount) {
        this.notUsefulCount = notUsefulCount;
    }

    public String getYoutubeIFrame() {
        return youtubeIFrame;
    }

    public void setYoutubeIFrame(String youtubeIFrame) {
        this.youtubeIFrame = youtubeIFrame;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrettyTime() {
        if (createdAt == null) return "";

        PrettyTime t = new PrettyTime(new Date(), new Locale("MM"));
        return MyanmarZarConverter.toMyanmarNumber(t.format(createdAt));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (usefulCount != post.usefulCount) return false;
        if (notUsefulCount != post.notUsefulCount) return false;
        if (viewCount != post.viewCount) return false;
        if (saved != post.saved) return false;
        if (commentCount != post.commentCount) return false;
        if (isLoved != post.isLoved) return false;
        if (isLiked != post.isLiked) return false;
        if (!id.equals(post.id)) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (slug != null ? !slug.equals(post.slug) : post.slug != null) return false;
        if (coverImageUrl != null
                ? !coverImageUrl.equals(post.coverImageUrl)
                : post.coverImageUrl != null) return false;
        if (coverImageTitle != null
                ? !coverImageTitle.equals(post.coverImageTitle)
                : post.coverImageTitle != null) return false;
        if (shortDescription != null
                ? !shortDescription.equals(post.shortDescription)
                : post.shortDescription != null) return false;
        if (description != null ? !description.equals(post.description) : post.description != null)
            return false;
        if (youtubeIFrame != null
                ? !youtubeIFrame.equals(post.youtubeIFrame)
                : post.youtubeIFrame != null) return false;
        if (audioFile != null ? !audioFile.equals(post.audioFile) : post.audioFile != null)
            return false;
        if (url != null ? !url.equals(post.url) : post.url != null) return false;
        if (downloadLink != null
                ? !downloadLink.equals(post.downloadLink)
                : post.downloadLink != null) return false;
        if (author != null ? !author.equals(post.author) : post.author != null) return false;
        if (category != null ? !category.equals(post.category) : post.category != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (coverImageUrl != null ? coverImageUrl.hashCode() : 0);
        result = 31 * result + (coverImageTitle != null ? coverImageTitle.hashCode() : 0);
        result = 31 * result + (shortDescription != null ? shortDescription.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + usefulCount;
        result = 31 * result + notUsefulCount;
        result = 31 * result + (youtubeIFrame != null ? youtubeIFrame.hashCode() : 0);
        result = 31 * result + (audioFile != null ? audioFile.hashCode() : 0);
        result = 31 * result + viewCount;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (downloadLink != null ? downloadLink.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (saved ? 1 : 0);
        result = 31 * result + (cropTypeIds != null ? cropTypeIds.hashCode() : 0);
        result = 31 * result + commentCount;
        result = 31 * result + (isLoved ? 1 : 0);
        result = 31 * result + (isLiked ? 1 : 0);
        return result;
    }
}
