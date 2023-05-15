import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
public class ImageDecrypterv2 {
    
    public static void main(String args[])throws IOException{
    	int width = 1018;	//width of the image
    	int height = 1018;   //height of the image
    	BufferedImage image = null;
    	File f = null;
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Enter the file path of the image you want to decrypt: ");
    	String fileName = scan.nextLine();
   	 
    	//read image
    	try{
      	f = new File(fileName); //image file path
      	BufferedImage rawImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      	rawImage = ImageIO.read(f);
      	image = resizeImage(rawImage, width, height);
      	System.out.println("Reading complete.");
    	}catch(IOException e){
      	System.out.println("Error: "+e);
    	}
   	 
    	System.out.println("Enter the decryption code for that image: ");
   	 
    	String codestring = scan.nextLine();
    	int code = Integer.parseInt(codestring);
    	int root1 = code / 10000;
    	int root2 = code % 10000;
   	 
    	BufferedImage unscrambledImage1 = unscrambleCols (image, root1);

    	BufferedImage unscrambledImage2 = unscrambleRows (unscrambledImage1, root2);
    	System.out.println("Image unscrambled.");

    	System.out.println("Enter the file path of where you would like to save the result: ");
    	String fileName2 = scan.nextLine();
   	 
   			 
    	File h = new File(fileName2);
   	 
    	ImageIO.write(unscrambledImage2, "png", h);
    	System.out.println("Writing complete.");
   	 
   	 
      }
    public static int prToThe (int base, int k) {
   	 int value = 1;
   	 for (int i = 0; i < k; i++) {
   		 value*=base;
   		 value%=1019;
   	 }
   	 return value;
    }
    static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
    	BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics2D = resizedImage.createGraphics();
    	graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    	graphics2D.dispose();
    	return resizedImage;
    }
    
    public static int discreteLogBasePrMod1019 (int base, int k) {
   	 int value = 0;
   	 for (int i = 0; i < 1019; i++) {
   		 if (prToThe(base, i) % 1019 == k) {
   			 value = i;
   			 break;
   		 }
   	 }
   	 return value;
    }
    
    //use pr1
    public static ArrayList<Color> decryptRows (ArrayList <Color> original, int pr) {
   	 ArrayList<Color> newArr = new ArrayList<Color>();
   	 for (int i = 0; i < original.size(); i++) {
   		 newArr.add(original.get(discreteLogBasePrMod1019(pr, i+1)));
   	 }
   	 return newArr;
    }
    
    //use pr2
    public static ArrayList<Color> decryptCols (ArrayList <Color> original, int pr) {
   	 ArrayList<Color> newArr = new ArrayList<Color>();
   	 for (int i = 0; i < original.size(); i++) {
   		 newArr.add(original.get(discreteLogBasePrMod1019(pr, i+1)));
   	 }
   	 return newArr;
    }

    
    public static BufferedImage unscrambleCols (BufferedImage original, int pr) {
   		 BufferedImage unscrambled = new BufferedImage (original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
   		 
   		 //scramble each row
   		 for (int y = 0; y < original.getHeight(); y++) {
   			 ArrayList<Color> origRow = new ArrayList <Color> ();
   			 for (int j = 0; j < original.getWidth(); j++) {
   				 Color c = new Color(original.getRGB(j, y));
   				 origRow.add(c);
   			 }
   			 
   			 ArrayList<Color> unscrambledRow = decryptRows (origRow, pr);
   			 for (int j = 0; j < original.getWidth(); j++) {
   				 int newRGB = unscrambledRow.get(j).getRGB();
   				 unscrambled.setRGB(j, y, newRGB);
   			 }
   			 System.out.println("Column " + y + " unscrambled");

   		 }
   		 return unscrambled;
   	 }
    
    public static BufferedImage unscrambleRows (BufferedImage original, int pr) {
   	 BufferedImage unscrambled = new BufferedImage (original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
   	 
   	 //scramble each column
   	 for (int x = 0; x < original.getWidth(); x++) {
   		 ArrayList<Color> origCol = new ArrayList <Color> ();
   		 for (int j = 0; j < original.getHeight(); j++) {
   			 Color c = new Color(original.getRGB(x, j));
   			 origCol.add(c);
   		 }
   		 
   		 ArrayList<Color> unscrambledCol = decryptCols (origCol, pr);
   		 for (int j = 0; j < original.getHeight(); j++) {
   			 int newRGB = unscrambledCol.get(j).getRGB();
   			 unscrambled.setRGB(x, j, newRGB);
   		 }
   		 System.out.println("Row " + x + " unscrambled");

   	 }
   	 return unscrambled;
    }

}

