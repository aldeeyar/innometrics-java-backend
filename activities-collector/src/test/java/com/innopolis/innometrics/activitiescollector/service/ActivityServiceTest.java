package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.ActivityReport;
import com.innopolis.innometrics.activitiescollector.entity.Activity;
import com.innopolis.innometrics.activitiescollector.repository.ActivityRepository;
import com.innopolis.innometrics.activitiescollector.repository.CumulativeReportRepository;
import com.innopolis.innometrics.activitiescollector.repository.MeasurementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        ActivityService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ActivityServiceTest {
    private final static String TEST_DATA = "Test";
    private final static String BROWSER = "https://innopolis.com/en/services/innopolis-university";
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    CumulativeReportRepository cumulativeReportRepository;
    @Autowired
    ActivityService service;

    @Test
    void createActivityTest() {
        ActivityReport activityReport = createActivityReport();
        service.createActivity(activityReport, TEST_DATA, new Timestamp(2));
        assertTrue(true);
    }

    @Test
    void deleteActivity() {
        activityRepository.save(createActivity());
        boolean result = service.deleteActivity(1, TEST_DATA);
        assertTrue(result);
    }

    @Test
    void deleteActivitiesWithIds() {
        activityRepository.save(createActivity());
        Integer[] ids = new Integer[1];
        boolean result = service.deleteActivitiesWithIds(ids);
        assertTrue(result);
    }

    private ActivityReport createActivityReport() {
        ActivityReport activityReport = new ActivityReport();
        activityReport.setActivityID(1);
        activityReport.setIdleActivity(true);
        activityReport.setActivityType(TEST_DATA);
        activityReport.setPid(TEST_DATA);
        activityReport.setBrowserUrl(BROWSER);
        activityReport.setBrowserTitle(BROWSER);
        activityReport.setStartTime(new Timestamp(1));
        activityReport.setEndTime(new Timestamp(2));
        activityReport.setExecutableName(TEST_DATA);
        return activityReport;
    }

    private Activity createActivity() {
        Activity activity = new Activity();
        activity.setActivityId(1);
        activity.setActivityType(TEST_DATA);
        activity.setIdleActivity(true);
        activity.setStartTime(new Timestamp(1));
        activity.setEndTime(new Timestamp(2));
        activity.setExecutableName(TEST_DATA);
        return activity;
    }
}
