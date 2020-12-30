import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageProcessingUsingSingleAndMultiThread {
	
	public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
	public static final String DESTINATION_FILE = "./out/many-flowers.jpg";
	
	
	public static void main(String[] args) throws IOException {
		BufferedImage origImg = ImageIO.read(new File(SOURCE_FILE));
		BufferedImage resImg = new BufferedImage(origImg.getWidth(), origImg.getHeight(), BufferedImage.TYPE_INT_BGR);
		long startTime = System.currentTimeMillis();
		//recolorSingleThreaded(origImg, resImg);
		recolorMultiThreaded(origImg, resImg, 8);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		File outputFile = new File(DESTINATION_FILE);
		ImageIO.write(resImg, "jpg", outputFile);
		System.out.println("duration: "+String.valueOf(duration));
	}
	
	
	public static void recolorMultiThreaded(BufferedImage origImg, BufferedImage resImg, int noOfThreads) {
		List<Thread> threads = new ArrayList<>();
		int width = origImg.getWidth();
		int height = origImg.getHeight() / noOfThreads;
		
		for(int i = 0 ; i < noOfThreads; i++) {
			final int threadMultiplier = i;
			
			Thread thread = new Thread(()->{
				int leftCorner = 0;
				int topCorner = height * threadMultiplier;
				recolorImage(origImg, resImg, leftCorner, topCorner, width, height);
			});
			threads.add(thread);
		}
		
		for(Thread thread : threads) {
			thread.start();
		}
		for(Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void recolorSingleThreaded(BufferedImage origImg, BufferedImage resImg) {
		recolorImage(origImg, resImg, 0, 0,origImg.getWidth(), origImg.getHeight());
	}
	
	
	public static void recolorImage(BufferedImage origImg, BufferedImage resImg, int leftCorner, int topCorner,
			int width, int height) {
		
		for(int x = leftCorner; x < leftCorner + width & x < origImg.getWidth(); x++) {
			for(int y = topCorner; y < topCorner + height && y < origImg.getHeight() ; y++) {
				recolorPixel(origImg, resImg, x, y);
			}
		}
		
	}
	
	public static void recolorPixel(BufferedImage origImg, BufferedImage resImg, int x, int y) {
		int rgb = origImg.getRGB(x, y);
		
		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		
		int newRed, newGreen, newBlue;
		
		if(isShadeOfGray(red, green, blue)) {
			newRed = Math.min(255, red+10);
			newGreen = Math.max(0, green-80);
			newBlue= Math.max(0, blue-20);
		}
		else {
			newRed = red;
			newGreen = green;
			newBlue = blue;
		}
		
		int newRgb = createRGBFromColors(newRed, newGreen, newBlue);
		setRGB(resImg, x, y, newRgb);
	}
	
	public static void setRGB(BufferedImage image, int x, int y, int rgb){
		image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
		
	}
	
	
	public static boolean isShadeOfGray(int red, int green, int blue) {
		return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
	}
	
	
	
	public static int createRGBFromColors(int red, int green, int blue) {
		
		int rgb = 0;
		
		rgb |= blue;
		rgb |= green << 8;
		rgb |= red << 16;
		
		rgb |= 0xFF000000;
		
		return rgb;
		
	}
	
	
	public static int getRed(int rgb) {
		return (rgb & 0x00FF0000) >> 16;
	}
	
	public static int getGreen(int rgb) {
		return (rgb & 0x0000FF00) >> 8;
	}
	
	public static int getBlue(int rgb) {
		return rgb & 0x000000FF;
	}

}
