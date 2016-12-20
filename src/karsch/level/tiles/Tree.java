package karsch.level.tiles;

import karsch.resources.ModelCache;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Tree extends Tile{
	public Tree(int x, int y, float scale) {
		super("tree" + x + " " + y, x, y);
		model = ModelCache.getInstance().get("tree1.3ds");
		model.setLocalScale(.1f);

		float randX = 3*0.5f-FastMath.rand.nextFloat();
		float randY = 3*0.5f-FastMath.rand.nextFloat();
		setLocalTranslation(new Vector3f(x*5+2f +randX, 4 ,y*5+2f +randY));
		attachChild(model);
		setLocalScale(scale + (0.5f*FastMath.rand.nextFloat()) );
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setAmbient(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setEnabled(true);
        
		setRenderState(ms);
		
		updateRenderState();
		
		lockAll();
	}
}
