package karsch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Callable;

import com.jme.scene.Node;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.game.StandardGame;
import com.jmex.game.StandardGame.GameType;
import com.jmex.model.converters.MaxToJme;

public class ConvertModels {
	private static final String texdir = "karsch/resource/textures";
	private StandardGame app;
	public ConvertModels() {
		app = new StandardGame("", GameType.HEADLESS);
		app.start();
		
		GameTaskQueueManager.getManager().update(new Callable<Object>() {
            public Object call() throws Exception {
				try {
					ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL,
					        new SimpleResourceLocator(ConvertModels.class.getClassLoader().getResource("karsch/resource/models/")));
					ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE,
					        new SimpleResourceLocator(ConvertModels.class.getClassLoader().getResource("karsch/resource/textures/")));
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				
				long time = System.currentTimeMillis();
				
				String[] files = new File("src/karsch/resource/models").list(new Maxfilter());
				
				for (String file: files){
				
					String outfile = file.substring(0, file.indexOf('.')) + ".jme";
			        
			        Node r;
			
			    	MaxToJme converter=new MaxToJme();
			        ByteArrayOutputStream BO=new ByteArrayOutputStream();
			        URL model=ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL, file);
			        System.out.println("LOADED: src/karsch/resource/models/"+file);
			        
			        try {
//			        	converter.setProperty("mtllib", model);
//				        converter.setProperty("texdir", texdir);
			        	
						converter.convert(model.openStream(),BO);
				        
				        r = (Node)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
				        
				        boolean result = BinaryExporter.getInstance().save((Savable)r, new File("src/karsch/resource/models/"+outfile));
				        System.out.println("SAVED: src/karsch/resource/models/"+outfile + " " + result);
			        } catch (IOException e) {
						e.printStackTrace();
					}
		        }
				
				System.out.println("DONE: " + files.length + " files converted in " + (System.currentTimeMillis()-time) + " milliseconds");
				app.finish();
				return null;
            }
		});
		
		
	}
	
	public static void main(String args []){
		new ConvertModels();
	}
	
	
	class Maxfilter implements FilenameFilter{
//		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(".3ds")){
				return true;
			}	
			return false;
		}
	}
}
