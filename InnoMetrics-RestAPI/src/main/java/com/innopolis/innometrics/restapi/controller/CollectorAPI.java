package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.service.CollectorVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/collector-version", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CollectorAPI {
    private final CollectorVersionService collectorVersionService;

    @GetMapping()
    public String getCollectorVersion(@RequestParam(required = true) String osversion) {
        return collectorVersionService.getCurrentVersion(osversion);
    }

    @PutMapping()
    public Boolean updateCollectorVersion(@RequestParam(required = true) String osversion,
                                          @RequestParam(required = true) String newVersion) {
        return collectorVersionService.updateCurrentVersion(osversion, newVersion);
    }
}
