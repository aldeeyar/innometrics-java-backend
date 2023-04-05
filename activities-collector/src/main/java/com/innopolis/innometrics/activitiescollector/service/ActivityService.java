package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.*;
import com.innopolis.innometrics.activitiescollector.entity.Activity;
import com.innopolis.innometrics.activitiescollector.entity.CumulativeReport;
import com.innopolis.innometrics.activitiescollector.entity.Measurement;
import com.innopolis.innometrics.activitiescollector.repository.ActivityRepository;
import com.innopolis.innometrics.activitiescollector.repository.CumulativeReportRepository;
import com.innopolis.innometrics.activitiescollector.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("ActivityService")
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private final ActivityRepository activityRepository;
    private final MeasurementRepository measurementRepository;
    private final CumulativeReportRepository cumulativeReportRepository;

    public void createActivity(ActivityReport activityReport, String userName, Date creationDate) {
        Activity myActivity = new Activity();
        myActivity.setActivityId(activityReport.getActivityID());
        myActivity.setActivityType(activityReport.getActivityType());
        myActivity.setIdleActivity(activityReport.getIdleActivity());
        myActivity.setEmail(activityReport.getUserID());
        myActivity.setStartTime(activityReport.getStartTime());
        myActivity.setEndTime(activityReport.getEndTime());
        myActivity.setExecutableName(activityReport.getExecutableName());
        myActivity.setBrowserUrl(activityReport.getBrowserUrl()
                .substring(0, Math.min(activityReport.getBrowserUrl().length(), 1024)));
        myActivity.setBrowserTitle(activityReport.getBrowserTitle()
                .substring(0, Math.min(activityReport.getBrowserTitle().length(), 1024)));
        myActivity.setIpAddress(activityReport.getIpAddress());
        myActivity.setMacAddress(activityReport.getMacAddress());
        myActivity.setPid(activityReport.getPid());
        myActivity.setOsVersion(activityReport.getOsVersion());
        myActivity.setCreationDate(creationDate);
        myActivity.setCreatedBy(userName);
        myActivity = activityRepository.save(myActivity);
        for (MeasurementReport m : activityReport.getMeasurements()) {
            Measurement myMeasurement = new Measurement();
            myMeasurement.setActivity(myActivity);
            try {
                myMeasurement.setMeasurementTypeId(Integer.parseInt(m.getMeasurementTypeId()));
            } catch (Exception e) {
                myMeasurement.setMeasurementTypeId(1);
            }
            myMeasurement.setValue(m.getValue());
            myMeasurement.setCreationDate(creationDate);
            myMeasurement.setCreatedBy(userName);
            myActivity.getMeasurements().add(myMeasurement);
        }
        measurementRepository.saveAll(myActivity.getMeasurements());
    }

    public boolean deleteActivity(Integer activityID, String username) {
        activityRepository.existsById(activityID);
        activityRepository.deleteById(activityID);
        return true;
    }

    public boolean deleteActivitiesWithIds(Integer[] ids) {
        activityRepository.deleteActivitiesWithIds(Arrays.asList(ids));
        return true;
    }

    public ActivitiesReportResponse getActivitiesByEmailAndDay(String userName, Date reportDate) {
        List<IActivitiesReportByUserAndDay> myActivities;
        myActivities = activityRepository.getActivitiesPerDay(userName, formatter.format(reportDate));
        ActivitiesReportResponse respose = new ActivitiesReportResponse();
        for (IActivitiesReportByUserAndDay activityReport : myActivities) {
            Activity myActivity = new Activity();
            myActivity.setActivityId(activityReport.getActivityID());
            myActivity.setActivityType(activityReport.getActivityType());
            myActivity.setIdleActivity(activityReport.getIdleActivity());
            myActivity.setEmail(activityReport.getEmail());
            myActivity.setStartTime(activityReport.getStartTime());
            myActivity.setEndTime(activityReport.getEndTime());
            myActivity.setExecutableName(activityReport.getExecutableName());
            myActivity.setBrowserUrl(activityReport.getBrowserUrl());
            myActivity.setBrowserTitle(activityReport.getBrowserTitle());
            myActivity.setIpAddress(activityReport.getIpAddress());
            myActivity.setMacAddress(activityReport.getMacAddress());
            myActivity.setPid(activityReport.getPid());
            myActivity.setOsVersion(activityReport.getOsVersion());
            myActivity.setCreationDate(activityReport.getCreationdate());
            myActivity.setCreatedBy(activityReport.getCreatedBy());
            respose.getReport().add(myActivity);
        }
        return respose;
    }

    public ActivitiesReportByUserResponse getActivitiesReportByUser(ActivitiesReportByUserRequest request) {
        List<IActivitiesReportByUser> result = activityRepository.getActivitiesReport(
                request.getProjectID(),
                request.getEmail(),
                (request.getMinDate() != null ? formatter.format(request.getMinDate()) : null),
                (request.getMaxDate() != null ? formatter.format(request.getMaxDate()) : null)
        );
        ActivitiesReportByUserResponse response = new ActivitiesReportByUserResponse();
        for (IActivitiesReportByUser a : result) {
            ActivitiesReportByUser temp = new ActivitiesReportByUser();
            if (a.getActivityDay() != null)
                temp.setActivityDay(a.getActivityDay());
            temp.setEmail(a.getEmail());
            temp.setExecutableName(a.getExecutableName());
            if (a.getTimeUsed() != null)
                temp.setTimeUsed(a.getTimeUsed());
            response.getReport().add(temp);
        }
        return response;
    }


    public TimeReportResponse getTimeReportByUser(TimeReportRequest request) {
        List<ITimeReportByUser> result = activityRepository.getTimeReport(
                request.getProjectID(),
                request.getEmail(),
                (request.getMinDate() != null ? formatter.format(request.getMinDate()) : null),
                (request.getMaxDate() != null ? formatter.format(request.getMaxDate()) : null)
        );
        TimeReportResponse response = new TimeReportResponse();
        for (ITimeReportByUser a : result) {
            TimeReportByUser temp = new TimeReportByUser();
            if (a.getActivityDay() != null)
                temp.setActivityDay(a.getActivityDay());
            temp.setEmail(a.getEmail());
            if (a.getTimeUsed() != null)
                temp.setTimeUsed(a.getTimeUsed());
            response.getReport().add(temp);
        }
        return response;
    }

    public CumulativeReportResponse getCumulativeReportByEmail(String email, Date minDate, Date maxDate) {
        List<CumulativeReport> myReport = cumulativeReportRepository.getCumulativeReport(
                email,
                (minDate != null ? formatter.format(minDate) : null),
                (maxDate != null ? formatter.format(maxDate) : null)
        );
        CumulativeReportResponse response = new CumulativeReportResponse();
        for (CumulativeReport r : myReport) {
            CumulativeActivityReport myApp = new CumulativeActivityReport();
            myApp.setEmail(r.getEmail());
            myApp.setExecutableName(r.getExecutableName());
            myApp.setCapturedDate(r.getCapturedDate().toString());
            myApp.setDailySum(r.getDailySum() != null ? r.getDailySum() : "");
            myApp.setMonthlySum(r.getDailySum() != null ? r.getMonthlySum() : "");
            myApp.setYearlySum(r.getDailySum() != null ? r.getYearlySum() : "");
            myApp.setUsedTime(r.getDailySum() != null ? r.getUsedTime() : "");
            response.getActivityReports().add(myApp);
        }
        return response;
    }
}

