package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.exceptions.NotFoundException;
import com.shegami.securityJwt.repositories.AbsenceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final AccountService accountService;

    @Override
    public List<Absence> listAllAbsences() {
        return absenceRepository.findAll();
    }


    @Override
    public List<Absence> listByUser(String username) {

        AppUser user = accountService.getUserByUsername(username);
        List<Absence> absences = absenceRepository.findAllByUser(user);

        return absences;

    }

    @Override
    public Absence addNewAbsence(Absence absence) {
        absenceRepository.save(absence);
        return absence;
    }


    @Override
    public Absence getAbsenceById(String id) {
        Absence absence = absenceRepository.findById(id).orElse(null);

        if (absence == null) {
            throw new NotFoundException("No absence Found with id: " + id);
        }
        return absence;
    }

    @Override
    public void deleteAbsence(String id) {

        Absence absence = absenceRepository.findById(id).orElse(null);

        if (absence == null) {
            throw new NotFoundException("No absence Found with id: " + id);
        }

        absenceRepository.deleteById(id);
    }


    @Override
    public Absence updateAbsence(Absence absence) {

        Absence foundAbsence = absenceRepository.findById(absence.getId()).orElse(null);

        if (foundAbsence == null) {
            throw new NotFoundException("No absence Found with id: " + absence.getId());
        }

        absenceRepository.save(absence);

        return absence;
    }
}
