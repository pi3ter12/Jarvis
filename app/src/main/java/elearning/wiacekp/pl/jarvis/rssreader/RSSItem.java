package elearning.wiacekp.pl.jarvis.rssreader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class RSSItem implements Parcelable {

	private String title;
	private String link;
	private String description;
	private String content;
	private String pubDate;
	private String image; // todo: delete this

	public RSSItem() {
	}

	public RSSItem(Parcel parcel) {
		final Bundle data = parcel.readBundle();

		title = data.getString("title");
		link = data.getString("link");
		description = data.getString("description");
		content = data.getString("content");
		pubDate = data.getString("pubDate");
		image = data.getString("image"); 

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		final Bundle data = new Bundle();

		data.putString("title", title);
		data.putString("link", link);
		data.putString("description", description);
		data.putString("content", content);
		data.putSerializable("pubDate", pubDate);
		data.putString("image", image);

		dest.writeBundle(data);
	}

	public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
		public RSSItem createFromParcel(Parcel data) {
			return new RSSItem(data);
		}

		public RSSItem[] newArray(int size) {
			return new RSSItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	// "SETTERS AND GETTERS"
	// GETTERS

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getContent() {
		return content;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getImage() {
		return image;
	}

	// I SETTERS

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public void setImage(String descriptionWithImage) {
		Pattern p = Pattern.compile(".*<img[^>]*src=\"([^\"]*)",
				Pattern.CASE_INSENSITIVE);

		Matcher m = p.matcher(descriptionWithImage);

		while (m.find()) {
			this.image = m.group(1);	
		}
	}
}