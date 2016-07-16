package connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class ConnHelper {
	// server tcp port
		private final int port = 8839;
		
		private Selector selector;
		
		
		private ConnHandler ch;
		
		public void setConnHandler(ConnHandler ch)
		{
			this.ch = ch;
		}
		public void start()
		{
			try {
				listenOnPort(port);
				monitorSelect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		private void listenOnPort(int port) throws IOException
		{
			selector = Selector.open();

			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking( false );
			ServerSocket ss = ssc.socket();
			InetSocketAddress address = new InetSocketAddress( port );
			ss.bind( address );		
			ssc.register( selector, SelectionKey.OP_ACCEPT );
			
			System.out.println( "Going to listen on " + port );
		}
		
		private void monitorSelect() throws IOException
		{
			while (true) {
				selector.select();			
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey)it.next();
					
					if (key.isAcceptable())
					{
						Logger.getGlobal().info("accept key:" + key.toString());
						handleAccption(key);
						it.remove();
					} else if (key.isReadable())
					{
						Logger.getGlobal().info("read key " + key.toString());
						handleRead(key);
						it.remove();
					}
									
				}
			}
		}
		
		private void handleRead(SelectionKey key) {
			// TODO Auto-generated method stub
			
			SocketChannel sc = (SocketChannel)key.channel();
			
			try {
				ch.handleRead(sc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				ch.connectionDisabled(sc);
				key.cancel();
				
				e.printStackTrace();
			}
		}

		private void handleAccption(SelectionKey key) throws IOException {
	        // Accept the new connection
	        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
	        SocketChannel sc = ssc.accept();
	        sc.configureBlocking( false );

	        // Add the new connection to the selector
	        sc.register( selector, SelectionKey.OP_READ );
	        
	        System.out.println( "Got connection from "+sc );
		}
}
