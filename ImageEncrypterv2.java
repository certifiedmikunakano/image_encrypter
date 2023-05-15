import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
public class ImageEncrypterv2 {
    
    public static void main(String args[])throws IOException{
   	 Random random = new Random();
    	int width = 1018;	//width of the image
    	int height = 1018;   //height of the image
    	//generate the primitive roots mod 1019
   	 
    	ArrayList <Integer> proots = new ArrayList <Integer> ();
    	for (int i = 0; i < 1018; i++) {
   		 proots.add(i);
    	}
    	for (int i = 0; i < 1019; i++) {
   		 int j = proots.indexOf((i*i) % 1019);
   		 if (j != -1) {
   			 proots.remove(j);
   		 }
   		 
    	}
    	int randomIndex1 = random.nextInt(proots.size());
    	int randomIndex2 = random.nextInt(proots.size());
    	int proot1 = proots.get(randomIndex1);
    	int proot2 = proots.get(randomIndex2);
   	 
   	 
    	BufferedImage image = null;
    	File f = null;
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Enter the file path of the image you want to encrypt: ");
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
   	 
    	BufferedImage scrambledImage = scramble(image, proot1);

    	BufferedImage scrambledImage2 = scramble2(scrambledImage, proot2);
    	System.out.println("Image Scrambled.");

    	System.out.println("Enter the file path of where you would like to save the result: ");
    	String fileName2 = scan.nextLine();
   	 
   			 
    	File h = new File(fileName2);
   	 
    	ImageIO.write(scrambledImage2, "png", h);
    	System.out.println("Image writing complete.");
   	 
    	int secretCode = 10000 * proot1 + proot2;
   	 
    	System.out.println("Your decryption code is: " + secretCode + ". Keep this code to yourself but don't lose it!");
   	 
   	 
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
    
    public static ArrayList<Color> encrypt (ArrayList <Color> original, int pr) {
   	 ArrayList<Color> newArr = new ArrayList<Color>();
   	 for (int i = 0; i < original.size(); i++) {
   		 newArr.add(original.get(prToThe(pr, i) - 1));
   	 }
   	 return newArr;
    }
    
    public static ArrayList<Color> encrypt2 (ArrayList <Color> original, int pr) {
   	 ArrayList<Color> newArr = new ArrayList<Color>();
   	 for (int i = 0; i < original.size(); i++) {
   		 newArr.add(original.get(prToThe(pr, i) - 1));
   	 }
   	 return newArr;
    }

    
    public static BufferedImage scramble (BufferedImage original, int pr) {
   		 BufferedImage scrambled = new BufferedImage (original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
   		 
   		 //scramble each row
   		 for (int y = 0; y < original.getHeight(); y++) {
   			 ArrayList<Color> origRow = new ArrayList <Color> ();
   			 for (int j = 0; j < original.getWidth(); j++) {
   				 Color c = new Color(original.getRGB(j, y));
   				 origRow.add(c);
   			 }
   			 
   			 ArrayList<Color> scrambledRow = encrypt (origRow, pr);
   			 for (int j = 0; j < original.getWidth(); j++) {
   				 int newRGB = scrambledRow.get(j).getRGB();
   				 scrambled.setRGB(j, y, newRGB);
   			 }
   		 }
   		 return scrambled;
   	 }
    
    public static BufferedImage scramble2 (BufferedImage original, int pr) {
   	 BufferedImage scrambled = new BufferedImage (original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
   	 
   	 //scramble each column
   	 for (int x = 0; x < original.getWidth(); x++) {
   		 ArrayList<Color> origCol = new ArrayList <Color> ();
   		 for (int j = 0; j < original.getHeight(); j++) {
   			 Color c = new Color(original.getRGB(x, j));
   			 origCol.add(c);
   		 }
   		 
   		 ArrayList<Color> scrambledCol = encrypt2 (origCol, pr);
   		 for (int j = 0; j < original.getHeight(); j++) {
   			 int newRGB = scrambledCol.get(j).getRGB();
   			 scrambled.setRGB(x, j, newRGB);
   		 }
   	 }
   	 return scrambled;
    }

}
