package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;

import java.util.List;

public interface AbsenceService {

    List<Absence> listAllAbsences();
    
    List<Absence> listByUser(String username);

    Absence getAbsenceById(String id);

    Absence addNewAbsence(Absence absence);

    void deleteAbsence(String id);

    Absence updateAbsence(Absence absence);
}
