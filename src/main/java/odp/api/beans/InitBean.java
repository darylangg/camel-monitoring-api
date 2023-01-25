package odp.api.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class InitBean {
    // instantiate as singleton object
    private static InitBean instance = null;
    public synchronized static InitBean getInstance() {
        if (instance == null) {
            instance = new InitBean();
            initMapping.put("producerConfig", false);
            initMapping.put("consumerConfig", false);
        }
        return instance;
    }

    Logger logger = LoggerFactory.getLogger(InitBean.class);

    private static final HashMap<String, Boolean> initMapping = new HashMap<String, Boolean>();

    public boolean isInit(){
        boolean init = true;
        for (Map.Entry<String, Boolean> set : initMapping.entrySet()){
            if (!set.getValue()){
                init = false;
                logger.info(set.getKey() + " not initialized");
            }
        }
        return init;
    }

    public void initializeMapping (String key){
        logger.info(key + " initialized");
        initMapping.put(key, true);
    }
}
