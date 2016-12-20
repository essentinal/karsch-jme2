package karsch.level;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

@SuppressWarnings("serial")
public class PanoramaEnv extends Sphere{
	public PanoramaEnv(int x, int y, float radius, int samples) {
		super("environment", new Vector3f(0,0,0),
				samples, samples, radius);

		TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();

		ts.setTexture(TextureManager.loadTexture(
				ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, "ENV_FOREST.PNG"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear),0);


        ts.setEnabled(true);
		setRenderState(ts);
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(ColorRGBA.white);
		ms.setAmbient(ColorRGBA.white);
		ms.setEmissive(ColorRGBA.white);
		ms.setSpecular(ColorRGBA.white);
		ms.setEnabled(true);
		
		setRenderState(ms);
		
		CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Front);
        cs.setEnabled(true);
        setRenderState(cs);
		
		updateRenderState();
		setModelBound(new BoundingBox());
		updateModelBound();
		getLocalRotation().fromAngleAxis(FastMath.DEG_TO_RAD*-90, Vector3f.UNIT_X);
		getLocalScale().z = .5f;
		
		
//		setNormalsMode(NormalsMode.Off);
		
		setLocalTranslation(x*5, -10f, y*5);
		
		lockBounds();
//		lockMeshes();
		lockShadows();
		lockBranch();
	}
}
