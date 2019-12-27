package tests;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ImageTransferClient {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 13085);
		OutputStream outputStream = socket.getOutputStream();

		BufferedImage image = ImageIO.read(new File("E:\\songzz\\Eminem - Kamikaze - (SongsLover.com)\\Cover.jpg"));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", byteArrayOutputStream);

		byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
		outputStream.write(size);
		outputStream.write(byteArrayOutputStream.toByteArray());
		outputStream.flush();

		Thread.sleep(1200000);
		
		outputStream.close();
		socket.close();
	}
}
