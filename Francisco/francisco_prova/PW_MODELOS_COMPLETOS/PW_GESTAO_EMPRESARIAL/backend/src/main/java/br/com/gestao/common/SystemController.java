package br.com.gestao.common;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;
@RestController @RequestMapping("/api/system")
public class SystemController {
  @GetMapping("/time") public Map<String,Object> time() {
    return Map.of("dateTime", LocalDateTime.now().toString(), "date", LocalDate.now().toString(), "time", LocalTime.now().withNano(0).toString());
  }
  @GetMapping("/ping") public Map<String,String> ping() { return Map.of("status","ok"); }
}
