package brag;

import java.nio.channels.SocketChannel;

public interface LoginHandler {
	public void saveSocketChannel(int userId, SocketChannel sc);
}
