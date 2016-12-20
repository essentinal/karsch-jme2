package karsch;

import java.util.HashMap;

import karsch.level.LevelMap;
import karsch.passes.HudPass;
import karsch.passes.LevelPass;
import karsch.states.CreditsGameState;
import karsch.states.HUDGameState;
import karsch.states.LevelGameState;
import karsch.states.LoadGameState;
import karsch.states.MenuGameState;
import karsch.states.PassManagerGameState;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.system.GameSettings;

public class Values {
	private static Values instance;
	
	private HUDGameState hudGameState;
	private LevelGameState levelGameState;
	private PassManagerGameState pManagerGameState;
	private LoadGameState loadingGS;
	private LevelPass levelPass;
	private HudPass hudPass;
	private GameSettings settings;
	private HashMap<Integer,PassManagerGameState> pmGameStates; 
	private HashMap<Integer,LevelGameState> levels; 
	private boolean pause = false;
	private int babies=0;
	private int keys=0;
	private MenuGameState menu;
	private LevelMap levelMap;
	private float speed = 2;
	private CreditsGameState credits;
	
	private Values() {
		pmGameStates = new HashMap<Integer, PassManagerGameState>(3);
		levels = new HashMap<Integer,LevelGameState>(3);
	}
	
	public static Values getInstance(){
		if (instance == null)
			instance = new Values();
		return instance;
	}
	
	public SceneMonitor getMonitor(){
		return SceneMonitor.getMonitor();
	}

	public HUDGameState getHudState() {
		return hudGameState;
	}

	public void setHudState(HUDGameState hudState) {
		this.hudGameState = hudState;
	}

	public LevelGameState getLevelGameState() {
		return levelGameState;
	}
	
	public LevelGameState getLevelGameState(int number) {
		return levels.get(number);
	}
	
	public PassManagerGameState getPManagerGameState(int number) {
		return pmGameStates.get(number);
	}

	public void setLevelGameState(int number, LevelGameState levelGameState) {
		this.levelGameState = levelGameState;
		if (!levels.containsKey(number)){
			levels.put(number, levelGameState);
		}
	}

	public PassManagerGameState getPManagerGameState() {
		return pManagerGameState;
	}

	public void setPManagerGameState(int number, PassManagerGameState managerGameState) {
		pManagerGameState = managerGameState;
		if (!pmGameStates.containsKey(number)){
			pmGameStates.put(number, managerGameState);
		}
		System.out.println("stored levels " + pmGameStates.size());
	}

	public LevelPass getLevelPass() {
		return levelPass;
	}

	public void setLevelPass(LevelPass levelPass) {
		this.levelPass = levelPass;
	}

	public HudPass getHudPass() {
		return hudPass;
	}

	public void setHudPass(HudPass hudPass) {
		this.hudPass = hudPass;
	}

	public HashMap<Integer, PassManagerGameState> getPmGameStates() {
		return pmGameStates;
	}

	public HashMap<Integer, LevelGameState> getLevels() {
		return levels;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getBabies() {
		return babies;
	}

	public void setBabies(int babies) {
		this.babies = babies;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public MenuGameState getMenu() {
		return menu;
	}

	public void setMenu(MenuGameState menu) {
		this.menu = menu;
	}
	
	public LevelMap getLevelMap() {
		return levelMap;
	}
	
	public void setLevelMap(LevelMap levelMap){
		this.levelMap = levelMap;
	}

	public int getKeys() {
		return keys;
	}

	public void setKeys(int keys) {
		this.keys = keys;
	}

	public LoadGameState getLoadingGS() {
		return loadingGS;
	}

	public void setLoadingGS(LoadGameState loadingGS) {
		this.loadingGS = loadingGS;
	}
	
	public float getSpeed(){
		return speed;
	}

	public CreditsGameState getCredits() {
		return credits;
	}

	public void setCredits(CreditsGameState credits) {
		this.credits = credits;
	}
}

