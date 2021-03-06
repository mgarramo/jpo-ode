package us.dot.its.jpo.ode.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OdePluginImpl implements Plugin {

	private List<Class<?>> classes = new ArrayList<Class<?>>();

	@Override
	public void load(Properties properties) throws ClassNotFoundException, IOException {
		Policy.setPolicy(new PluginPolicy());
		System.setSecurityManager(new SecurityManager());

		File plugins = new File("plugins");
		
		if (plugins.exists() && plugins.isDirectory()) {
			File[] files = plugins.listFiles();
			
			for (File pluginJarFile : files) {
				loadAllClasses(pluginJarFile);
			}
		}
	}
	
	private void loadAllClasses (File file) throws IOException, ClassNotFoundException {
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> e = jarFile.entries();

		URLClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
		
		while (e.hasMoreElements()) {
		    JarEntry je = e.nextElement();
		    if(je.isDirectory() || !je.getName().endsWith(".class")){
		        continue;
		    }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    classes.add(loader.loadClass(className));
		}
		jarFile.close();
	}

	public List<Class<?>> getClasses() {
		return classes;
	}


}
