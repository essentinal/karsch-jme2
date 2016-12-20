package karsch.level.tiles;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

@SuppressWarnings("serial")
public class Stone extends Tile{
	public Stone(int x, int y) {
		super("stone" + String.valueOf(x)+String.valueOf(y), x, y);
		attachChild(new Sphere("stone" + String.valueOf(x)+String.valueOf(y),
				new Vector3f(0,0,0),
				5, 5, 2.5f));

		TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

		ts.setTexture(TextureManager.loadTexture(
				ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "DIRT.PNG"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear),0);


        ts.setEnabled(true);
		setRenderState(ts);
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setEnabled(true);
		
		setRenderState(ms);
		
		updateRenderState();
		setModelBound(new BoundingBox());
		updateModelBound();
		setLocalRotation(new Matrix3f(	0,FastMath.rand.nextFloat(),0,
										0,0,FastMath.rand.nextFloat(),
										0,0,FastMath.rand.nextFloat()));
		setLocalTranslation(x*5+2.5f, 0, y*5+2.5f);
		
		lockAll();
	}
}
