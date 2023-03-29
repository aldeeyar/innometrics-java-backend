package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.ProfileRequest;
import com.innopolis.innometrics.authserver.entitiy.Profile;
import com.innopolis.innometrics.authserver.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.NO_PROFILE_FOUND;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Boolean existsByEmail(String email, String macAddress){
        return profileRepository.existsByUserEmailAndMacAddress(email,macAddress);
    }

    public ProfileRequest create(ProfileRequest detail){
        Profile entity = new Profile();
        detail.setProfileId(null);
        BeanUtils.copyProperties(detail, entity, PropertyNames.getNullPropertyNames(detail));
        entity = profileRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity,detail);
        return detail;
    }

    public ProfileRequest findByMacAddress(String macAddress){
        Profile entity = profileRepository.findByMacAddress(macAddress);
        assertNotNull(entity, "No profile found by macAdress " + macAddress);
        ProfileRequest detail = new ProfileRequest();
        BeanUtils.copyProperties(entity,detail);
        return detail;
    }

    public ProfileRequest update(ProfileRequest detail) {
        Profile entity = profileRepository.findByUserEmailAndMacAddress(
                detail.getUserEmail(),
                detail.getMacAddress());
        assertNotNull(entity, "No profile found by user email and macaddress " + detail.getProfileId());
        detail.setProfileId(null);
        BeanUtils.copyProperties(detail, entity, PropertyNames.getNullPropertyNames(detail));
        entity = profileRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public void delete(Integer id) {
        Profile entity = profileRepository.findById(id)
                .orElseThrow(() -> new ValidationException(NO_PROFILE_FOUND.getValue() + id));
        profileRepository.delete(entity);
    }
}
