package de.xima.fc.form.expression.highlight;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.StringUtils;

/**
 * An immutable color class with (r,g,b,a) values all ranging
 * from 0 to 1. a=1 is full opacity, a=0.0 is full transparency.
 * @author mad_gaksha
 *
 */
@Immutable
public final class Color {
	public final float r,g,b,a;
	
	public Color(float r, float g, float b, float a) {
		this.r = clamp(r);
		this.g = clamp(g);
		this.b = clamp(b);
		this.a = clamp(a);
	}
	
	public Color(float r, float g, float b) {
		this(r,g,b,1f);
	}
	
	public Color(int rgba) {
		this(
			((rgba&0xFF000000)>>24)/255.0f,
			((rgba&0x00FF0000)>>16)/255.0f,
			((rgba&0x0000FF00)>>8)/255.0f,
			((rgba&0x000000FF))/255.0f);
	}
	
	private float clamp(float x) {
		return x < 0f ? 0f : x > 1f ? 1f : x;
	}
	
	public final static Color RED = new Color(1f,0f,0f);
	public final static Color GREEN = new Color(0f,1f,0f);
	public final static Color BLUE = new Color(0f,0f,1f);
	public final static Color YELLOW = new Color(1f,1f,0f);
	public final static Color PURPLE = new Color(1f,0f,1f);
	public final static Color CYAN = new Color(0f,1f,1f);
	public final static Color WHITE = new Color(1f,1f,1f);
	public final static Color BLACK = new Color(0f,0f,0f);
	public final static Color GRAY40 = new Color(0.4f,0.4f,0.4f);
	public final static Color GRAY50 = new Color(0.5f,0.5f,0.5f);

	public final static Color MAGENTA_HAZE = new Color(0x9F4576FF);

	public int getHexRgba() {
		return (((int)(r*255f))<<24)|(((int)(g*255f))<<16)|(((int)(b*255f))<<8)|((int)(a*255f));
	}

	public int getHexRgb() {
		return (((int)(r*255f))<<16)|(((int)(g*255f))<<8)|(((int)(b*255f)));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(a);
		result = prime * result + Float.floatToIntBits(b);
		result = prime * result + Float.floatToIntBits(g);
		result = prime * result + Float.floatToIntBits(r);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Color)) return false;
		final Color other = (Color) obj;
		if (Float.floatToIntBits(a) != Float.floatToIntBits(other.a))
			return false;
		if (Float.floatToIntBits(b) != Float.floatToIntBits(other.b))
			return false;
		if (Float.floatToIntBits(g) != Float.floatToIntBits(other.g))
			return false;
		if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
			return false;
		return true;
	}

	/**
	 * @return A padded hex string for the color, 6 characters long.
	 */
	public String getHexStringRgb() {
		return StringUtils.leftPad(Integer.toHexString(getHexRgb()), 6, '0');
	}
	/**
	 * @return A padded hex string for the color, 8 characters long.
	 */
	public String getHexStringRgba() {
		return StringUtils.leftPad(Integer.toHexString(getHexRgba()), 8, '0');
	}	
}
