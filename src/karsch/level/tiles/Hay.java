package karsch.level.tiles;

import karsch.controller.NPCController;
import karsch.resources.ModelCache;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Hay extends Tile{
	public Hay(int x, int y) {
		super("hay" + x + " " + y, x, y);
		
		loadModel(FastMath.rand.nextInt(3)+1);
	}
	
	private void loadModel(int number){
		model = ModelCache.getInstance().get("hay"+ number +".3ds");
		model.setLocalScale(.020f);

		float randX = FastMath.rand.nextFloat();
		float randY = FastMath.rand.nextFloat();
		setLocalTranslation(new Vector3f(x*5+1.5f +randX, 0 ,y*5+1.5f + randY));
		attachChild(model);
//		setLocalScale(scale + (0.5f*FastMath.rand.nextFloat()) );
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setAmbient(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setEnabled(true);
        
		setRenderState(ms);
		
		updateRenderState();
		
		int direction = FastMath.rand.nextInt(4);
		
		if(direction == NPCController.DIRECTION_LEFT){
			getLocalRotation().fromAngleAxis(-FastMath.HALF_PI,new Vector3f(0,1,0));
		} else if (direction == NPCController.DIRECTION_RIGHT){
			getLocalRotation().fromAngleAxis(FastMath.HALF_PI,new Vector3f(0,1,0));
		} else if (direction == NPCController.DIRECTION_UP){
			getLocalRotation().fromAngleAxis(FastMath.PI,new Vector3f(0,1,0));
		}
		
		lockAll();
	}
}
