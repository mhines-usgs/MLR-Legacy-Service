package gov.usgs.wma.mlrlegacy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class Controller {
    
    @GetMapping("/")
    public List<MonitoringLocation> getMonitoringLocations() {
        return new ArrayList<>();
    }
    
}
