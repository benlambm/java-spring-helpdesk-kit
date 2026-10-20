package edu.vwcc.itp220mod4;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * A tiny read-only web API, so a deployed app has something to show.
 * Provided; every endpoint just calls your TicketRepository.
 */
@RestController
public class TicketController {

    private final TicketRepository repository;

    public TicketController(TicketRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> home = new LinkedHashMap<>();
        home.put("app", "ITP 220 help desk");
        home.put("tickets", repository.count());
        home.put("try", List.of("/api/tickets/open", "/api/tickets/HD-101",
                "/api/technicians", "/api/stats/categories"));
        return home;
    }

    @GetMapping("/api/tickets/open")
    public List<Ticket> openTickets() {
        return repository.findOpen();
    }

    @GetMapping("/api/tickets/{id}")
    public ResponseEntity<Ticket> ticket(@PathVariable String id) {
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/api/technicians")
    public Set<String> technicians() {
        return repository.findTechnicianNames();
    }

    @GetMapping("/api/stats/categories")
    public Map<String, Integer> categoryCounts() {
        return repository.countByCategory();
    }
}
