package karsch.characters;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

@SuppressWarnings("serial")
public class Shadow extends Box{
	public Shadow(float sizeX, float sizeY) {
		super("shadow", new Vector3f(0,.2f, 0),sizeX, .0f, sizeY);
        
        // create new Blendstate
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.ConstantAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);

        as.setEnabled(true);
        
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setTexture(TextureManager.loadTexture(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,"SHADOW_ROUND.PNG"),
        		Texture.MinificationFilter.BilinearNearestMipMap,
        		Texture.MagnificationFilter.Bilinear));
	    ts.setEnabled(true);
        
        setRenderState(as);
        setRenderState(ts);
        setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
        
        lockBounds();
//		lockMeshes();
		lockShadows();
	}
}
