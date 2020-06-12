package com.bantoo.babooo.Model;

public class Report {

    private Integer reportTimeStamp;
    private String reportDate;
    private String reportType;
    private String reportStatus;
    private String reportStory;
    private String phoneNumber;
    private String reportKey;

    public Integer getReportTimeStamp() { return reportTimeStamp; }

    public void setReportTimeStamp(Integer reportTimeStamp) { this.reportTimeStamp = reportTimeStamp; }

    public String getReportDate() { return reportDate; }

    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public String getReportType() { return reportType; }

    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReportStatus() { return reportStatus; }

    public void setReportStatus(String reportStatus) { this.reportStatus = reportStatus; }

    public String getReportStory() { return reportStory; }

    public void setReportStory(String reportStory) { this.reportStory = reportStory; }

    public String getphoneNumber() { return phoneNumber; }

    public void setphoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getReportKey() { return reportKey; }

    public void setReportKey(String reportKey) { this.reportKey = reportKey; }

    public Report(Integer reportTimeStamp, String reportType, String reportStatus, String reportStory, String phoneNumber) {
        this.reportTimeStamp = reportTimeStamp;
        this.reportType = reportType;
        this.reportStatus = reportStatus;
        this.reportStory = reportStory;
        this.phoneNumber = phoneNumber;
    }
}
