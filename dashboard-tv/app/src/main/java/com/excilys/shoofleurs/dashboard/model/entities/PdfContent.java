package com.excilys.shoofleurs.dashboard.model.entities;


import com.excilys.shoofleurs.dashboard.model.json.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Represents a PDF file with an extra property.
 */
public class PdfContent extends AbstractContent {

    /**
     * Duration display of each page. By default, 10.
     */
    @JsonProperty("durationPerPage")
    @JsonView(Views.FullContent.class)
    private int mDurationPerPage = 10;

    public PdfContent() {
        super();
    }

    public PdfContent(String title) {
        super(title);
    }

    public PdfContent(String title, String url) {
        super(title, url);
    }

    public int getDurationPerPage() {
        return mDurationPerPage;
    }

    public void setDurationPerPage(int durationPerPage) {
        mDurationPerPage = durationPerPage;
    }
}
