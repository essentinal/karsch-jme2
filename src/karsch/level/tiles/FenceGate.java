package karsch.level.tiles;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController;
import karsch.interfaces.Interactable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelText;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class FenceGate extends Tile implements KarschPassable, Interactable{
	private SpatialTransformer trans;
	private int direction = NPCController.DIRECTION_DOWN;
	private boolean open = false;
	
	public FenceGate(int x, int y, LevelText levelText){
		super("fenceGate" + x + "" + y, x, y);
		
		model = ModelCache.getInstance().get("fencegate.3ds");
		model.setLocalScale(.025f);
		attachChild(model);
		
		trans = (SpatialTransformer) model.getController(0);		
		trans.setRepeatType(Controller.RT_CLAMP);
		trans.setActive(true);
		trans.update(.1f);
		trans.setActive(false);
		trans.setCurTime(0);
		
		setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);
		
		direction = levelText.getRotationField(x, y);
		
		if(direction == NPCController.DIRECTION_LEFT){
			getLocalRotation().fromAngleAxis(-FastMath.HALF_PI,new Vector3f(0,1,0));
		} else if (direction == NPCController.DIRECTION_RIGHT){
			getLocalRotation().fromAngleAxis(FastMath.HALF_PI,new Vector3f(0,1,0));
		} else if (direction == NPCController.DIRECTION_UP){
			getLocalRotation().fromAngleAxis(FastMath.PI,new Vector3f(0,1,0));
		}
	}

//	@Override
	public void interact(Karsch karsch) {
		if (open)
			return;
		
		Values values = Values.getInstance();
		int keys = values.getKeys();
		if (keys > 0){
			karsch.getController().setActive(false);
			SoundManager.getInstance().playSoundOnce("creak1.ogg");

			values.setKeys(keys-1);
			values.getHudState().setKeys(keys-1);
			trans.setActive(true);
			values.getLevelGameState().getKarsch().getController().setActField(new Freefield());
			values.getHudState().displayTextTime("The gate is now open", 1000);
			open = true;
			
			new Thread(){
				@Override
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					stopInteraction(Values.getInstance().getLevelGameState().getKarsch());
				}
			}.start();
			
		} else {
			values.getHudState().displayTextTime("You have no key", 2000);
		}
	}

//	@Override
	public void stopInteraction(Karsch karsch) {
		karsch.getController().setActive(true);
	}

//	@Override
	public boolean canPass() {
		return open;
	}
	
}
