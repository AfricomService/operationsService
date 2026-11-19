package com.gpm.operations.client;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@AuthorizedFeignClient(name = "PROJECTSERVICE")
public interface UserContactRestClient {
    @GetMapping("/api/find-societe-id-by-affaire-id/{affaireId}")
    Long getSocieteId(@PathVariable("affaireId") Long affaireId);

    @GetMapping("/api/matricules-to-grant-permission")
    List<String> getMatriculesToGrantPermission(@RequestParam("roleCode") String roleCode, @RequestParam("societeId") Long societeId);

    @GetMapping("/api/matricule/{contactSocieteId}")
    String getMatriculeByContactSocieteId(@PathVariable("contactSocieteId") Long contactSocieteId);

    @GetMapping("/api//clients-code/{id}")
    String getClientCode(@PathVariable("id") Long id);

    @PostMapping("/api/matricules")
    List<String> getMatriculesByContactSocieteIds(@RequestBody List<Long> contactSocieteIds);
}
