package gov.usgs.wma.mlrlegacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MonitoringLocationDao {
    
    public List<MonitoringLocation> getByMap(Map<String, String> queryParams) {
        return new ArrayList<>();
    }
    
    public MonitoringLocation getById(String id) {
        return new MonitoringLocation();
    }
    
    public MonitoringLocation create(MonitoringLocation ml) {
        return new MonitoringLocation();
    }
    
    public MonitoringLocation update(MonitoringLocation ml) {
        return ml;
    }
}
