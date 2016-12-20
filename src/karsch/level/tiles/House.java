package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.resources.ModelCache;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class House extends Tile{
	private boolean newHouse = true;
	protected Node model;
	
	public House(int x, int y, BufferedImage levelImage){
		super("house" + x + "" + y, x, y, levelImage);
		
		detectHouse();
//		lockAll();
	}
	
	private void detectHouse(){
		int actPixel = levelImage.getRGB(x, y);
		int xSize = 1;
		int ySize = 1;		
		
		if ((x>0) && (levelImage.getRGB(x-1,y) == actPixel)){
			newHouse = false;
		} else if ((y>0) && (levelImage.getRGB(x,y-1) == actPixel)){
			newHouse = false;
		} else {
			int i=1;
			while((x+i <= levelImage.getWidth()) && (levelImage.getRGB(x+i, y) == actPixel)){
				xSize++;
				i++;
			}
			i=1;
			while((y+i <= levelImage.getHeight()) && (levelImage.getRGB(x,y+i) == actPixel)){
				ySize++;
				i++;
			}
		}		
		if (newHouse){
			createHouse(xSize, ySize);
		}
	}
	
	private void createHouse(int xSize, int ySize){
		model = ModelCache.getInstance().get("house1.3ds");
		model.setLocalScale(new Vector3f(1f/12f,1f/16f,1f/16f));
		attachChild(model);
		
		setModelBound(new BoundingBox());
		updateModelBound();
		
		setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		
		
		if(xSize > ySize){
			getLocalRotation().fromAngleAxis(-FastMath.PI/2,new Vector3f(0,1,0));
			setLocalScale(new Vector3f(ySize, xSize, 8));
		} else {
			setLocalScale(new Vector3f(xSize, ySize, 8));
		}
		
		setLocalTranslation((x+xSize/2f)*5f, 0f, (y+ySize/2f)*5f);
	}
	
	public boolean getNewHouse(){
		return newHouse;
	}
}
