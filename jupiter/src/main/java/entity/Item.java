package entity;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Item {
	private String itemId;
	private String jobTitle;
	private String employerName;
	private String employerLogo;
	private String jobApplyLink;
	private String jobLocation;
	private String jobPostDate;
	private Set<String> keywords;

	public String getItemId() {
		return String.valueOf(itemId);
	}

	public void setItemId(String itemId) {
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

	public String getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(String jobLocation) {
		this.jobLocation = jobLocation;
	}

	public String getJobPostDate() {
		return jobPostDate;
	}

	public void setJobPostDate(String jobPostDate) {
		this.jobPostDate = jobPostDate;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	private Item(ItemBuilder builder) {
		this.itemId = builder.itemId;
		this.jobTitle = builder.jobTitle;
		this.employerName = builder.employerName;
		this.employerLogo = builder.employerLogo;
		this.jobApplyLink = builder.jobApplyLink;
		this.jobLocation = builder.jobLocation;
		this.jobPostDate = builder.jobPostDate;
		this.keywords = builder.keywords;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("item_id", itemId);
		obj.put("job_title", jobTitle);
		obj.put("employer_name", employerName);
		obj.put("employer_logo", employerLogo);
		obj.put("job_apply_link", jobApplyLink);
		obj.put("job_location", jobLocation);
		obj.put("job_post_date", jobPostDate);
		obj.put("keywords", new JSONArray(keywords));
		return obj;
	}

	public static class ItemBuilder {
		private String itemId;
		private String jobTitle;
		private String employerName;
		private String employerLogo;
		private String jobApplyLink;
		private String jobLocation;
		private String jobPostDate;
		private Set<String> keywords;

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

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
			setItemId(generateItemId(jobApplyLink));
		}
		private String generateItemId(String url) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] encodedhash = digest.digest(url.getBytes(StandardCharsets.UTF_8));
				return bytesToHex(encodedhash);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}

		private static String bytesToHex(byte[] hash) {
			StringBuilder hexString = new StringBuilder(2 * hash.length);
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		}

		public void setJobLocation(String jobLocation) {
			this.jobLocation = jobLocation;
		}

		public void setJobPostDate(String jobPostDate) {
			this.jobPostDate = DateConverter.convertDateToNewFormat(jobPostDate);
		}

		public void setKeywords(Set<String> keywords) {
			this.keywords = keywords;
		}

		public Item build() {
			return new Item(this);
		}
	}
}
