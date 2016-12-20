package karsch.level.tiles;

import java.awt.image.BufferedImage;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;

public abstract class Tile extends Node{
	final Tile tile;
	protected int x, y;
	protected Node model;
	protected BufferedImage levelImage;
	
	public Tile(String name, int x, int y) {
		super(name);
		tile = this;
		this.x = x;
		this.y = y;
	}
	
	public Tile(String name, int x, int y, BufferedImage levelImage) {
		super(name);
		tile = this;
		this.x = x;
		this.y = y;
		this.levelImage = levelImage;
	}
	
	public Tile(String name){
		super(name);
		tile = this;
	}
	
	protected void lockAll(){
		setModelBound(new BoundingBox());
		updateModelBound();
		
		tile.lockBounds();
    	tile.lockMeshes();
    	tile.lockShadows();
    	tile.lockBranch();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
