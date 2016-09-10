package com.ztesoft.socketClt;


import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

public class SocketCltFactory extends BasePoolableObjectFactory<AsyncSocketClient> {

	private static final Logger logger = Logger.getLogger(SocketCltFactory.class);

	// private AsyncSocketClient asynSocketClt;

	String host = "";
	int port;

	SocketCltFactory(String host, int port) {
		this.host = host;
		this.port = port;

	}

	@Override
	public AsyncSocketClient makeObject() throws Exception {
		// TODO Auto-generated method stub
		logger.info("make an asyncSocketClent!");
		AsyncSocketClient asynSocketClt = new AsyncSocketClient(host, port);
		asynSocketClt.open();
		return asynSocketClt;
	}

	@Override
	public void destroyObject(AsyncSocketClient obj) throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("destroyObject。。。。。。");

		try {
			obj.close();
			obj = null;
		} catch (Exception e) {
			logger.error(e);
			super.destroyObject(obj);
		}

	}

	@Override
	public boolean validateObject(AsyncSocketClient obj) {
		// TODO Auto-generated method stub
		// System.out.println("峇佩validate。。。。。。");

		AsyncSocketClient asyncSocketClt = obj;
		if (!asyncSocketClt.isOpen()) {

			return false;
		}

		return true;

	}

}
