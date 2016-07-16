package pork;

import java.util.Arrays;
import java.util.Random;

public class PorkShuffler {
	private int gloabPorkNum = 52;
	private boolean equalVarpork = true;
	private int[] porks;
	
	public PorkShuffler() {
		porks = new int[this.gloabPorkNum];
		for (int i = 0; i < this.gloabPorkNum; i++) {
			porks[i] = this.gloabPorkNum;
		}
	}
	
	public PorkShuffler(int gloabPorkNum, boolean equalVarPork) {
		
		this.gloabPorkNum = gloabPorkNum;
		this.equalVarpork = equalVarPork;
		
		if (gloabPorkNum < 20) {
			this.gloabPorkNum = 20;
		}
		
		if (gloabPorkNum > 52) {
			this.gloabPorkNum = 52;
		}
		
		if (gloabPorkNum % 2 != 0) {
			this.gloabPorkNum = gloabPorkNum + 1;
		}
		
		porks = new int[this.gloabPorkNum];
		for (int i = 0; i < this.gloabPorkNum; i++) {
			porks[i] = this.gloabPorkNum;
		}
	}

	public void shuffle()
	{
		Random r = new Random();
		int newPork = 0;
		int currentPorkCount = 0;
		int maxSetNum = gloabPorkNum;
		int maxPork = gloabPorkNum;
		boolean hasExist = false;
		
		if (equalVarpork) {
			porks[0] = 4;
			porks[1] = 5;
			porks[gloabPorkNum - 1] = 6;
			porks[gloabPorkNum - 2] = 7;
			
			currentPorkCount = 2;
			maxSetNum = gloabPorkNum - 2;
		}
				
		while (currentPorkCount < maxSetNum) {
			newPork = r.nextInt(maxPork);
			
			hasExist = false;
			for (int i = 0; i < gloabPorkNum; i++) {
				if (newPork == porks[i]) {
					hasExist = true;
					break;
				}
			}
			
			if (!hasExist) {
				porks[currentPorkCount] = newPork;
				currentPorkCount++;
			}
		}
	}

	public int[] getFistPartPorks() {
		int halfCount = gloabPorkNum / 2;
				
		int[] firstPartPorks = Arrays.copyOfRange(porks, 0, halfCount); 
		Arrays.sort(firstPartPorks);
		
		return firstPartPorks;
	}
	
	public int[] getSecondPartPorks() {
		int start = gloabPorkNum / 2;
		
		int[] secondPartPorks = Arrays.copyOfRange(porks, start, gloabPorkNum);
		Arrays.sort(secondPartPorks);
		
		return secondPartPorks;
	}
}
