package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.entity.CollectorVersion;
import com.innopolis.innometrics.restapi.repository.CollectorVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectorVersionService {
    private final CollectorVersionRepository versionRepository;

    public String getCurrentVersion(String osVersion) {
        CollectorVersion myVersion = versionRepository.findByOsVersion(osVersion);
        if (myVersion != null)
            return myVersion.getValue();
        return "";
    }

    public boolean updateCurrentVersion(String osVersion, String newVersion) {
        CollectorVersion myVersion = versionRepository.findByOsVersion(osVersion);
        myVersion.setValue(newVersion);
        versionRepository.save(myVersion);
        return true;
    }
}
