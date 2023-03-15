package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.ProfileRequest;
import com.innopolis.innometrics.restapi.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/User/Profile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileAPI {
    private final ProfileService profileService;

    @PostMapping()
    public ResponseEntity<ProfileRequest> updateProfileOfUser(@RequestBody ProfileRequest profileRequest,
                                                              @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                profileService.updateProfileOfUser(profileRequest, token),
                HttpStatus.OK
        );
    }

    @DeleteMapping()
    public boolean deleteProfile(@RequestParam Integer id,
                                 @RequestHeader(required = false) String token) {
        return profileService.deleteProfile(id, token);
    }

    @GetMapping()
    public ResponseEntity<ProfileRequest> findByMacaddress(@RequestParam String macaddress,
                                                           @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(profileService.findByMacaddress(macaddress, token),
                HttpStatus.OK);
    }
}
