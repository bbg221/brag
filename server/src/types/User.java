package types;

public class User {
	public int INVALID_AGE = -1; 
	
	public int userId = 0;	
	public int sex = 0;   // 0->man, 1->lady, 2->visitor
	public int age = INVALID_AGE;
	public int win_count = 0;
	public int fail_count = 0;
	public int equal_count = 0;
	public int brag_count = 0;
	public String name = "";
}
