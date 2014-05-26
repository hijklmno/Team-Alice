package cse110.TeamNom.projectnom.newsfeedadapter;

public class NewsItem {

	private String headline;
	private String reporterName;
	private String date;
	private String url;
	private boolean report_image;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "[ headline=" + headline + ", reporter Name=" + reporterName
		+ " , date=" + date + "]";
	}

	public boolean setReport(boolean report_image) {
		this.report_image = report_image;
		return report_image;
	}

	public boolean getReport() {
		return report_image;
	}

}
