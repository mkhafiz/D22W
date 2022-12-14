package sg.edu.nus.pafworkshop22.workshop22.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.pafworkshop22.workshop22.repository.RSVPRepository;
import sg.edu.nus.pafworkshop22.workshop22.models.RSVP;

@Service
public class RSVPService {

// Business Logic/ transactional rules are stated here

    @Autowired
    private RSVPRepository rsvpRepo;

    public List<RSVP> getAllRSVP(String q) {
        return rsvpRepo.getAllRSVP(q);
    }

    public RSVP searchRSVPByName(String name) {
        return rsvpRepo.searchRSVPByName(name);
    }

    public RSVP insertPurchaseOrder(final RSVP rsvp) {
        return rsvpRepo.insertRSVP(rsvp);
    }

    public boolean updateRSVP(final RSVP rsvp) {
        return rsvpRepo.updateRSVP(rsvp);
    }

    public Integer getTotalRSVP() {
        return rsvpRepo.getTotalRSVP();
    }
}
