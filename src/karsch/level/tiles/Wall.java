package karsch.level.tiles;

import karsch.resources.TextureCache;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Wall extends Tile{
	public Wall(int x, int y) {
		this(x, y, false);
	}
	
	public Wall(int x, int y, boolean black) {
		super("wall" + String.valueOf(x)+String.valueOf(y), x, y);
		attachChild(new Box("wall" + String.valueOf(x)+String.valueOf(y),
				new Vector3f(x*5, 0, y*5),
				new Vector3f(x*5+5f, 5f, y*5+5f)));

		if (black){
			MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
			ms.setAmbient(ColorRGBA.black);
			ms.setDiffuse(ColorRGBA.black);
			
			ms.setEnabled(true);
			
			setRenderState(ms);
		} else {
			TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	
			ts.setTexture(
					TextureCache.getInstance().getTexture("DIRT.PNG"),0);
	
	
	        ts.setEnabled(true);
			setRenderState(ts);
		}
		updateRenderState();
		setModelBound(new BoundingBox());
		updateModelBound();
		
		lockAll();
	}
}
