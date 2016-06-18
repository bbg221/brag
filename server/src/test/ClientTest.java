package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import brag.MessageTypes;
import brag.Tools;

public class ClientTest {
	public static void main(String[] args) {
		ClientTest ct = new ClientTest();
		
		try {
			ct.init();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Selector selector;
	
	public void init() throws IOException {
		selector = Selector.open();
		SocketChannel channel = SocketChannel.open();   
		channel.configureBlocking(false);   
		channel.connect(new InetSocketAddress("localhost", 8839));   
		channel.register(selector, SelectionKey.OP_CONNECT); 
		
		while (true) {
			selector.select();			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey)it.next();
				
				if (key.isConnectable())
				{
					SocketChannel socketChannel = (SocketChannel) key.channel();   
				    if(socketChannel.isConnectionPending())   
				        socketChannel.finishConnect();   
				    //key.cancel();
				    it.remove();
				    socketChannel.register(selector, SelectionKey.OP_READ); 
				    
				    handleWrite(socketChannel);
				    
				    
				} else if (key.isReadable())
				{
					handleRead(key);
					it.remove();
				}
				
				//it.remove();
			}
		}

	}
	
	
	private void handleWrite(SocketChannel socketChannel) throws IOException {
		ByteBuffer reqBuf = ByteBuffer.allocate(1024);
		reqBuf.clear();
		
		int reqType = MessageTypes.MESSAGE_LOGIN;
		
		JSONObject loginJson = new JSONObject();
		
		try {
			loginJson.put("loginType", 1);
			loginJson.put("loginNum", "463553109");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reqBuf.putInt(reqType);
		reqBuf.putInt(loginJson.toString().length());
		reqBuf.put(loginJson.toString().getBytes());
		
		reqBuf.flip();
		
		socketChannel.write(reqBuf);
		Logger.getGlobal().info("client send data");
	}


	private void handleRead(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		int intlen = 4;
		
		SocketChannel sc = (SocketChannel)key.channel();
		ByteBuffer typeBuf = ByteBuffer.allocate(intlen);
		ByteBuffer lenBuf = ByteBuffer.allocate(intlen);
		
		ByteBuffer[] bufferArray = {typeBuf, lenBuf};
		
		long readNum = sc.read(bufferArray);
		if (0 == readNum)
		{
			// a empty message, it doesn't make sense.
			return;
		} else if (0 > readNum) {
			IOException e = new IOException("read num : -1");
			throw e;
		}
		
		typeBuf.flip();
		lenBuf.flip();
		
		int messageType = typeBuf.getInt();
		int messageLen = lenBuf.getInt();
		
		ByteBuffer  dataBuf = ByteBuffer.allocate(messageLen);
		
		long datalen = sc.read(dataBuf);
		Logger.getGlobal().info("client recv userdata len " + datalen);
		
		dataBuf.flip();
		

		try {
			JSONObject loginJson = new JSONObject(Tools.byteBufferToString(dataBuf));
		
			int userId = 0;
			int needRefreshFlag = 0;
			try {
				userId = loginJson.getInt("userId");
				needRefreshFlag = loginJson.getInt("needRefreshFlag");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Logger.getGlobal().info("client recv userId " + userId + ", flag " + needRefreshFlag);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public void startClient() throws UnknownHostException, IOException {
		Socket s = new Socket();
		s.connect(new InetSocketAddress("localhost", 8839), 3000);
		
		System.out.println("client connect to server");
		
		DataInputStream dataInput = new DataInputStream(s.getInputStream());
		DataOutputStream dataOutput = new DataOutputStream(s.getOutputStream());
		
		JSONObject loginJson = new JSONObject();
		try {
			loginJson.put("loginType", 1);
			loginJson.put("loginNum", "463553109");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataOutput.writeInt(MessageTypes.MESSAGE_LOGIN);
		dataOutput.writeInt(loginJson.toString().length());
		dataOutput.write(loginJson.toString().getBytes());
		

		
		int t = dataInput.readInt();
		System.out.println("client receive type from server: " + t);
		

		int len = dataInput.readInt();
		System.out.println("client receive lengh from server: " + len);
		
		byte[] bytes = new byte[len];
		
		dataInput.read(bytes, 0, len);
		
		JSONObject rspJson = new JSONObject(bytes);
		
		try {
			int userId = rspJson.getInt("userId");
			int refreshFlag = rspJson.getInt("needRefreshFlag");
			
			System.out.println("get data from server userId: " + userId + ", refresh flag : " + refreshFlag);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		s.close();
		
		
	}
}
