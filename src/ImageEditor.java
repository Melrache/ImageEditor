import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageEditor {
	private static BufferedImage img;

	public static void main(String[] args) {
		String file = args[0];
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (args.length == 3) {
			ImageEditor.increaseContrastAndBrightness(img, Double.parseDouble(args[1]), Integer.parseInt(args[2]));

		} else if (args.length == 2) {
//			ImageEditor.makeMonochrome(img);
			ImageEditor.meanBlur();
		}

//		try {
//			ImageIO.write(img, "PNG", new File("modified.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static void meanBlur() {
		BufferedImage inImage = img;
		BufferedImage outImage = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = outImage.getRaster();
		int[][] filterMatrix = new int[3][3];

		// init filterMatrix
		for (int i = 0; i < filterMatrix.length; i++) {
			for (int j = 0; j < filterMatrix[i].length; j++) {
				filterMatrix[i][j] = 1;
			}
		}

		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int channels = 0; channels < inRaster.getNumBands(); channels++) {
					int matrixAreaValue = 0;
					int missedPixelCounter = 0;
					int mI = 0, mJ = 0;
					
					for (int i = width - 1; i < width + 3; i++) {
						for (int j = height - 1; j < height + 3; j++) {
							try {
//								System.out.println(filterMatrix[i][j]);
//								System.out.println(inRaster.getSample(i, j, channels));
//								System.out.println(missedPixelCounter);
								matrixAreaValue += inRaster.getSample(i, j, channels) * 1;
//								System.out.println(matrixAreaValue);
								mJ++;
							}catch(Exception e) {
								missedPixelCounter++;
								mJ++;
							}
						}
						mI++;
					}
//					System.out.println(matrixAreaValue);
					int outPixelValue = matrixAreaValue/9;
					if(outPixelValue > 255) {
						outPixelValue = 255;
					}else if(outPixelValue < 0) {
						outPixelValue = 0;
					}
					outRaster.setSample(width, height, channels, outPixelValue);
					
				}
			}
		}
		try {
			ImageIO.write(outImage, "PNG", new File("modified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void detectEdges() {

	}

	public static void increaseContrastAndBrightness(BufferedImage inImage, double contrast, int brightness) {
		BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = image.getRaster();
		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int colour = 0; colour < inRaster.getNumBands(); colour++) {
					int rgbPixelValue = inRaster.getSample(width, height, colour);
					rgbPixelValue = (int) (((((rgbPixelValue / 255.0) - 0.5) * contrast) + 0.5) * 255.0) + brightness;
					if (rgbPixelValue > 255) {
						rgbPixelValue = 255;
					} else if (rgbPixelValue < 0) {
						rgbPixelValue = 0;
					}
					outRaster.setSample(width, height, colour, rgbPixelValue);
				}
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("modified.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void makeMonochrome(BufferedImage inImage) {
		BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster inRaster = inImage.getRaster();
		WritableRaster outRaster = image.getRaster();
		int red = -1, green = -1, blue = -1;
		for (int width = 0; width < inRaster.getWidth(); width++) {
			for (int height = 0; height < inRaster.getHeight(); height++) {
				for (int colour = 0; colour < inRaster.getNumBands(); colour++) {
					if (colour == 0) {
						red = inRaster.getSample(width, height, colour);
					} else if (colour == 1) {
						blue = inRaster.getSample(width, height, colour);
					} else if (colour == 2) {
						green = inRaster.getSample(width, height, colour);
						outRaster.setSample(width, height, 0, (red + green + blue) / 3);
					}
				}
			}
		}
		try {
			ImageIO.write(image, "PNG", new File("monochrome.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
