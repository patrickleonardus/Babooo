package com.bantoo.babooo.Model;

import java.util.Comparator;

public class ReportComparator implements Comparator<Report> {

    @Override
    public int compare(Report t1, Report t2) {
        return Long.compare(t2.getReportTimeStamp(), t1.getReportTimeStamp());
    }
}
