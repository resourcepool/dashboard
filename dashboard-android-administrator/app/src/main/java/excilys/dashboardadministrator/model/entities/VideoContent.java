package excilys.dashboardadministrator.model.entities;


/**
 * Video content. No extra property.
 */
public class VideoContent extends AbstractContent {

	public VideoContent() {
		super();
	}

	public VideoContent(String title) {
		super(title);
	}

	public VideoContent(String title, String url) {
		super(title, url);
	}
}
