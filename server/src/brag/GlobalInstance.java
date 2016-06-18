package brag;

import brag.datebaseHandler.DbHandler;
import brag.networkWithClient.ConnHelper;

public class GlobalInstance {
	private GlobalInstance(){}

    private static final GlobalInstance instance = new GlobalInstance();

    public static GlobalInstance getInstance(){
        return instance;
    }
    
    
    private DbHandler dbHandler = new DbHandler();
    private ConnHelper connHelper = new ConnHelper();
    
    public DbHandler getDbHandler(){
    	return dbHandler;
    }
    
    public ConnHelper getConnHelper() {
    	return connHelper;
    }
    
    
}
