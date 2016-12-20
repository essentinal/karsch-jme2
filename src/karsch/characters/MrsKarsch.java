package karsch.characters;

import karsch.Values;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.LevelManager;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.Dialog;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class MrsKarsch extends CharacterBase implements Interactable{
	private SpeechBalloon balloon;
	private boolean interacting = false;
	private Dialog dialog;
	
	public MrsKarsch(int x, int y, LevelMap levelMap) {
		super(x, y, levelMap, "mrskarsch");	
		model = ModelCache.getInstance().get("mrskarsch.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setSpeed(5f);
        trans.setMaxTime(25);
        trans.setActive(true);
		model.setLocalScale(.008f);
		model.setLocalTranslation(0, 1.8f, 0);
		
		attachChild(model);
		
		attachChild(new Shadow(1.5f,1.5f));
		
		balloon = new SpeechBalloon();
		attachChild(balloon);
		
		setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
		setModelBound(new BoundingBox());
		updateModelBound();
		
		dialog = new Dialog(balloon);
		
		if (levelMap.getLevelNumber() == 1){
			dialog.addText("Hi darling, have you seen the babies?");
			dialog.addText("No...  ");
			dialog.addText("They must be playing on the outside!");
			dialog.addText("And now?");
			dialog.addText("Look for the babies and take them home, Karsch.");
			
			dialog.addSpeech("01hidarlinghaveyouseen.ogg");
			dialog.addSpeech("02no.ogg");
			dialog.addSpeech("03theymustbeplaying.ogg");
			dialog.addSpeech("04andnow.ogg");
			dialog.addSpeech("05lookforthebabies.ogg");
		} else {
			if (Values.getInstance().getBabies() >= 5){
				dialog.addText("Oh my darling, you found all babies!");
				dialog.addText("Of course I did");
				dialog.addText("Thanks sweety, I love you");
				
				dialog.addSpeech("01ohmydarling.ogg");
				dialog.addSpeech("02ofcourseidid.ogg");
				dialog.addSpeech("03thankssweety.ogg");
			} else {
				dialog.addText("You come home without all the babies?");
				dialog.addText("Oh my god...");
				dialog.addText("They will find their way home...");
				
				dialog.addSpeech("01youcomehomewithout.ogg");
				dialog.addSpeech("02ohmygod.ogg");
				dialog.addSpeech("03theywillfindtheirway.ogg");
			}
		}
		
		lookAt(getLocalTranslation().clone().add(new Vector3f(-1,0,0)), Vector3f.UNIT_Y);
	}

//	@Override
	public void interact(Karsch karsch) {
		if (!interacting){
			lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
			interacting = true;
			karsch.getController().setActive(false);
		}
		
		if (!dialog.start()){
			if (levelMap.getLevelNumber() == 1){
				stopInteraction(karsch);
			} else {
				LevelManager.getInstance().loadCredits();
			}
		}

	}

//	@Override
	public void stopInteraction(Karsch karsch) {
		interacting = false;
		karsch.getController().setActive(true);
	}
}
