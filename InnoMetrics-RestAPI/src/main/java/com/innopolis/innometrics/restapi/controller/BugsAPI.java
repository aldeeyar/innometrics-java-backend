package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.BugReportRequest;
import com.innopolis.innometrics.restapi.dto.BugTrackingListRequest;
import com.innopolis.innometrics.restapi.dto.BugTrackingRequest;
import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.service.BugTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import static com.innopolis.innometrics.restapi.constants.ErrorMessages.NOT_ENOUGH_DATA;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/V1/Bug", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BugsAPI {
    private final BugTrackingService bugTrackingService;

    @PutMapping("/")
    public ResponseEntity<BugTrackingRequest> updateBug(@RequestBody BugTrackingRequest bug,
                                                        @RequestHeader(required = false) String token) {
        if (bug == null || bug.getTitle() == null || bug.getTrace() == null)
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        if (!bugTrackingService.updateBug(bug, token)) {
            throw new ValidationException(ErrorMessages.BUG_NOT_UPDATED.getMessage());
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/")
    public ResponseEntity<BugTrackingRequest> createBug(@RequestBody BugReportRequest bug,
                                                        @RequestHeader(required = false) String token) {
        if (bug == null || bug.getTitle() == null || bug.getTrace() == null)
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        if (!bugTrackingService.createBug(bug, token)) {
            throw new ValidationException(ErrorMessages.BUG_NOT_CREATED.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<BugTrackingRequest> getBugById(@RequestParam Integer bugId,
                                                         @RequestHeader(required = false) String token) {
        if (bugId != null) {
            BugTrackingRequest bug = bugTrackingService.findBugById(bugId, token);
            return ResponseEntity.ok(bug);
        } else
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }

    @GetMapping("/all")
    public ResponseEntity<BugTrackingListRequest> getBugsByParameters(@RequestParam(required = false) Integer status,
                                                                      @RequestParam(required = false) String creationdateFrom,
                                                                      @RequestParam(required = false) String creationdateTo,
                                                                      @RequestHeader(required = false) String token) {
        BugTrackingListRequest bugs = bugTrackingService.findBugsByParameters(status, creationdateFrom, creationdateTo, token);
        return ResponseEntity.ok(bugs);
    }

    @DeleteMapping("/")
    public ResponseEntity<BugTrackingRequest> deleteBug(@RequestParam @NotNull Integer bugId,
                                                        @RequestHeader(required = false) String token) {
        bugTrackingService.deleteBug(bugId, token);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handeException(MethodArgumentNotValidException e) {
        throw new ValidationException(NOT_ENOUGH_DATA.getMessage());
    }
}
