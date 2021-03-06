import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/*
 * http://www.compiletimeerror.com/2013/08/java-downloadextract-all-images-from.html#.VaqL5vmN9Cg
 */
public class Homework4HTMLEditorKit {
	public static void main(String[] args) throws IOException {
		String webUrl = "http://www.hdwallpapers.in/";
		String target = "/tmp/";
		URL url = new URL(webUrl);
		URLConnection connection = url.openConnection();

		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		HTMLEditorKit htmlKit = new HTMLEditorKit();
		HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
		HTMLEditorKit.Parser parser = new ParserDelegator();
		HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
		parser.parse(br, callback, true);
		
		for (HTMLDocument.Iterator iterator = htmlDoc.getIterator(HTML.Tag.IMG); iterator.isValid(); iterator.next()) {			
			AttributeSet attributes = iterator.getAttributes();
			String imgSrc = (String) attributes
					.getAttribute(HTML.Attribute.SRC);
			log(imgSrc);
			
			if (imgSrc != null && (imgSrc.endsWith(".jpg") || (imgSrc.endsWith(".png")) || (imgSrc.endsWith(".jpeg")) || (imgSrc.endsWith(".bmp")) || (imgSrc.endsWith(".ico")))) {
                try {
                    downloadImage(webUrl, imgSrc, target);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
		}
	}
	
	private static void log(String m) {
		System.out.println(m);
	}
	
    private static void downloadImage(String url, String imgSrc, String target) throws IOException {
        BufferedImage image = null;
        try {
            if (!(imgSrc.startsWith("http"))) {
                url = url + imgSrc;
            } else {
                url = imgSrc;
            }
            imgSrc = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);
            String imageFormat = null;
            imageFormat = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
            String imgPath = null;
            imgPath = target + imgSrc + "";
            URL imageUrl = new URL(url);
            image = ImageIO.read(imageUrl);
            if (image != null) {
                File file = new File(imgPath);
                ImageIO.write(image, imageFormat, file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
