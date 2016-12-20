package karsch;

import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import karsch.level.LevelManager;
import karsch.utils.StreamGobbler;

import com.jme.util.GameTaskQueueManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class KarschSimpleGame extends KarschAbstractGame{
	public static final String VERSION = "1.0 ";
	public static final String AUTHORS = "David Walter & Stephan Dreyer";
	public static final boolean DEBUG  = false;
	private static boolean isjar = true;
	private static boolean isforked = false;
	
	private static KarschSimpleGame app;
	
	public static void main (String args[]){
		
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--nofork")) {
				isforked = true;
			}
		}
		
		
		if (isforked) {
			app = new KarschSimpleGame();
			app.setConfigShowMode(ConfigShowMode.ShowIfNoConfig, KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/textures/karsch1.jpg"));
			app.start();
			
			GameTaskQueueManager.getManager().update(new Callable<Object>() {
	            public Object call() throws Exception {
	            	LevelManager.getInstance().loadMenu();
					
					return null;
	            }
			});
	
			Values values = Values.getInstance();
			values.setSettings(app.getSettings());
		} else {
			try {
				// class path, the quotes are necessary if a directory name contains spaces
				
				String vmargs = " -Xms128m -Xmx512m -XX:PermSize=128m -Djava.library.path=lib ";
				String classpath = System.getProperty("java.class.path");
				if (classpath == null || classpath.length() == 0){
					classpath = "Karsch.jar";
				}
				
				String params = " --nofork";
				String command;
				
				if (isjar) { // if started from jar, fork the jar file 
					
					String jarfile = "Karsch.jar";
					
					// if run in windows, the filename must be quoted to avoid problems with whitespaces
					boolean iswindows = System.getProperty("os.name").toLowerCase().contains("windows");
					if (iswindows){
						System.out.println("Operation System: Windows");
						jarfile = quote(jarfile);
					}

					command = "java " + "-cp " + quote(".:"+classpath) + vmargs  + " -jar " + jarfile + params;
				
				} else { // fork the class file
					
					command = "java -cp " + classpath + vmargs + " " + "karsch.KarschSimpleGame" + params;
				}

				// get the runtime
				Runtime rt = Runtime.getRuntime();

				System.out.println("forking, command is: \n" + command);

				// execute command/fork process
				Process proc = rt.exec(command);

				System.out.println("start");

				// redirect error
				StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(),
						StreamGobbler.TYPE_ERROR, true);

				// redirect output
				StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),
						StreamGobbler.TYPE_OUTPUT, true);

				// start gobbler threads
				errorGobbler.start();
				outputGobbler.start();

				// any error???
				int exitVal = proc.waitFor();
				System.out.println("exit");
				System.out.println("ExitValue: " + exitVal);

			} catch (Exception e) {
				System.err.println("Fatal error");
				e.printStackTrace();
			}
		}
	}
		
	private static String quote(String string){
		return " \""+ string + "\" ";
	}
	
	
	public KarschSimpleGame() {
		super("Karsch the pig");
		try {
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/textures/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/levels/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/models/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/sound/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/sound/speech/gunther/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/sound/speech/karsch/")));
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_AUDIO,
			        new SimpleResourceLocator(KarschSimpleGame.class.getClassLoader().getResource("karsch/resource/sound/speech/mrskarsch/")));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
}