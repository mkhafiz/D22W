package sg.edu.nus.pafworkshop22.workshop22.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.pafworkshop22.workshop22.models.RSVP;
import sg.edu.nus.pafworkshop22.workshop22.models.RSVPTotalCntMapper;

import static sg.edu.nus.pafworkshop22.workshop22.repository.Queries.*;

@Repository
public class RSVPRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate; // from Spring

// TASK 2.1 & 2.2

    public List<RSVP> getAllRSVP(String q) {
        // prevent inheritance
        final List<RSVP> rsvps = new LinkedList<>();
        SqlRowSet rs = null;
        // perform the query
        System.out.println("Q>" + q);
        if (q == null) {
            rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_RSVP);
        } else { 
            // else = parse in the name in the URL as part of param
            rs = jdbcTemplate.queryForRowSet(SQL_SEARCH_RSVP_BY_NAME, q);
        }

        while (rs.next()) {
            rsvps.add(RSVP.create(rs));
        }
        return rsvps;
    }

    public RSVP searchRSVPByName(String name) {
        // prevent inheritance
        final List<RSVP> rsvps = new LinkedList<>();
        // perform the query
        final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SEARCH_RSVP_BY_NAME, name);

        while (rs.next()) {
            rsvps.add(RSVP.create(rs));
        }
        return rsvps.get(0);
    }

    public RSVP insertRSVP(final RSVP rsvp) {
        KeyHolder keyholder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_RSVP,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, rsvp.getName());
            ps.setString(2, rsvp.getEmail());
            ps.setString(3, rsvp.getPhone());
            System.out.println("Confirmation date > " + rsvp.getConfirmationDate());
            // JODA time converted to timestamp using long
            ps.setTimestamp(4, new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()));
            ps.setString(5, rsvp.getComments());
            return ps;
        }, keyholder);

        BigInteger primaryKeyVal = (BigInteger) keyholder.getKey();
        rsvp.setId(primaryKeyVal.intValue());
        return rsvp;
    }

    public boolean updateRSVP(final RSVP rsvp) {
        return jdbcTemplate.update(SQL_UPDATE_RSVP_BY_EMAIL,
                rsvp.getName(),
                rsvp.getEmail(),
                rsvp.getPhone(),
                new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()),
                rsvp.getComments(),
                rsvp.getEmail()) > 0;
    }

    public Integer getTotalRSVP() {
        // perform the query
        List<RSVP> rsvps = jdbcTemplate.query(SQL_TOTAL_CNT_RSVP, new RSVPTotalCntMapper(), new Object[] {});

        return rsvps.get(0).getTotalCnt();
    }
}
