package karsch.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.CloneImportExport;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jmex.model.converters.MaxToJme;


public class ModelCache {	
	private static ModelCache instance;
	private CloneImportExport ie;
	private HashMap<String, Savable> models;
	private ModelCache() {
		ie = new CloneImportExport();
		models = new HashMap<String, Savable>();
	}
	
	public static ModelCache getInstance(){
		if (instance == null){
			instance = new ModelCache();
		}
		return instance;
	}
	
	public Node get(String name){
		Node node = (Node) models.get(name);
		if (node == null) {
			node = (Node) load3ds(name);
		} else {
			System.out.println("clone of " + name + " loaded from cache");
		}
		
		// puts a new clone in the hashmap
		ie.saveClone(node);
		models.put(name, ie.loadClone());
		
		return node;
	}
	
	public static Spatial loadJME(String modelPath) {
		try {
            String outfile = modelPath;
            if (!outfile.endsWith(".jme")){
            	outfile = modelPath.substring(0, modelPath.indexOf('.')) + ".jme";
            }
            	
            Node r;
            
            BinaryImporter bi = BinaryImporter.getInstance();
            BinaryImporter.debug = true;

            System.out.println(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL,outfile) + " loaded");
        	r = (Node) bi.load(ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_MODEL,outfile));
        	
        	
            Quaternion temp=new Quaternion();
            temp.fromAngleAxis(FastMath.HALF_PI,new Vector3f(-1,0,0));
            r.setLocalRotation(temp);
            r.setIsCollidable(true);
            r.setModelBound(new BoundingBox());
            r.updateModelBound();
            r.updateRenderState();

            return r;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
	}
	
	public static Spatial load3ds(String modelPath) {
		try {
			URL model = ResourceLocatorTool.locateResource(
					ResourceLocatorTool.TYPE_MODEL, modelPath);

			MaxToJme converter = new MaxToJme();
			ByteArrayOutputStream BO = new ByteArrayOutputStream();
			
			converter.setProperty("texurl", model);
			converter.setProperty("mtllib", model);
			System.out.println(Thread.currentThread().getContextClassLoader().getResource("karsch/resource/textures"));
			converter.convert(model.openStream(), BO);


			Node r = (Node) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(BO.toByteArray()));
			


			Quaternion temp=new Quaternion();
			temp.fromAngleAxis(FastMath.HALF_PI,new Vector3f(-1,0,0));
			r.setLocalRotation(temp);
			r.setIsCollidable(true);
			r.setModelBound(new BoundingBox());
			r.updateModelBound();
			r.updateRenderState();
			
			return r;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	public static void save(Spatial r, String outfile) throws IOException{
//		BinaryExporter.getInstance().save((Savable)r, new File("src/karsch/resource/models/"+outfile));
//	}
}
