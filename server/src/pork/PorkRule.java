package pork;

import java.util.Arrays;

public class PorkRule {
	private int[] porkCenter = new int[52];
	private int porkNum = 0;
	
	private int[] lastPorks = new int[52];
	private int lastPorkNum = 0;
	private int lastFake = 0;
	
	private int[] variablePork = {4,5,6,7};
	
	private boolean isVariablePork(int pork) {
		for (int var : variablePork) {
			if (var == pork) {
				return true;
			}
		}
		return false;
	}
	
	private void addToPorkCenter(int[] porks) {
		for (int i = 0; i < porks.length; i++) {
			porkCenter[porkNum] = porks[i];
			porkNum++;
		}
	}
	
	private void setLastPorks(int[] porks) {
		lastPorks = Arrays.copyOf(porks, porks.length);
		lastPorkNum = porks.length;
	}
	
	private void setLastPorks(int fake, int[] porks) {
		lastFake = fake;		
		setLastPorks(porks);
	}
	
	public boolean firstHand(int fake, int[] porks) {
		// initialize parameter. it's important to set zero to pork number, and it's only necessary to set these two parameter.
		porkNum = 0;
		lastPorkNum = 0;
		
		addToPorkCenter(porks);
		setLastPorks(fake, porks);		
		
		return isFaked();
	}
	
	public boolean addPork(int[] porks) {
		addToPorkCenter(porks);
		setLastPorks(porks);
		
		return isFaked();
	}
	
	public boolean believe() {
		return false;
	}
	
	//return false means the last player was truly faked. 
	public boolean unbelieve() {
		return isFaked();
	}
	
	private boolean isFaked() {
		for (int i = 0; i < lastPorkNum; i++) {
			if (!isVariablePork(lastPorks[i]) && (lastPorks[i]/4 + 1) != lastFake) {
				return false;
			}
		}
		
		return true;
	}
	
	public int[] getPorks() {
		int[] porks = Arrays.copyOf(porkCenter, porkNum);
		
		return porks;
	}
}
