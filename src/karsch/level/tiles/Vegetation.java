package karsch.level.tiles;

import karsch.level.Freefield;
import karsch.level.Level;
import karsch.level.LevelMap;
import karsch.resources.TextureCache;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.scene.BillboardNode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Vegetation extends Tile{
	public Vegetation(LevelMap levelMap) {
		super("vegetation");
		
		for (int i=0; i < levelMap.getXSize(); i++){
    		for (int j=0; j < levelMap.getYSize(); j++){
    			if (levelMap.getLevelMap()[i][j] instanceof Freefield){
    				if (levelMap.getStyle() == Level.LS_FARM){
    					addFlowers(i, j, "FLOWER", .5f);
    				} else if (levelMap.getStyle() == Level.LS_FORREST){
    					addFlowers(i, j, "MUSHROOM", .5f);
    				} else if (levelMap.getStyle() == Level.LS_UNDERGRND){
    					addFlowers(i, j, "ROCK", .5f);
    				} else if (levelMap.getStyle() == Level.LS_HOUSE){
    					addFlowers(i, j, "SHIT", .3f);
    				} 
    			}
    		}
    	}
		
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.One);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);

        bs.setEnabled(true);
        
        setRenderState(bs);
        updateRenderState();
		
		setModelBound(new BoundingBox());
		updateModelBound();
		
		if (levelMap.getStyle() == Level.LS_HOUSE){
			bs.setReference(0.6f);
			bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		}
			
		lockAll();
	}
	
	private void addFlowers(int x, int y, String fileName, float statSize){
		int random = FastMath.rand.nextInt(10);
		if (random > 2)
			return;
		
		BillboardNode bbn = new BillboardNode("vegNode");
//		bbn.setAlignment(BillboardNode.CAMERA_ALIGNED);
		
		float randSize = FastMath.rand.nextFloat();
		
		Quad quad = new Quad(fileName+x+y, statSize+randSize, statSize+randSize);
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(TextureCache.getInstance().getTexture(fileName + (random+1) + ".PNG"));
		quad.setRenderState(ts);
		
        quad.setLightCombineMode(LightCombineMode.Off);
		
		quad.updateRenderState();
		
		bbn.attachChild(quad);

		bbn.setLocalTranslation( x*5+1.5f+FastMath.rand.nextInt(2) , .5f, y*5+1.5f+FastMath.rand.nextInt(2));
		
		attachChild(bbn);
	}
}
