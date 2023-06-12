package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.TeammembersListRequest;
import com.innopolis.innometrics.authserver.dto.TeammembersRequest;
import com.innopolis.innometrics.authserver.entitiy.Teammembers;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.repository.TeammembersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.*;
import static com.innopolis.innometrics.authserver.service.PropertyNames.getNullPropertyNames;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@RequiredArgsConstructor
public class TeammembersService {
    private final TeammembersRepository teammembersRepository;

    public boolean existsById(Integer id){
        return teammembersRepository.existsById(id);
    }

    public boolean existInTheTeam(Integer id,String email){
        return teammembersRepository.existsByTeamIdAndEmail(id, email);
    }

    public void delete(Integer id) {
        Teammembers entity = teammembersRepository.findById(id)
                .orElseThrow(() -> new javax.validation.ValidationException(NO_TEAM_BY_ID_FOUND.getValue() + id));
        teammembersRepository.delete(entity);
    }

    public TeammembersRequest create(TeammembersRequest detail) {
        Teammembers entity = new Teammembers();
        BeanUtils.copyProperties(detail, entity);
        entity = teammembersRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public TeammembersRequest update(TeammembersRequest detail) {
        Teammembers entity = teammembersRepository.findByMemberId(detail.getMemberId());
        assertNotNull(entity, NO_TEAM_BY_ID_FOUND.getValue() + detail.getMemberId());
        if (Boolean.TRUE.equals(teammembersRepository.existsByTeamIdAndEmail(detail.getTeamId(), detail.getEmail()))
                && !teammembersRepository.findByEmailAndTeamId(detail.getEmail(), detail.getTeamId()).getMemberId().equals(detail.getMemberId())) {
            throw new ValidationException("there is already email " + detail.getEmail() + " in the team with id " + detail.getTeamId());
        }
        detail.setMemberId(null);
        BeanUtils.copyProperties(detail, entity, getNullPropertyNames(detail));
        entity = teammembersRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public TeammembersListRequest findAllActiveTeammembers() {
        List<Teammembers> activeTeams = teammembersRepository.findAllActive();
        assertNotNull(activeTeams, NO_TEAM_MEMBER_FOUND.getValue());
        return convertFromList(activeTeams);

    }

    public TeammembersListRequest findAllTeammembers(){
        List<Teammembers> allTeams = teammembersRepository.findAll();
        assertNotNull(allTeams, NO_TEAM_MEMBER_FOUND.getValue());
        return convertFromList(allTeams);
    }

    public TeammembersListRequest findByTeammemberProperties(Integer memberId, Integer teamId, String email) {
        TeammembersListRequest teammembersListRequest = new TeammembersListRequest();
        if (memberId != null) {
            Teammembers member = teammembersRepository.findByMemberId(memberId);
            assertNotNull(member, NO_TEAM_BY_ID_FOUND.getValue() + memberId);
            if ((teamId == null || member.getTeamId().equals(teamId))
                    && (StringUtils.isEmpty(email) || member.getEmail().equals(email))) {
                TeammembersRequest memberReq = new TeammembersRequest();
                BeanUtils.copyProperties(member, memberReq);
                teammembersListRequest.addTeammembersRequest(memberReq);
            }
        } else {
            if (email != null && !email.equals("") && teamId != null) {
                Teammembers member = teammembersRepository.findByEmailAndTeamId(email, teamId);
                assertNotNull(member, NO_TEAM_BY_EMAIL_FOUND.getValue() + email + " and team id " + teamId);
                TeammembersRequest memberReq = new TeammembersRequest();
                BeanUtils.copyProperties(member, memberReq);
                teammembersListRequest.addTeammembersRequest(memberReq);
            } else if ((StringUtils.isEmpty(email)) && teamId != null) {
                List<Teammembers> teammembers = teammembersRepository.findByTeamId(teamId);
                assertNotNull(teammembers, "No team members found by this team id " + teamId);
                teammembersListRequest = convertFromList(teammembers);
            } else if (StringUtils.isNotEmpty(email)) {
                List<Teammembers> teammembers = teammembersRepository.findByEmail(email);
                assertNotNull(teammembers, NO_TEAM_BY_EMAIL_FOUND.getValue() + email);
                teammembersListRequest = convertFromList(teammembers);
            }
        }
        return teammembersListRequest;
    }

    private TeammembersListRequest convertFromList(List<Teammembers> teammembers){
        TeammembersListRequest teammembersListRequest = new TeammembersListRequest();
        for (Teammembers activeTeammember : teammembers) {
            TeammembersRequest detail = new TeammembersRequest();
            BeanUtils.copyProperties(activeTeammember,detail);
            teammembersListRequest.addTeammembersRequest(detail);
        }
        return teammembersListRequest;
    }
}
