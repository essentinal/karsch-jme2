package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.resources.ModelCache;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

@SuppressWarnings("serial")
public class Fence extends Tile {
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	
	public Fence(int x, int y, BufferedImage levelImage){
		super("fence" + x + "" + y, x, y, levelImage);
		detectFence(levelImage);
				
		lockAll();
	}
	
	private void detectFence(BufferedImage levelImage){
		int actPixel = levelImage.getRGB(x, y);
		
		if ((x>0) && (levelImage.getRGB(x-1,y) == actPixel)){
			left = true;
		}
		if ((y>0) && (levelImage.getRGB(x,y-1) == actPixel)){
			up = true;
		}
		if ((x<=levelImage.getWidth()) && (levelImage.getRGB(x+1,y) == actPixel)){
			right = true;
		}
		if ((y<=levelImage.getHeight()) && (levelImage.getRGB(x,y+1) == actPixel)){
			down = true;
		}	
		createFence();
	}
	
	private void createFence(){
		if(left && right && !up && !down){
			model = ModelCache.getInstance().get("fence1.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);			
		} else if (!left && !right && up && down){
			model = ModelCache.getInstance().get("fence1.3ds");
			model.setLocalScale(.025f); 
			attachChild(model);
			getLocalRotation().fromAngleAxis(FastMath.PI/2,new Vector3f(0,1,0));
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);		
		} else if (!left && right && !up && down){
			model = ModelCache.getInstance().get("fence1_edge.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			getLocalRotation().fromAngleAxis(-FastMath.PI/2,new Vector3f(0,1,0));
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);
		} else if (!left && right && up && !down){
			model = ModelCache.getInstance().get("fence1_edge.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);		
		} else if (left && !right && !up && down){
			model = ModelCache.getInstance().get("fence1_edge.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			getLocalRotation().fromAngleAxis(FastMath.PI,new Vector3f(0,1,0));
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);
		} else if (left && !right && up && !down){
			model = ModelCache.getInstance().get("fence1_edge.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			getLocalRotation().fromAngleAxis(-FastMath.PI/2f*3f,new Vector3f(0,1,0));
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);	
		} else if((left && !right && !up && !down) || (!left && right && !up && !down)){
			model = ModelCache.getInstance().get("fence1.3ds");
			model.setLocalScale(.025f);
			attachChild(model);
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f);
		 }else if((!left && !right && up && !down) || (!left && !right && !up && down)){
			model = ModelCache.getInstance().get("fence1.3ds");
			model.setLocalScale(.025f); 
			attachChild(model);
			getLocalRotation().fromAngleAxis(FastMath.PI/2,new Vector3f(0,1,0));
			setLocalTranslation(x*5f+2.5f, 0, y*5+2.5f); 
		}
	}
}