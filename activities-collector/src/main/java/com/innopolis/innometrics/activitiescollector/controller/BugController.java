package com.innopolis.innometrics.activitiescollector.controller;

import com.innopolis.innometrics.activitiescollector.dto.BugTrackingListRequest;
import com.innopolis.innometrics.activitiescollector.dto.BugTrackingRequest;
import com.innopolis.innometrics.activitiescollector.service.BugTrackingService;
import com.netflix.config.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;

import static com.innopolis.innometrics.activitiescollector.constants.ExceptionMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/V1/Bug", produces = MediaType.APPLICATION_JSON_VALUE)
public class BugController {
    private static final String UTF = "UTF-8";
    private final BugTrackingService bugTrackingService;

    @PutMapping()
    public ResponseEntity<BugTrackingRequest> updateBug(@RequestBody BugTrackingRequest bug,
                                                        @RequestHeader(required = false) String token) {
        if (bug == null || bug.getTitle() == null || bug.getTrace() == null)
            throw new ValidationException(NOT_ENOUGH_DATA.getValue());
        BugTrackingRequest bugTrackingRequest;
        if (bugTrackingService.existsById(bug.getBugId()))
            bugTrackingRequest = bugTrackingService.update(bug);
        else throw new ValidationException(BUG_DOES_NOT_EXIST.getValue() + bug.getBugId());
        if (bugTrackingRequest == null) {
            throw new ValidationException(BUG_NOT_UPDATED.getValue());
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping()
    public ResponseEntity<BugTrackingRequest> createBug(@RequestBody BugTrackingRequest bug,
                                                        @RequestHeader(required = false) String token) {
        if (bug == null || bug.getTitle() == null || bug.getTrace() == null)
            throw new ValidationException(NOT_ENOUGH_DATA.getValue());
        BugTrackingRequest bugTrackingRequest;
        if (bugTrackingService.existsById(bug.getBugId()))
            throw new ValidationException(BUG_ALREADY_EXIST.getValue() + bug.getBugId());
        else bugTrackingRequest = bugTrackingService.create(bug);
        if (bugTrackingRequest == null) {
            throw new ValidationException(BUG_NOT_CREATED.getValue());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<BugTrackingRequest> getBugById(@RequestParam Integer bugId,
                                                         @RequestHeader(required = false) String token) {
        if (bugId != null) {
            BugTrackingRequest bug = bugTrackingService.findByID(bugId);
            return ResponseEntity.ok(bug);
        } else
            throw new ValidationException(NOT_ENOUGH_DATA.getValue());
    }

    @GetMapping("/all")
    public ResponseEntity<BugTrackingListRequest> getBugsByParameters(@RequestParam(required = false) Integer status,
                                                                      @RequestParam(required = false) String creationdateFrom,
                                                                      @RequestParam(required = false) String creationdateTo,
                                                                      @RequestHeader(required = false) String token) throws UnsupportedEncodingException {
        String creationDateFrom2 = URLDecoder.decode(creationdateFrom, UTF);
        String creationDateTo2 = URLDecoder.decode(creationdateTo, UTF);
        Timestamp dFrom = new Timestamp(1);
        Timestamp dTo = new Timestamp(System.currentTimeMillis());
        if (StringUtils.isNotEmpty(creationDateFrom2))
            dFrom = java.sql.Timestamp.valueOf(creationDateFrom2);
        if (StringUtils.isNotEmpty(creationDateTo2))
            dTo = java.sql.Timestamp.valueOf(creationDateTo2);
        BugTrackingListRequest bugs;
        if (status == null || status > 1 || status < 0) {
            bugs = bugTrackingService.findBugsByCreationDate(dFrom, dTo);
        } else {
            boolean stat = status.equals(1);
            bugs = bugTrackingService.findBugsByCreationDateWithStatus(dFrom, dTo, stat);
        }
        return ResponseEntity.ok(bugs);
    }

    @DeleteMapping()
    public ResponseEntity<BugTrackingRequest> deleteBug(@RequestParam Integer bugId,
                                                        @RequestHeader(required = false) String token) {
        if (bugId == null)
            throw new ValidationException(NOT_ENOUGH_DATA.getValue());
        bugTrackingService.delete(bugId);
        return ResponseEntity.ok().build();
    }
}
