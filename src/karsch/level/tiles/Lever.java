package karsch.level.tiles;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.NPCController;
import karsch.interfaces.Interactable;
import karsch.level.LevelText;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class Lever extends Tile implements Interactable{
	private SpatialTransformer trans;
	private int direction = NPCController.DIRECTION_DOWN;
	private boolean open = false, interacting = false;
	
	public Lever(int x, int y, LevelText levelText) {
		super("lever"+x+""+y, x, y);
		
		Values.getInstance().getLevelGameState().getRiddle().addLever(this);
		
		model = ModelCache.getInstance().get("lever.3ds");
		model.setLocalScale(.020f);
		Node leverNode = new Node("levernode");
		leverNode.attachChild(model);
		leverNode.getLocalRotation().fromAngleAxis(-FastMath.HALF_PI,new Vector3f(0,1,0));
		attachChild(leverNode);
		
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
		if (interacting)
			return;
		interacting = true;

		karsch.getController().setActive(false);
		SoundManager.getInstance().playSoundOnce("creak1.ogg");

		trans.setActive(true);
		
		if (open){
			trans.setSpeed(-15);
		} else {
			trans.setSpeed(15);
		}
		
		open = !open;
		
		new Thread(){
			@Override
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				stopInteraction(Values.getInstance().getLevelGameState().getKarsch());
			}
		}.start();
		
		Values.getInstance().getLevelGameState().getRiddle().checkRiddle();
	}

//	@Override
	public void stopInteraction(Karsch karsch) {
		interacting = false;
		karsch.getController().setActive(true);
		
		Values.getInstance().getHudState().displayTextTime("Something happened... ", 1000);
	}
	
	public boolean getOpen(){
		return open;
	}

}
