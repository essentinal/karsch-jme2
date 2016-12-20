package karsch.items;

import java.util.ArrayList;

import karsch.level.tiles.Lever;
import karsch.level.tiles.RollingStone;

public class SwitchRiddle {
	private ArrayList<Lever> levers;
	private ArrayList<RollingStone> stones; // TODO change data type
		
	
	public SwitchRiddle() {
		levers = new ArrayList<Lever>(3);
		stones = new ArrayList<RollingStone>(3);
	}
	
	
	public void addLever(Lever lever){
		levers.add(lever);
	}
	
	public void addStone(RollingStone stone){
		stones.add(stone);
	}
	
	public boolean checkRiddle(){
		if (levers.size() == 3){
			if(checkIt(levers.get(0).getOpen(), levers.get(1).getOpen(), levers.get(2).getOpen(), 0)){
				stones.get(0).open();
				System.out.println("true");
			} else {
				stones.get(0).close();
				System.out.println("false");
			}
			if(checkIt(levers.get(0).getOpen(), levers.get(1).getOpen(), levers.get(2).getOpen(), 1)){
				stones.get(1).open();
				System.out.println("true");
			} else {
				stones.get(1).close();
				System.out.println("false");
			}
			if(checkIt(levers.get(0).getOpen(), levers.get(1).getOpen(), levers.get(2).getOpen(), 2)){
				stones.get(2).open();
				System.out.println("true");
			} else {
				stones.get(2).close();
				System.out.println("false");
			}
		}
		return true;
	}
	
	private boolean checkIt(boolean x0, boolean x1, boolean x2, int stone){
		System.out.println(x0 + " " + x1 + " " + x2 + " " + stone);
		if(stone == 0){
			return (x0);
		} else if(stone == 2){
			return (x0 && x2);
		} else if(stone == 1){
			return (x0 && x1 && !x2) || (x0 && !x1 && x2);
		} else {
			return false;
		}
	}
}
