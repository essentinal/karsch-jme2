package karsch.characters;

import karsch.Values;
import karsch.controller.NPCController;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.Dialog;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class Sheep extends CharacterBase implements Interactable{
	private boolean interacting = false;
	private SpeechBalloon balloon;
	
	private Dialog dialog;
	
	public Sheep(int x, int y, LevelMap levelMap) {
		super(x, y, levelMap, "sheep");
		model = ModelCache.getInstance().get("sheep1.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setActive(false);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setSpeed(30f * Values.getInstance().getSpeed());
        trans.setMaxTime(19);
		model.setLocalScale(.06f);
		
		model.setLocalTranslation(new Vector3f(0,-2.5f,-2.5f));
		model.setModelBound(new BoundingBox());
		model.updateModelBound();
		
		setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
		attachChild(model);
		
		attachChild(new Shadow(1.5f,1.5f));
		
		controller = new NPCController(this, trans, x, y);
		controller.setSpeed(2 / Values.getInstance().getSpeed());
		addController(controller);
		addSound("sheep", 1, 3, 5);
		
		balloon = new SpeechBalloon();
		attachChild(balloon);
		
		dialog = new Dialog(balloon);
		
		dialog.addText("Baa  ");
		dialog.addText("That's a sheep. Looks very fluffy.");
		
		dialog.addSpeech("sheep1.ogg");
		dialog.addSpeech("thatsasheep.ogg");
	}

//	@Override
	public void interact(Karsch karsch) {
		if (!interacting){
			lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
			interacting = true;
			controller.setActive(false);
			trans.setActive(false);
			karsch.getController().setActive(false);
		}
		
		if (dialog.start()){
			
		} else {
			stopInteraction(karsch);
		}
	}


//	@Override
	public void stopInteraction(Karsch karsch) {
		interacting = false;
		controller.setActive(true);
		trans.setActive(true);
		karsch.getController().setActive(true);
	}
}
