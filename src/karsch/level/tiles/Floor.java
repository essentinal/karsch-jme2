package karsch.level.tiles;

import karsch.level.Level;
import karsch.resources.TextureCache;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Floor extends Tile{
	public Floor(int xSize, int ySize, int levelStyle) {
		super("floor");
		attachChild(new Box("floor",
        		new Vector3f(0.5f,-.1f,.5f),
        		new Vector3f(xSize*5+0.5f,0,ySize*5+0.5f)));
		
        
        TextureState ts=DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        
        if(levelStyle == Level.LS_FARM){
        	ts.setTexture(
    				TextureCache.getInstance().getTexture("GRASS2.PNG"),0);
        } else if (levelStyle == Level.LS_FORREST){
        	ts.setTexture(
    				TextureCache.getInstance().getTexture("GRASS2_DARK.PNG"),0);
        } else {
        	ts.setTexture(
    				TextureCache.getInstance().getTexture("DIRT2.PNG"),0);
        } 

        ts.setEnabled(true);
        
		ts.getTexture().setWrap(Texture.WrapMode.Repeat);
		ts.getTexture().setScale(new Vector3f(xSize,ySize,ySize));
		
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		ms.setDiffuse(new ColorRGBA(.7f,.7f,.7f,1f));
		ms.setEnabled(true);
        
		setRenderState(ms);
		setRenderState(ts);
        
        updateRenderState();
        setLocalTranslation(-xSize*2.5f,0,-ySize*2.5f);
        lockAll();
	}
}
