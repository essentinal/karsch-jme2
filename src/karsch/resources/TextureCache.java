package karsch.resources;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.Font;
import org.fenggui.binding.render.ITexture;
import org.fenggui.util.Alphabet;
import org.fenggui.util.fonttoolkit.AssemblyLine;
import org.fenggui.util.fonttoolkit.BinaryDilation;
import org.fenggui.util.fonttoolkit.Clear;
import org.fenggui.util.fonttoolkit.DrawCharacter;
import org.fenggui.util.fonttoolkit.FontFactory;
import org.fenggui.util.fonttoolkit.PixelReplacer;

import com.jme.image.Texture;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;

public class TextureCache {
	private static TextureCache instance;
	private HashMap<String, ITexture> itextures;
	private HashMap<String, Texture> textures;
	
	private Font coolAWTSerifFont, coolAWTSerifFont2, standardFont;
	
	private TextureCache() {
		itextures = new HashMap<String, ITexture>();
		textures = new HashMap<String, Texture>();
	}
	
	public static TextureCache getInstance() {
		if (instance == null)
			instance = new TextureCache();
		return instance;
	}
	
	public Texture getTexture(String fileName){
		if (textures.containsKey(fileName)){
			return textures.get(fileName);
		}
		
		Texture tex = TextureManager.loadTexture(
				ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, fileName),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);
		
		if (tex == null){
			tex = TextureManager.loadTexture(
					ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, fileName.toUpperCase()),
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear);
		}

		textures.put(fileName, tex);
		return tex;
	}
	
	public ITexture getITexture(String fileName){
		if (itextures.containsKey(fileName)){
			return itextures.get(fileName);
		}
		
		ITexture tex=null;
		
		try {
			tex = Binding.getInstance().getTexture(
					ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, fileName));
			if (tex == null){
				tex = Binding.getInstance().getTexture(
						ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, fileName.toUpperCase()));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		itextures.put(fileName, tex);
		return tex;
	}
	
	public ITexture getScaledITexture(String fileName, int width, int height){
		String key = fileName + "_" + width + "_" + height;
		if (itextures.containsKey(key)){
			return itextures.get(key);
		}
		
		ITexture tex=null;
		
		try {
			BufferedImage image = ImageIO.read(
					ResourceLocatorTool.locateResource(
							ResourceLocatorTool.TYPE_TEXTURE, fileName));
			if (image == null){
				image = ImageIO.read(
						ResourceLocatorTool.locateResource(
								ResourceLocatorTool.TYPE_TEXTURE, fileName.toUpperCase()));
			}

			Image newImg = image.getScaledInstance(width, height ,Image.SCALE_AREA_AVERAGING);
			
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D biContext = bi.createGraphics();
			biContext.drawImage(newImg, 0, 0, null);
			
			biContext.dispose();
			
			tex = Binding.getInstance().getTexture(bi);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		itextures.put(key, tex);
		return tex;
	}
	

	public Font getBoldFont() {
		if (coolAWTSerifFont == null){
			java.awt.Font awtFont = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24);
			
			FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
			AssemblyLine line = ff.getAssemblyLine();
			
			Paint redYellowPaint = new java.awt.GradientPaint(0, 0, java.awt.Color.RED, 15, 15, java.awt.Color.YELLOW, true);
			
			line.addStage(new Clear());
			line.addStage(new DrawCharacter(java.awt.Color.WHITE, false));
			
			line.addStage(new BinaryDilation(java.awt.Color.BLACK, 3));
			line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
			line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
			
			coolAWTSerifFont = ff.createFont();
		}
		return coolAWTSerifFont;
	}
	
	public Font getFont() {
		if (coolAWTSerifFont2 == null){
			java.awt.Font awtFont = new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 18);
			
			FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
			AssemblyLine line = ff.getAssemblyLine();
			
			Paint redYellowPaint = new java.awt.GradientPaint(-1, -1, java.awt.Color.RED, 10, 10, java.awt.Color.YELLOW, true);
			
			line.addStage(new Clear());
			line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
			
			line.addStage(new BinaryDilation(java.awt.Color.BLACK, 2));
			line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
			line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
			
			coolAWTSerifFont2 = ff.createFont();
		}
		return coolAWTSerifFont2;
	}
	
	public Font getStandardFont() {
		if (standardFont == null){
			java.awt.Font awtFont = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 20);
			
			FontFactory ff = new FontFactory(Alphabet.GERMAN, awtFont);
			AssemblyLine line = ff.getAssemblyLine();
			
//			Paint redYellowPaint = new java.awt.GradientPaint(-1, -1, java.awt.Color.RED, 10, 10, java.awt.Color.YELLOW, true);
			
//			line.addStage(new Clear());
//			line.addStage(new DrawCharacter(java.awt.Color.WHITE, true));
//			
//			line.addStage(new BinaryDilation(java.awt.Color.BLACK, 2));
			line.addStage(new DrawCharacter(java.awt.Color.BLACK, true));
//			line.addStage(new PixelReplacer(redYellowPaint, java.awt.Color.WHITE));
			
			standardFont = ff.createFont();
		}
		return standardFont;
	}
	
}
