package com.floorplansapi.imageprocessor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageController {
	/*
	 * compressMultipartFileImage compress multipartFile type to requested width and
	 * return compressed byte array of image
	 * @Parms MultipartFile and Integer	 
	 * @return byte[] of image
	 */
	public byte[] compressMultiparFileImage(MultipartFile orginalFile, Integer width) throws IOException{  
	    ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();  
	    BufferedImage thumbImg = null;  
	    BufferedImage img = ImageIO.read(orginalFile.getInputStream());  
	    thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);  
	    ImageIO.write(thumbImg, orginalFile.getContentType().split("/")[1] , thumbOutput);  
	    return thumbOutput.toByteArray();  
	}
}
