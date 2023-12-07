package entity;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public class Item {
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0); // For generating unique item IDs
	private int itemId;
	private String jobTitle;
	private String employerName;
	private String employerLogo;
	private String jobApplyLink;
	private String jobCity;
	private String jobState;

	public String getItemId() {
		return String.valueOf(itemId);
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getEmployerLogo() {
		return employerLogo;
	}

	public void setEmployerLogo(String employerLogo) {
		this.employerLogo = employerLogo;
	}

	public String getJobApplyLink() {
		return jobApplyLink;
	}

	public void setJobApplyLink(String jobApplyLink) {
		this.jobApplyLink = jobApplyLink;
	}

	public String getJobCity() {
		return jobCity;
	}

	public void setJobCity(String jobCity) {
		this.jobCity = jobCity;
	}

	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	private Item(ItemBuilder builder) {
		this.itemId = ID_GENERATOR.getAndIncrement();;
		this.jobTitle = builder.jobTitle;
		this.employerName = builder.employerName;
		this.employerLogo = builder.employerLogo;
		this.jobApplyLink = builder.jobApplyLink;
		this.jobCity = builder.jobCity;
		this.jobState = builder.jobState;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("item_id", itemId);
		obj.put("job_title", jobTitle);
		obj.put("employer_name", employerName);
		obj.put("employer_logo", employerLogo);
		obj.put("job_apply_link", jobApplyLink);
		obj.put("job_city", jobCity);
		obj.put("job_state", jobState);
		return obj;
	}

	public static class ItemBuilder {
		private String jobTitle;
		private String employerName;
		private String employerLogo;
		private String jobApplyLink;
		private String jobCity;
		private String jobState;

		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}

		public void setEmployerName(String employerName) {
			this.employerName = employerName;
		}

		public void setEmployerLogo(String employerLogo) {
			this.employerLogo = employerLogo;
		}

		public void setJobApplyLink(String jobApplyLink) {
			this.jobApplyLink = jobApplyLink;
		}

		public void setJobCity(String jobCity) {
			this.jobCity = jobCity;
		}

		public void setJobState(String jobState) {
			this.jobState = jobState;
		}

		public Item build() {
			return new Item(this);
		}
	}
}
