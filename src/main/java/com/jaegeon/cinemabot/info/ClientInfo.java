package com.jaegeon.cinemabot.info;

import java.net.Socket;

public class ClientInfo {
	private Socket cs;
	private String id;
	
	public ClientInfo(Socket cs, String id) {
		super();
		this.cs = cs;
		this.id = id;
	}

	public Socket getCs() {
		return cs;
	}

	public String getId() {
		return id;
	}
}
