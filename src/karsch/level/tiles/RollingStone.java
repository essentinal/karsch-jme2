package karsch.level.tiles;

import java.awt.Point;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.characters.Shadow;
import karsch.effects.SpeechBalloon;
import karsch.interfaces.Interactable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.level.LevelText;
import karsch.sound.Dialog;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

@SuppressWarnings("serial")
public class RollingStone extends Tile implements Interactable{
	private LevelMap levelMap;
	private Point rollingStoneGoalField;
	private int xGoal, yGoal;
	private Vector3f goalFieldPos;
	private Vector3f startFieldPos;
	
	private boolean interacting = false;
	private SpeechBalloon balloon;
	private Dialog dialog;
	
	public RollingStone (int x, int y, LevelText levelText, LevelMap levelMap) {
		super("rollingStone"+x+""+y, x, y);
		attachChild(new Sphere("rollingStone" + String.valueOf(x)+String.valueOf(y),
				new Vector3f(0,0,0),
				8, 8, 2.5f));
		this.levelMap = levelMap;
		Values.getInstance().getLevelGameState().getRiddle().addStone(this);
		rollingStoneGoalField = levelText.getSecondRollingStoneField(x, y);
		xGoal = (int) rollingStoneGoalField.getX();
		yGoal = (int) rollingStoneGoalField.getY();
		startFieldPos = new Vector3f(x*5+2.5f, 2.5f, y*5+2.5f);
		goalFieldPos = new Vector3f((float)xGoal*5f+2.5f, 2.5f, (float)yGoal*5f+2.5f);

		TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

		ts.setTexture(TextureManager.loadTexture(
				ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "DIRT.PNG"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear),0);


        ts.setEnabled(true);
		setRenderState(ts);
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(new ColorRGBA(.4f,.4f,.45f,1f));
		ms.setEnabled(true);
		
		setRenderState(ms);
		
		updateRenderState();
		setModelBound(new BoundingBox());
		updateModelBound();
//		setLocalRotation(new Matrix3f(	0,FastMath.rand.nextFloat(),0,
//										0,0,FastMath.rand.nextFloat(),
//										0,0,FastMath.rand.nextFloat()));
		
		Shadow shadow = new Shadow(4,4);
		shadow.setLocalTranslation(0, -2.5f, 0);
		attachChild(shadow);
		setLocalTranslation(startFieldPos);	
		
		balloon = new SpeechBalloon();
		dialog = new Dialog(balloon);
		
		dialog.addText("");
		dialog.addText("This stone blocks my way.");
		
		dialog.addSpeech("silence.ogg");
		dialog.addSpeech("thisstoneblocks.ogg");
	}
	
	public void open(){
		setLocalTranslation(goalFieldPos);
		levelMap.getLevelMap()[xGoal][yGoal] = this;
		levelMap.getLevelMap()[x][y] = new Freefield();
	} 

	public void close(){
		setLocalTranslation(startFieldPos);
		levelMap.getLevelMap()[x][y] = this;
		levelMap.getLevelMap()[xGoal][yGoal] = new Freefield();
	}
	
//	@Override
	public void interact(Karsch karsch) {
		if (!interacting){
			interacting = true;
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
		karsch.getController().setActive(true);
	}
}
