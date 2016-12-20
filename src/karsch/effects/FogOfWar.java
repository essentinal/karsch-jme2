package karsch.effects;

import karsch.resources.TextureCache;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class FogOfWar extends Node{
	public FogOfWar() {
		Box quad = new Box("fogofwar", new Vector3f(0,7,0),70,0,70);
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    
	    ts.setTexture(TextureCache.getInstance().getTexture("FOGOFWAR.PNG"),0);
	    quad.setRenderState(ts);
	    
	    BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
//        bs.setReference(0.1f);

        bs.setEnabled(true);
        
        quad.setRenderState(bs);
        quad.updateRenderState();
        
        attachChild(quad);
	}
}
