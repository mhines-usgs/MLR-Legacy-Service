package gov.usgs.wma.mlrlegacy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class MonitoringLocationDao {
    
    private final SqlSession sqlSession;
    
    public MonitoringLocationDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    public List<MonitoringLocation> getByMap(Map<String, String> queryParams) {
        return sqlSession.selectList("getByMap", queryParams);
    }
    
    public MonitoringLocation getById(String id) {
        Map <String,String> map = new HashMap<>();
        map.put("id", id);
        
        return sqlSession.selectOne("getById", map);
    }
    
    public String create(MonitoringLocation ml) {
        sqlSession.insert("create", ml);
        return ml.getId();
    }
    
    public void update(MonitoringLocation ml) {
        sqlSession.update("update", ml);
    }
}
