package concept_based.align_cross_wiki;

/**
 * Created by Sahelsoft on 3/8/2018.
 */
public class WikiObjectTemplate {
    private String title;
    private String id;
    private String text;
    private String crossTitle;
    private String crossId;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrossTitle() {
        return crossTitle;
    }
    public void setCrossTitle(String crossTitle) {
        this.crossTitle = crossTitle;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCrossId() {
        return crossId;
    }
    public void setCrossId(String crossId) {
        this.crossId = crossId;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
