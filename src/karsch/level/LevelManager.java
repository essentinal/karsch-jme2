package karsch.level;

import java.util.concurrent.Callable;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.input.KarschInputHandler;
import karsch.level.tiles.LevelEntrance;
import karsch.passes.HudPass;
import karsch.passes.LevelPass;
import karsch.states.CreditsGameState;
import karsch.states.HUDGameState;
import karsch.states.LevelGameState;
import karsch.states.LoadGameState;
import karsch.states.MenuGameState;
import karsch.states.PassManagerGameState;

import com.jme.math.Vector3f;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;


public class LevelManager {
	private static LevelManager instance;
	
	protected LoadGameState loading;
	
	private Values values = Values.getInstance();
	
	// actual level number, the game starts with 1
	private int levelNumber=1;
	
	private LevelManager() {
		
	}
	
	public static LevelManager getInstance(){
		if (instance == null)
			instance = new LevelManager();
		return instance;
	}
	
	
	public void loadMenu(){
		if (values.getMenu() == null) {
			MenuGameState menu = new MenuGameState();
			GameStateManager.getInstance().attachChild(menu);
			menu.setActive(true);
			values.setMenu(menu);
		} else {
			if (Values.getInstance().getPManagerGameState() != null) {
				Values.getInstance().getPManagerGameState().setActive(false);
			}
			GameStateManager.getInstance().activateChildNamed("menu");
		}
	}
	
	public void loadCredits(){
		HUDGameState hud = values.getHudState();
		if (hud != null){
			hud.setActive(false);
			PassManagerGameState actGameState = values.getPManagerGameState();
			if (actGameState != null){
				actGameState.setActive(false);
				GameStateManager.getInstance().detachChild(actGameState);
			}
			values.setLevelGameState(levelNumber, null);
			values.getPmGameStates().remove(levelNumber);
			values.getLevels().remove(levelNumber);
		}
			
		loading = new LoadGameState(ResourceLocatorTool.locateResource(
				ResourceLocatorTool.TYPE_TEXTURE, "LOADINGSCREEN.PNG"));
        loading.setActive(true);
        loading.setProgress(0,"Please wait, credits are loading..."); // center the progress bar.
        GameStateManager.getInstance().attachChild(loading);  
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).setExecuteAll(true);
        Values.getInstance().setLoadingGS(loading);
        
        loading.setProgress(.1f);
		
		new Thread(){
			@Override
			public void run() {
				this.setPriority(Thread.NORM_PRIORITY + 1);       
				
                GameTaskQueueManager.getManager().update(new Callable<Object>() {
                    public Object call() throws Exception {
						
						CreditsGameState credits = new CreditsGameState();
						GameStateManager.getInstance().attachChild(credits);
						credits.setActive(true);
						values.setCredits(credits);		

						loading.setProgress(1f, "Loading done");
						return null;
				    }
				});
			}
		}.start();
	}
	
	public void loadFirstLevel(){
		// game state for passmanager
		PassManagerGameState pManagerGS = new PassManagerGameState(levelNumber);
		
		values.setPManagerGameState(levelNumber, pManagerGS);
		
        // passes for level and hud
        LevelPass levelPass = new LevelPass();
        values.setLevelPass(levelPass);
        
        HudPass hudPass = new HudPass();
        values.setHudPass(hudPass);
        
        // game state for the hud
        HUDGameState hudGameState = new HUDGameState();
        values.setHudState(hudGameState);      
        hudPass.setState(hudGameState);
        
        // game state for the level
        LevelGameState levelGameState = new LevelGameState(levelNumber);
        
        levelPass.setState(levelGameState);
        
        // add the passes to the passmanager state
        pManagerGS.getManager().add(levelPass);
        
        
//        BloomRenderPass bloomRenderPass = new BloomRenderPass(levelGameState.getCam(), 4);
//
//		if (!bloomRenderPass.isSupported()) {
//			System.out.println(	"GLSL Not supported on this computer.");
//			
//		} else {
//			bloomRenderPass.add(levelGameState.getRootNode());
//			bloomRenderPass.setUseCurrentScene(true);
//			bloomRenderPass.setBlurIntensityMultiplier(1f);
//			pManagerGS.getManager().add(bloomRenderPass);
//		}
		
        pManagerGS.getManager().add(hudPass);
        
        GameStateManager.getInstance().attachChild(pManagerGS);
        
        pManagerGS.setActive(true);
        
        
        printInfo();
	}
	
	public void loadLevel(int offset, LevelEntrance entrance){
		if ((levelNumber + offset) < 1)
			return;
		
		LevelGameState actLevel = values.getLevelGameState();
		Karsch karsch = actLevel.getKarsch();
		karsch.getController().dontMove();
		actLevel.setPause(true);
		actLevel.setActive(false);
		
		if (entrance != null){
			karsch.setX(entrance.getX());
			karsch.setY(entrance.getY());
			karsch.setLocalTranslation(new Vector3f(karsch.getX()*5f+2.5f, 0 , karsch.getY()*5f+2.5f));
		}
		
		levelNumber+=offset;
		
		if (values.getPManagerGameState(levelNumber) == null){
		
			loading = new LoadGameState(ResourceLocatorTool.locateResource(
					ResourceLocatorTool.TYPE_TEXTURE, "LOADINGSCREEN.PNG"));
	        loading.setActive(true);
	        loading.setProgress(0,"Please wait, loading..."); // center the progress bar.
	        GameStateManager.getInstance().attachChild(loading);  
	        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).setExecuteAll(true);
	        Values.getInstance().setLoadingGS(loading);
	        
	        loading.setProgress(.1f);
		}
		
		new Thread(){
			@Override
			public void run() {
				this.setPriority(Thread.NORM_PRIORITY + 1);       
				
                GameTaskQueueManager.getManager().update(new Callable<Object>() {
                    public Object call() throws Exception {
						values.getPManagerGameState().setActive(false);
						
						boolean levelExists = (values.getPManagerGameState(levelNumber) != null);
						System.out.println("LEVEL EXISTS");
						
						// game state for passmanager
						PassManagerGameState pManagerGS;
						if (levelExists){
							pManagerGS = values.getPManagerGameState(levelNumber);
						} else {
							pManagerGS = new PassManagerGameState(levelNumber);
						}
						
						if (!levelExists)
				        	GameStateManager.getInstance().attachChild(pManagerGS);
						
						pManagerGS.setActive(true);
				
						values.setPManagerGameState(levelNumber,pManagerGS);
						
						//		GameStateManager.getInstance().detachAllChildren();
							
						
				        // passes for level and hud
				        LevelPass levelPass = new LevelPass();
				        values.setLevelPass(levelPass);
				        HudPass hudPass = new HudPass();
				        values.setHudPass(hudPass);
				        
				        // game state for the hud  
				        hudPass.setState(values.getHudState());
				        
				        // game state for the level
				        LevelGameState nextLevel;
				        if (levelExists){
				        	nextLevel = values.getLevelGameState(levelNumber);
				        } else {
				        	nextLevel = new LevelGameState(levelNumber);
				        }
				        values.setLevelGameState(levelNumber, nextLevel);
				        levelPass.setState(nextLevel);
				        if (levelExists)
				        	nextLevel.setActive(true);
				        
				        // add the passes to the passmanager state
				        if (!levelExists){
				        	pManagerGS.getManager().add(levelPass);
				        	pManagerGS.getManager().add(hudPass);
				        }
				        pManagerGS.setActive(true);
				        
				        Karsch karsch = nextLevel.getKarsch();
						
//						if (levelOffset < 0)
//							karsch.setLocalTranslation(nextLevel.getLevelExit());
//						else
//							karsch.setLocalTranslation(nextLevel.getLevelEntrance());
						
						KarschInputHandler.getInstance().setKarsch(karsch);
						printInfo();
						if (!levelExists)
							loading.setProgress(1f, "Loading done");
						return null;
				    }
				});
			}
		}.start();
	}
	
	public void restartLevel(){
		PassManagerGameState actGameState = values.getPManagerGameState();
		actGameState.setActive(false);
		GameStateManager.getInstance().detachChild(actGameState);
		values.getPmGameStates().remove(levelNumber);
		values.getLevels().remove(levelNumber);
		loadLevel(0, null);
	}
	
	public void unloadCredits(){
		PassManagerGameState actGameState = values.getPManagerGameState();
		if (actGameState != null){
			actGameState.setActive(false);
			GameStateManager.getInstance().detachChild(actGameState);
		}
		values.getPmGameStates().remove(levelNumber);
		values.getLevels().remove(levelNumber);

		levelNumber = 0;
		loadMenu();
		
		CreditsGameState credits = values.getCredits();
		if (credits != null){
			GameStateManager.getInstance().detachChild(credits);
			values.setCredits(null);
		}
	}
	
	public int getLevelNumber() {
		return levelNumber;
	}
	
	private void printInfo(){
		if (values.getHudPass() != null){
			System.out.print("HUD Pass ");
			System.out.println("enabled: " + values.getHudPass().isEnabled());
		}
		if (values.getHudState() != null){
			System.out.print("HUD State ");
			System.out.println("active: " + values.getHudState().isActive());
		}
		if (values.getLevelPass() != null){
			System.out.print("Level Pass ");
			System.out.println("enabled: " + values.getLevelPass().isEnabled());
		}
		if (values.getLevelGameState() != null){
			System.out.print("Level State " + values.getLevelGameState().getName());
			System.out.println(" active: " + values.getLevelGameState().isActive());
		}
		
		GameStateManager gsManager = GameStateManager.getInstance();
		System.out.println("\n"+gsManager.getQuantity() + " levels in GameStateManager");
		for (GameState gs : values.getPmGameStates().values()){
			System.out.println(gs.getName() + " active: " + gs.isActive());
		}
		
		for (GameState gs : values.getLevels().values()){
			System.out.println(gs.getName() + " active: " + gs.isActive());
		}
	}
}
