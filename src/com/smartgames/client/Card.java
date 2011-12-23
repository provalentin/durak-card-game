package com.smartgames.client;

import com.google.gwt.user.client.ui.Image;

public class Card {
	public int id;
    public int kind;
    public String srcImage;
    public Image image;
    public int state;
	public Card(int id, int kind, String srcImage, Image image, int state) {
		super();
		this.id = id;
		this.kind = kind;
		this.srcImage = srcImage;
		this.image = image;
		this.state = state;
	}
    
    

}
