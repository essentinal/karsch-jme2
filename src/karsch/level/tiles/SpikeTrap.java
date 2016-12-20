package karsch.level.tiles;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class SpikeTrap extends CharacterBase implements KarschPassable, Collectable{
	private SpikeTrap spikeTrap;
	private SpikeTrapController spikeTrapController;
	public SpikeTrap(int x, int y){
		super("spiketrap" + x + "" + y, x, y);
		spikeTrap = this;
		
		model = ModelCache.getInstance().get("spiketrap.3ds");
		model.setLocalScale(.025f);
		
		trans = (SpatialTransformer) model.getController(0);
		trans.setActive(true);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setMaxTime(35f);
        trans.setSpeed(10f + FastMath.rand.nextInt(10));
		
		attachChild(model);
		setModelBound(new BoundingBox());
		updateModelBound();
		setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);
		spikeTrapController = new SpikeTrapController();
		
		addController(spikeTrapController);
	}
//	@Override
	public void collect(Node source) {
		if (trans.getCurTime() > 15){
			Values.getInstance().getLevelGameState().getKarsch().collect(this);
		}	
	}
	
	@Override
	public void setPause(boolean pause) {
		super.setPause(pause);
		if (spikeTrapController != null)
			spikeTrapController.setActive(!pause);
	}
	
	class SpikeTrapController extends Controller{
		public SpikeTrapController() {
			setRepeatType(RT_WRAP);
			setActive(true);
		}
		@Override
		public void update(float tpf) {

			if (isVisible()){
				if ( trans.getCurTime() > 15f && trans.getCurTime() < 15.3f ){
					SoundManager.getInstance().playSoundOnce("spike_out.ogg");
				} else if ( trans.getCurTime() > 30f && trans.getCurTime() < 30.3f){
					SoundManager.getInstance().playSoundOnce("spike_in.ogg");
				}
			}
			
			Object karschActfield = Values.getInstance().getLevelGameState().getKarsch().getController().getActField();
			if (trans.getCurTime() > 15 && 
				karschActfield instanceof SpikeTrap && karschActfield == spikeTrap){
				
				Values.getInstance().getLevelGameState().getKarsch().collect(spikeTrap);
			}
		}
	}

//	@Override
	public boolean canPass() {
		return true;
	}
}