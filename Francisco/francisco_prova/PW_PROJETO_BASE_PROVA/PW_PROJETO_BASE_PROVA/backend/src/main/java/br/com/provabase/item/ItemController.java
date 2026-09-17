package br.com.provabase.item;

import br.com.provabase.item.dto.ItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Item> list(@RequestParam(required = false) String search) {
        return service.list(search);
    }

    @GetMapping("/{id}")
    public Item find(@PathVariable Long id) {
        return service.find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@Valid @RequestBody ItemRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
