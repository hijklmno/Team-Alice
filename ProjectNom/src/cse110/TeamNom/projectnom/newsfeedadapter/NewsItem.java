package cse110.TeamNom.projectnom.newsfeedadapter;

public class NewsItem {

	private String headline;
	private String reporterName;
	private String date;
	private String url;
	private boolean report_image;
	private byte[] bytes;
	private String name;
	private String ID;
	public boolean book_mark = false;
	public boolean like = false;
	
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
	
	
	/* David's Stuff */
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setFile(byte[] my_bytes)
	{
		this.bytes = my_bytes;
	}
	
	public byte[] getFile()
	{
		return bytes;
	}
	
	public void setId(String pID)
	{
		ID = pID;
	}
	
	public String getID() {
		return this.ID;
	}

}
