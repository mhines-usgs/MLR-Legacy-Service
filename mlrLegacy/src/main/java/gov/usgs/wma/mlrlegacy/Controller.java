package gov.usgs.wma.mlrlegacy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/monitoringLocations")
public class Controller {
    
    
    @GetMapping()
    public List<MonitoringLocation> getMonitoringLocations() {
        return new ArrayList<>();
    }
    
    @GetMapping("/{id}")
    public MonitoringLocation getMonitoringLocation(@PathVariable("id") String id) {
        return new MonitoringLocation();
    }
    
    @PostMapping()
    public MonitoringLocation createMonitoringLocation(@RequestBody MonitoringLocation ml) {
        return ml;
    }
    
    @PutMapping("/{id}")
    public MonitoringLocation updateMonitoringLocation(@PathVariable("id") String id, @RequestBody MonitoringLocation ml) {
        ml.setId(id);
        return ml;
    }
    
}
