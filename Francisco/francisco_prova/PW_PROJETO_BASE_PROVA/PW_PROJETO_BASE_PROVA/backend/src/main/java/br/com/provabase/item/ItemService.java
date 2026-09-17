package br.com.provabase.item;

import br.com.provabase.item.dto.ItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;
    private final SimpMessagingTemplate messaging;

    public ItemService(ItemRepository repository, SimpMessagingTemplate messaging) {
        this.repository = repository;
        this.messaging = messaging;
    }

    public List<Item> list(String search) {
        if (search == null || search.isBlank()) return repository.findAll();
        return repository.findByNameContainingIgnoreCaseOrderByName(search);
    }

    public Item find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado"));
    }

    public Item create(ItemRequest request) {
        Item item = new Item(null, request.name(), request.description(), request.price(), request.active() == null || request.active());
        Item saved = repository.save(item);
        notifyChange("CREATED", saved);
        return saved;
    }

    public Item update(Long id, ItemRequest request) {
        Item item = find(id);
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        if (request.active() != null) item.setActive(request.active());
        Item saved = repository.save(item);
        notifyChange("UPDATED", saved);
        return saved;
    }

    public void delete(Long id) {
        Item item = find(id);
        repository.delete(item);
        notifyChange("DELETED", item);
    }

    private void notifyChange(String action, Item item) {
        messaging.convertAndSend("/topic/items", new ItemEvent(action, item));
    }

    public record ItemEvent(String action, Item item) {}
}
