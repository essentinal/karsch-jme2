package karsch.characters;

import karsch.Values;
import karsch.controller.KarschController;
import karsch.effects.Pow;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Collectable;
import karsch.level.LevelMap;
import karsch.level.LevelText;
import karsch.level.tiles.SpikeTrap;
import karsch.resources.ModelCache;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class Karsch extends CharacterBase implements Collectable{
	private KarschController karschController;
	private boolean dead = false;
	private int babiesFound = 0;
	private SpeechBalloon speech;
	
	public Karsch(int x, int y, LevelMap levelMap, LevelText levelText){
		super(x, y, levelMap, "karsch");
		model = ModelCache.getInstance().get("karsch1.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setSpeed(30f * Values.getInstance().getSpeed());
        trans.setMaxTime(19);

        model.setLocalScale(.008f);
        model.setModelBound(new BoundingBox());
        model.setLocalTranslation(0,1.5f,0);
        model.updateModelBound();
        
        attachChild(model);
        attachChild(new Shadow(1.5f,1.5f));
        
		karschController = new KarschController(this, trans, x, y);
		addController(karschController);
		
		speech = new SpeechBalloon();
		attachChild(speech);
		
		karschController.setLastDirection(levelText.getRotationEntrance());
	}
	
	public boolean moveDown() {
		karschController.moveDown();
		return true;
	}

	public boolean moveLeft() {
		karschController.moveLeft();
		return true;
	}

	public boolean moveRight() {
		karschController.moveRight();
		return true;
	}

	public boolean moveUp() {
		karschController.moveUp();
		return true;
	}
	
	public KarschController getController(){
		return karschController;
	}
	
	@Override
	public void setPause(boolean pause){
		if (controller != null)
			controller.setActive(!pause);
		if (trans != null && pause)
			trans.setActive(false);
		if (track != null){
			if (pause){
				track.stop();
			} else {
				track.play();
			}
		}
		if (audioController != null)
			audioController.setActive(!pause);
	}

//	@Override
	public void collect(Node source) {
		if (!dead){
			dead = true;
			Pow pow = null;
			if (source instanceof Cow2){
				pow = new Pow(this, Pow.TYPE_COWPOW);
			} else if (source instanceof SpikeTrap){
				pow = new Pow(this, Pow.TYPE_SPIKEOUCH);
			}
			
			
			
			Values values = Values.getInstance();
			values.setBabies(values.getBabies()-babiesFound);
			values.getHudState().setBabies(values.getBabies());
			pow.setActive();
		}
	}

	public int getBabiesFound() {
		return babiesFound;
	}

	
	/**
	 * Increments the found babies
	 */
	public void incBabiesFound() {
		babiesFound++;
	}
	
	public void setBabiesFound(int babiesFound) {
		this.babiesFound = babiesFound;
	}
	
	public void speak(String text){
		speech.showText(text);
	}
	
	public void checkInteractions(){
		karschController.checkInteractions();
	}
}
