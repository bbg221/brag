package brag.networkWithClient;

import java.nio.channels.SocketChannel;

public interface ConnHandler {
	public void handleRead(SocketChannel sc) throws Exception; 
	public void connectionDisabled(SocketChannel sc);
	
}
