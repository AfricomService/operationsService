package com.gpm.operations.service;

import com.gpm.operations.domain.Mission;
import com.gpm.operations.domain.NoteFrais;
import com.gpm.operations.repository.MissionRepository;
import com.gpm.operations.repository.NoteFraisRepository;
import com.gpm.operations.service.dto.NoteFraisCreationDTO;
import com.gpm.operations.service.dto.NoteFraisDTO;
import com.gpm.operations.service.mapper.NoteFraisMapper;
import com.gpm.operations.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NoteFrais}.
 */
@Service
@Transactional
public class NoteFraisService {

    private final Logger log = LoggerFactory.getLogger(NoteFraisService.class);

    private final NoteFraisRepository noteFraisRepository;

    private final NoteFraisMapper noteFraisMapper;

    private final MissionRepository missionRepository;

    public NoteFraisService(
        NoteFraisRepository noteFraisRepository,
        NoteFraisMapper noteFraisMapper,
        MissionRepository missionRepository
    ) {
        this.noteFraisRepository = noteFraisRepository;
        this.noteFraisMapper = noteFraisMapper;
        this.missionRepository = missionRepository;
    }

    /**
     * Crée une note de frais pour les missions sélectionnées : somme des montantTotale, montant en lettres,
     * puis rattachement des missions (noteFraisId). Tout est fait dans la même transaction.
     */
    public NoteFraisDTO createFromMissions(NoteFraisCreationDTO request) {
        String entity = "operationsServiceNoteFrais";
        List<Long> ids = request.getMissionIds();

        if (ids == null || ids.isEmpty()) {
            throw new BadRequestAlertException("Aucune mission sélectionnée", entity, "nomission");
        }

        List<Mission> missions = missionRepository.findAllById(ids);

        if (missions.size() != ids.stream().distinct().count()) {
            throw new BadRequestAlertException("Mission introuvable", entity, "missionnotfound");
        }
        if (missions.stream().anyMatch(m -> m.getNoteFraisId() != null)) {
            throw new BadRequestAlertException("Une mission est déjà rattachée à une note de frais", entity, "missiondejanotee");
        }

        Set<Long> contacts = missions.stream().map(Mission::getContactSocieteId).collect(Collectors.toSet());
        if (contacts.size() != 1) {
            throw new BadRequestAlertException("Les missions doivent concerner le même bénéficiaire", entity, "beneficiairedifferent");
        }
        Long contactSocieteId = contacts.iterator().next();
        if (request.getContactSocieteId() != null && !request.getContactSocieteId().equals(contactSocieteId)) {
            throw new BadRequestAlertException("Bénéficiaire incohérent avec les missions", entity, "beneficiairedifferent");
        }

        BigDecimal total = missions
            .stream()
            .map(Mission::getMontantTotale)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        NoteFrais noteFrais = new NoteFrais();
        noteFrais.setDateDebut(request.getDateDebut());
        noteFrais.setDateFin(request.getDateFin());
        noteFrais.setContactSocieteId(contactSocieteId);
        noteFrais.setMontantTotale(total);
        noteFrais.setMontantTotaleEnLettre(montantEnLettres(total));
        noteFrais.setIdentifiantUnique(request.getIdentifiantUnique());
        noteFrais = noteFraisRepository.save(noteFrais);

        for (Mission mission : missions) {
            mission.setNoteFraisId(noteFrais.getId());
        }
        missionRepository.saveAll(missions);

        return noteFraisMapper.toDto(noteFrais);
    }

    /**
     * Save a noteFrais.
     *
     * @param noteFraisDTO the entity to save.
     * @return the persisted entity.
     */
    public NoteFraisDTO save(NoteFraisDTO noteFraisDTO) {
        log.debug("Request to save NoteFrais : {}", noteFraisDTO);
        NoteFrais noteFrais = noteFraisMapper.toEntity(noteFraisDTO);
        noteFrais = noteFraisRepository.save(noteFrais);
        return noteFraisMapper.toDto(noteFrais);
    }

    /**
     * Partially update a noteFrais.
     *
     * @param noteFraisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NoteFraisDTO> partialUpdate(NoteFraisDTO noteFraisDTO) {
        log.debug("Request to partially update NoteFrais : {}", noteFraisDTO);

        return noteFraisRepository
            .findById(noteFraisDTO.getId())
            .map(existingNoteFrais -> {
                noteFraisMapper.partialUpdate(existingNoteFrais, noteFraisDTO);

                return existingNoteFrais;
            })
            .map(noteFraisRepository::save)
            .map(noteFraisMapper::toDto);
    }

    /**
     * Get all the noteFrais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NoteFraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NoteFrais");
        return noteFraisRepository.findAll(pageable).map(noteFraisMapper::toDto);
    }

    /**
     * Get one noteFrais by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NoteFraisDTO> findOne(Long id) {
        log.debug("Request to get NoteFrais : {}", id);
        return noteFraisRepository.findById(id).map(noteFraisMapper::toDto);
    }

    /**
     * Delete the noteFrais by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NoteFrais : {}", id);
        noteFraisRepository.deleteById(id);
    }

    // ================================
    // Montant en lettres (français), devise : dinar tunisien (1 dinar = 1000 millimes)
    // ================================
    private static final String[] UNITES = {
        "zéro", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
        "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize",
    };
    private static final String[] DIZAINES = { "", "", "vingt", "trente", "quarante", "cinquante", "soixante" };

    public String montantEnLettres(BigDecimal montant) {
        BigDecimal m = (montant == null ? BigDecimal.ZERO : montant).abs().setScale(3, RoundingMode.HALF_UP);
        long dinars = m.toBigInteger().longValue();
        long millimes = m.unscaledValue().longValue() % 1000;

        String res = nombreEnLettres(dinars) + " dinar" + (dinars > 1 ? "s" : "");
        if (millimes > 0) {
            res += " et " + nombreEnLettres(millimes) + " millime" + (millimes > 1 ? "s" : "");
        }
        return Character.toUpperCase(res.charAt(0)) + res.substring(1);
    }

    private String nombreEnLettres(long n) {
        if (n == 0) {
            return "zéro";
        }
        long milliards = n / 1_000_000_000L;
        long millions = (n / 1_000_000L) % 1000;
        long milliers = (n / 1000L) % 1000;
        long reste = n % 1000;

        StringBuilder sb = new StringBuilder();
        if (milliards > 0) {
            sb.append(moinsDeMille((int) milliards, false)).append(" milliard").append(milliards > 1 ? "s" : "").append(' ');
        }
        if (millions > 0) {
            sb.append(moinsDeMille((int) millions, false)).append(" million").append(millions > 1 ? "s" : "").append(' ');
        }
        if (milliers > 0) {
            sb.append(milliers == 1 ? "mille" : moinsDeMille((int) milliers, false) + " mille").append(' ');
        }
        if (reste > 0) {
            sb.append(moinsDeMille((int) reste, true));
        }
        return sb.toString().trim();
    }

    /** plural = true si rien ne suit (cents / quatre-vingts prennent un « s »). */
    private String moinsDeMille(int n, boolean plural) {
        int c = n / 100;
        int r = n % 100;
        if (c == 0) {
            return moinsDeCent(r, plural);
        }
        String prefix = c == 1 ? "cent" : UNITES[c] + " cent";
        if (r == 0) {
            return c > 1 && plural ? prefix + "s" : prefix;
        }
        return prefix + " " + moinsDeCent(r, plural);
    }

    private String moinsDeCent(int n, boolean plural) {
        if (n < 17) {
            return UNITES[n];
        }
        if (n < 20) {
            return "dix-" + UNITES[n - 10];
        }
        if (n < 70) {
            String base = DIZAINES[n / 10];
            int u = n % 10;
            return u == 0 ? base : u == 1 ? base + " et un" : base + "-" + UNITES[u];
        }
        if (n < 80) {
            return n == 71 ? "soixante et onze" : "soixante-" + moinsDeCent(n - 60, plural);
        }
        if (n == 80) {
            return plural ? "quatre-vingts" : "quatre-vingt";
        }
        return "quatre-vingt-" + moinsDeCent(n - 80, plural);
    }
}
