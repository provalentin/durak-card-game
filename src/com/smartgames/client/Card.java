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
	public String getSrcImage() {
		return srcImage;
	}
	public void setSrcImage(String srcImage) {
		this.srcImage = srcImage;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public int getKind() {
		return kind;
	}
    
    

}
