package karsch.characters;

import karsch.interfaces.Interactable;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class PigHouse extends CharacterBase implements Interactable{
//	private boolean interacting = false;
//	private SpeechBalloon balloon;
//	
//	private Dialog dialog;
	
	public PigHouse(int x, int y, LevelMap levelMap){
		super(x, y, levelMap, "babypig");
		model = ModelCache.getInstance().get("babypig.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setRepeatType(Controller.RT_WRAP);
		trans.setSpeed(0.5f + FastMath.rand.nextFloat());

        model.setLocalScale(.004f);
        model.setModelBound(new BoundingBox());
        model.setLocalTranslation(0,.5f,0);
        model.updateModelBound();
        
        attachChild(model);
        attachChild(new Shadow(1.5f,1.5f));
        
//        balloon = new SpeechBalloon();
//		attachChild(balloon);
//		
//		dialog = new Dialog(balloon);
//		
//		dialog.addText("Mooooo  ");
//		dialog.addText("That's a cow. Cows can't speak.");
//		
//		dialog.addSpeech("cow1.ogg");
//		dialog.addSpeech("thatsacow.ogg");
	}
	
//	@Override
	public void interact(Karsch karsch) {
//		if (!interacting){
//			lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
//			interacting = true;
//			controller.setActive(false);
//			trans.setActive(false);
//			karsch.getController().setActive(false);
//		}
//		
//		if (dialog.start()){
//			
//		} else {
//			stopInteraction(karsch);
//		}
	}

//	@Override
	public void stopInteraction(Karsch karsch) {
//		interacting = false;
//		controller.setActive(true);
//		trans.setActive(true);
//		karsch.getController().setActive(true);
	}
}
