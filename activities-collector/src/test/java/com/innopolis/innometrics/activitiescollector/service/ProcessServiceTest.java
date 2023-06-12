package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.MeasurementReport;
import com.innopolis.innometrics.activitiescollector.dto.ProcessReport;
import com.innopolis.innometrics.activitiescollector.entity.Process;
import com.innopolis.innometrics.activitiescollector.repository.ProcessMeasurementRepository;
import com.innopolis.innometrics.activitiescollector.repository.ProcessRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        ProcessService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ProcessServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    ProcessService service;
    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ProcessMeasurementRepository processMeasurementRepository;


    @Test
    void deleteProcess() {
        processRepository.save(createProcess());
        boolean result = service.deleteProcess(1, TEST_DATA);
        assertTrue(result);
    }

    @Test
    void deleteProcessesWithIds() {
        processRepository.save(createProcess());
        Integer[] ids = new Integer[1];
        boolean result = service.deleteProcessesWithIds(ids);
        assertTrue(result);
    }


    private ProcessReport createReport() {
        List<MeasurementReport> reportList = new ArrayList<>();
        MeasurementReport measurementReport = new MeasurementReport();
        measurementReport.setValue(TEST_DATA);
        measurementReport.setAlternativeLabel(TEST_DATA);
        measurementReport.setMeasurementTypeId("1");
        reportList.add(measurementReport);
        ProcessReport report = new ProcessReport();
        report.setPid(TEST_DATA);
        report.setOsVersion(TEST_DATA);
        report.setUserID(TEST_DATA);
        report.setMeasurementReportList(reportList);
        return report;
    }

    private Process createProcess() {
        Process process = new Process();
        process.setEmail(TEST_DATA);
        process.setProcessId(1);
        return process;
    }
}
