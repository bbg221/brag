package types;

public class DataState {
	public final int INVALID = 2; // 0: not changed, 1: changed, 2: invalid 
	
	public int name_changed = INVALID;
	public int picture_changed = INVALID;
}
