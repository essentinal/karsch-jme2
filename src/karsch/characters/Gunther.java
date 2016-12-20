package karsch.characters;

import karsch.Values;
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
public class Gunther extends CharacterBase implements Interactable{
	private SpeechBalloon balloon;
	private boolean interacting = false;
	private Dialog keyDialog,noKeyDialog;
	
	public Gunther(int x, int y, LevelMap levelMap) {
		super(x, y, levelMap, "gunther");	
		model = ModelCache.getInstance().get("gunther.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setSpeed(5f);
        trans.setMaxTime(25);
        trans.setActive(true);
		model.setLocalScale(.04f);
		model.updateModelBound();
		
		attachChild(model);
		
		attachChild(new Shadow(1.5f,1.5f));
		
		balloon = new SpeechBalloon();
		attachChild(balloon);
		
		setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
		setModelBound(new BoundingBox());
		updateModelBound();
		
		noKeyDialog = new Dialog(balloon);
		
		noKeyDialog.addText("Hi Karsch!");
		noKeyDialog.addText("Hi Gunther, what's going on here?");
		noKeyDialog.addText("I locked your baby into the sheep's enclosure.");
		noKeyDialog.addText("Ok, give me the key!");
		noKeyDialog.addText("I lost the key!");
		noKeyDialog.addText("What the ...");
		noKeyDialog.addText("Sorry but I don't know it better.");
		noKeyDialog.addText("Ohh you ...");
		noKeyDialog.addText("You gotta find the key.. don't know where I lost it");
		
		noKeyDialog.addSpeech("01hikarsch.ogg");
		noKeyDialog.addSpeech("02higunther.ogg");
		noKeyDialog.addSpeech("03ilockedyourbaby.ogg");
		noKeyDialog.addSpeech("04givemethekey.ogg");
		noKeyDialog.addSpeech("05ilostthekey.ogg");
		noKeyDialog.addSpeech("06whatthe.ogg");
		noKeyDialog.addSpeech("07sorryidontknow.ogg");
		noKeyDialog.addSpeech("08ohyou.ogg");
		noKeyDialog.addSpeech("09yougottafindthekey.ogg");
		
		keyDialog = new Dialog(balloon);
		
		keyDialog.addText("Have a nice day...");
		keyDialog.addText("Never mind!");
		
		keyDialog.addSpeech("01haveaniceday.ogg");
		keyDialog.addSpeech("02nevermind.ogg");
		
		lookAt(getLocalTranslation().clone().add(new Vector3f(1,0,0)), Vector3f.UNIT_Y);
		
		setCullHint(CullHint.Never);
	}

//	@Override
	public void interact(Karsch karsch) {
		if (!interacting){
			lookAt(karsch.getLocalTranslation(), Vector3f.UNIT_Y);
			interacting = true;
			karsch.getController().setActive(false);
		}
		
		if (Values.getInstance().getLevelGameState().getKarsch().getBabiesFound() < 1){
			if (!noKeyDialog.start()){
				stopInteraction(karsch);
			}
		} else {
			if (!keyDialog.start()){
				stopInteraction(karsch);
			}
		}
	}

//	@Override
	public void stopInteraction(Karsch karsch) {
		interacting = false;
		karsch.getController().setActive(true);
	}
}
