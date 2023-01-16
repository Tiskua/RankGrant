package me.tiskua.rankgrant.Logs;

public class LogsManager {

	private int page = 0;
	private boolean showExpired = true;
	private boolean showOld = true;
	private String filterTargetName = "No Current Filter";
	private String filterGranterName = "No Current Filter";
	private int sort = 0;
	boolean isFilteringTargetName = false;
	boolean isFilteringGranterName = false;

	public int getPage() {
		return this.page;
	}

	public boolean isShowExpired() {
		return this.showExpired;
	}

	public boolean isShowOld() {
		return this.showOld;
	}

	public String getFilterTargetName() {
		return this.filterTargetName;
	}

	public String getFilterGranterName() {
		return this.filterGranterName;
	}

	public int getSort() {
		return this.sort;
	}

	public boolean isFilteringTargetName() {
		return this.isFilteringTargetName;
	}

	public boolean isFilteringGranterName() {
		return this.isFilteringGranterName;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setShowExpired(boolean showExpired) {
		this.showExpired = showExpired;
	}

	public void setShowOld(boolean showOld) {
		this.showOld = showOld;
	}

	public void setFilterTargetName(String filterTargetName) {
		this.filterTargetName = filterTargetName;
	}

	public void setFilterGranterName(String filterGranterName) {
		this.filterGranterName = filterGranterName;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setFilteringTargetName(boolean isFilteringTargetName) {
		this.isFilteringTargetName = isFilteringTargetName;
	}

	public void setFilteringGranterName(boolean isFilteringGranterName) {
		this.isFilteringGranterName = isFilteringGranterName;
	}
}