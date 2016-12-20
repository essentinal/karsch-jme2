package karsch.menu;

import java.util.concurrent.Callable;

import karsch.Values;
import karsch.level.LevelManager;
import karsch.sound.SoundManager;
import karsch.states.LoadGameState;
import karsch.states.MenuGameState;

import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.game.state.GameStateManager;

public class MainMenu extends SubMenu{
	private LoadGameState loading;
	public MainMenu(MenuGameState menuGameState) {
		super(menuGameState, "karsch2.jpg");
		
		addEntry("Start Game");
		
		addEntry("Options");
		addEntry("Credits");
		addEntry("Exit");
		
		init();
	}

	@Override
	public void select() {
		if (counter == 0){
			if (Values.getInstance().getLevelGameState() != null)
				resumeGame();
			else
				newGame();
		} else if (counter == 1){
			menuGameState.activateMenu("options");
		} else if (counter == 2){
			LevelManager.getInstance().loadCredits();
			//deactivate this gamestate
			menuGameState.setActive(false);
		} else if (counter == 3){
			System.exit(0);
		}
	}
	
	@Override
	public void escape() {
		if (Values.getInstance().getLevelGameState() != null)
			resumeGame();
		else
			System.exit(0);
	}
	
	@Override
	public void setActive(boolean active) {
		if (Values.getInstance().getLevelGameState() != null)
			labels.get(0).setText("Resume Game");
		else
			labels.get(0).setText("Start Game");
		super.setActive(active);
	}
	
	private void newGame() {
		loading = new LoadGameState(
				ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,
						"LOADINGSCREEN.PNG"));
		loading.setActive(true);
		loading.setProgress(0, "Please wait, loading...");

		GameStateManager.getInstance().attachChild(loading);
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).setExecuteAll(true);
		Values.getInstance().setLoadingGS(loading);

		loading.setProgress(.1f);
		
		//deactivate this gamestate
		menuGameState.setActive(false);
		
		new Thread() {
			public void run() {

				this.setPriority(Thread.NORM_PRIORITY + 1);
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {
							public Object call() throws Exception {
								
								// init music
								SoundManager soundManager = SoundManager.getInstance();
								soundManager.playMusic();
								
								// init level
								LevelManager.getInstance().loadFirstLevel();

								// update loading bar
								loading.setProgress(1f, "Loading done");

								return null;
							}
						});
			}
		}.start();
	}
	
	private void resumeGame(){
		menuGameState.setActive(false);
		Values.getInstance().getPManagerGameState().setActive(true);
	}

}
