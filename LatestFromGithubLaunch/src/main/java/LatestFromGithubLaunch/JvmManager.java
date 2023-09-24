package LatestFromGithubLaunch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JvmManager {
	private String location = null;

	public String getJVM() {
		return location;
	}

	public JvmManager(String githubProject, String girhubRepo, String jvm, String latestVersionString, String bindir)
			throws Exception {
		String urlsource = "https://api.github.com/repos/" + githubProject + "/" + girhubRepo + "/releases/latest";
		InputStream issource = new URL(urlsource).openStream();
		String downloadURL = null;
		long size = 0;
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
		Type TT_mapStringString = new TypeToken<HashMap<String, Object>>() {
		}.getType();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(issource, Charset.forName("UTF-8")));
			String jsonText = LatestFromGithubLaunchUI.readAll(rd);
			// Create the type, this tells GSON what datatypes to instantiate when parsing
			// and saving the json

			// chreat the gson object, this is the parsing factory
			HashMap<String, Object> database = gson.fromJson(jsonText, TT_mapStringString);
			latestVersionString = (String) database.get("tag_name");
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> assets = (List<Map<String, Object>>) database.get("assets");
			for (Map<String, Object> key : assets) {
				if (((String) key.get("name")).contentEquals(jvm)) {
					downloadURL = (String) key.get("browser_download_url");
					size = ((Double) key.get("size")).longValue();
					System.out.println(downloadURL + " Size " + size + " bytes");
				}
			}
		} finally {
			issource.close();
		}
		if (downloadURL == null)
			throw new RuntimeException("Url of jvm missing");
		System.out.println("Found JMV config " + downloadURL);
		URL url = new URL(downloadURL);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		ProcessInputStream pis = new ProcessInputStream(is, (int) size);

		File folder = new File(bindir + latestVersionString + "/");
		File json = new File(bindir + latestVersionString + "/" + jvm);
		if (!folder.exists() || !json.exists()) {
			folder.mkdirs();
			json.createNewFile();
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			FileOutputStream fileOutputStream = new FileOutputStream(json.getAbsoluteFile());
			while ((bytesRead = pis.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			fileOutputStream.close();
			pis.close();
		}
		String content = new String(Files.readAllBytes(Paths.get(json.getAbsolutePath())));
		System.out.println(content);
		HashMap<String, Object> database = gson.fromJson(content, TT_mapStringString);
		String jvmPackageURL = null;
		if (StudioBuildInfo.isLinux()) {
			jvmPackageURL = database.get("Linux-amd64").toString();
		}
		if (StudioBuildInfo.isWindows()) {
			jvmPackageURL = database.get("windows").toString();
		}
		if (StudioBuildInfo.isMac()) {
			if (StudioBuildInfo.isM1MacOS())
				jvmPackageURL = database.get("mac-aarch64").toString();
			else
				jvmPackageURL = database.get("mac-intel").toString();
		}
		if (jvmPackageURL == null)
			throw new RuntimeException(
					"Can not detect OS " + System.getProperty("os.arch") + " " + System.getProperty("os.name"));
		System.out.println("Getting VM from " + jvmPackageURL);
		File archive = getArchive(jvmPackageURL, bindir);
		String filename = archive.getName();
		String archiveBase = "";
		File destination = new File(bindir + "/java17/");
		Archiver archiver = null;

		System.out.println("Extracting JVM");
		if (filename.endsWith(".tar.gz")) {
			System.out.println("Tar file found");
			if (!destination.exists())
				archiver = ArchiverFactory.createArchiver("tar", "gz");
			archiveBase = filename.split("\\.tar")[0];
		}
		if (filename.endsWith(".zip")) {
			System.out.println("Zip file found ");
			if (!destination.exists())
				archiver = ArchiverFactory.createArchiver("zip");
			archiveBase = filename.split("\\.zip")[0];
		}
		if (archiver != null)
			archiver.extract(archive, destination);

		location = destination.getAbsolutePath() + "/" + archiveBase + "/bin/java";
		if(!new File(location).exists())
			throw new RuntimeException("JVM not extracted properly");
		System.out.println("Setting location of JVM to " + location);
	}

	private File getArchive(String downloadURL, String bindir) throws IOException {
		String[] urlparts = downloadURL.split("/");

		String filename = urlparts[urlparts.length - 1];

		URL url = new URL(downloadURL);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();

		File folder = new File(bindir + "/");
		File json = new File(bindir + "/" + filename);
		if (!folder.exists() || !json.exists()) {
			folder.mkdirs();
			json.createNewFile();
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			FileOutputStream fileOutputStream = new FileOutputStream(json.getAbsoluteFile());
			while ((bytesRead = is.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			fileOutputStream.close();
			is.close();
		}
		return json;
	}

}
