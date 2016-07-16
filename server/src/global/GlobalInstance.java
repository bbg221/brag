package global;
import connection.ConnHelper;
import database.DbHandler;

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
