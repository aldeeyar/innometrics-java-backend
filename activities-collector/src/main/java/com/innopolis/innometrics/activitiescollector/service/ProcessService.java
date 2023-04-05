package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.*;
import com.innopolis.innometrics.activitiescollector.entity.Process;
import com.innopolis.innometrics.activitiescollector.entity.ProcessMeasurement;
import com.innopolis.innometrics.activitiescollector.repository.ProcessMeasurementRepository;
import com.innopolis.innometrics.activitiescollector.repository.ProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProcessService {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private final ProcessRepository processRepository;
    private final ProcessMeasurementRepository processMeasurementRepository;

    public void createProcessReport(ProcessReport processReport, String userName, Date creationDate) {
        Process myProcess = new Process();
        myProcess.setEmail(processReport.getUserID());
        myProcess.setExecutableName(processReport.getProcessName());
        myProcess.setIpAddress(processReport.getIpAddress());
        myProcess.setMacAddress(processReport.getMacAddress());
        myProcess.setPid(processReport.getPid());
        myProcess.setCollectedTime(processReport.getCollectedTime());
        myProcess.setOsVersion(processReport.getOsVersion());
        myProcess.setCreationdate(creationDate);
        myProcess.setCreatedBy(userName);
        myProcess = processRepository.save(myProcess);
        for (MeasurementReport m : processReport.getMeasurementReportList()) {
            ProcessMeasurement myMeasurement = new ProcessMeasurement();
            myMeasurement.setProcess(myProcess);
            myMeasurement.setMeasurementTypeId(Integer.parseInt(m.getMeasurementTypeId()));
            myMeasurement.setValue(m.getValue());
            myMeasurement.setCapturedDate(m.getCapturedDate());
            myMeasurement.setCreationdate(creationDate);
            myMeasurement.setCreatedBy(userName);
            myProcess.getProcessMeasurements().add(myMeasurement);
        }
        processMeasurementRepository.saveAll(myProcess.getProcessMeasurements());
    }

    public boolean deleteProcess(Integer processID, String userName) {
        processRepository.existsById(processID);
        processRepository.deleteById(processID);
        return true;
    }

    public boolean deleteProcessesWithIds(Integer[] ids) {
        processRepository.deleteProcessesWithIds(Arrays.asList(ids));
        return true;
    }

    public ProcessDayReportResponse getProcessesByEmailAndDay(String userName, Date reportDay) {
        List<IProcessReportByUserAndDay> myProcesses = processRepository
                .getProcessesPerDay(userName, formatter.format(reportDay));
        ProcessDayReportResponse response = new ProcessDayReportResponse();
        for (IProcessReportByUserAndDay processReport : myProcesses) {
            Process myProcess = new Process();
            myProcess.setProcessId(processReport.getProcessID());
            myProcess.setEmail(processReport.getEmail());
            myProcess.setExecutableName(processReport.getExecutableName());
            myProcess.setIpAddress(processReport.getIpAddress());
            myProcess.setMacAddress(processReport.getMacAddress());
            myProcess.setPid(processReport.getPid());
            myProcess.setCollectedTime(processReport.getCollectedTime());
            myProcess.setOsVersion(processReport.getOsVersion());
            myProcess.setCreationdate(processReport.getCreationdate());
            myProcess.setCreatedBy(processReport.getCreatedBy());
            response.getProcessReports().add(myProcess);
        }
        return response;
    }

    public CurrentActivityReport getCurrentActivityReport(String userName, Date reportDay) {
        List<String> myProcesses = processRepository.getCurrentProcessList(userName, formatter.format(reportDay));
        List<String> myAddress = processRepository.getCurrentMACList(userName);
        return new CurrentActivityReport(myProcesses, myAddress);
    }
}
